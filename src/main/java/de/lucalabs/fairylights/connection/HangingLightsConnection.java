package de.lucalabs.fairylights.connection;

import de.lucalabs.fairylights.blocks.FairyLightBlocks;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.feature.FeatureType;
import de.lucalabs.fairylights.feature.light.Light;
import de.lucalabs.fairylights.feature.light.LightBehavior;
import de.lucalabs.fairylights.items.HangingLightsConnectionItem;
import de.lucalabs.fairylights.items.LightVariant;
import de.lucalabs.fairylights.items.SimpleLightVariant;
import de.lucalabs.fairylights.sounds.FairyLightSounds;
import de.lucalabs.fairylights.string.StringType;
import de.lucalabs.fairylights.string.StringTypes;
import de.lucalabs.fairylights.util.ItemHelper;
import de.lucalabs.fairylights.util.Tags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.*;

public final class HangingLightsConnection extends HangingFeatureConnection<Light<?>> {
    private static final int MAX_LIGHT = 15;
    private static final int LIGHT_UPDATE_WAIT = 400;
    private static final int LIGHT_UPDATE_RATE = 10;

    private final Set<BlockPos> litBlocks = new HashSet<>();
    private final Set<BlockPos> oldLitBlocks = new HashSet<>();

    private StringType string;
    private List<ItemStack> pattern;
    private boolean isOn = true;
    private int lightUpdateTime = (int) (Math.random() * LIGHT_UPDATE_WAIT / 2);

    private int lightUpdateIndex;

    public HangingLightsConnection(
            final ConnectionType<? extends HangingLightsConnection> type,
            final World world,
            final Fastener<?> fastenerOrigin,
            final UUID uuid) {

        super(type, world, fastenerOrigin, uuid);
        this.string = StringTypes.BLACK_STRING;
        this.pattern = new ArrayList<>();
    }

    public StringType getString() {
        return this.string;
    }

    @Override
    public boolean interact(
            final PlayerEntity player,
            final Vec3d hit,
            final FeatureType featureType,
            final int feature,
            final ItemStack heldStack,
            final Hand hand) {

        if (featureType == FEATURE && heldStack.isIn(Tags.LIGHTS)) {
            final int index = feature % this.pattern.size();
            final ItemStack light = this.pattern.get(index);
            if (!ItemStack.areEqual(light, heldStack)) {
                final ItemStack placed = heldStack.split(1);
                this.pattern.set(index, placed);
                ItemHelper.giveItemToPlayer(player, light);
                this.computeCatenary();
                this.world.playSound(
                        null,
                        hit.x,
                        hit.y,
                        hit.z,
                        FairyLightSounds.FEATURE_COLOR_CHANGE,
                        SoundCategory.BLOCKS,
                        1,
                        1);
                return true;
            }
        }

        if (super.interact(player, hit, featureType, feature, heldStack, hand)) {
            return true;
        }

        this.isOn = !this.isOn;
        final SoundEvent lightSnd;
        final float pitch;

        if (this.isOn) {
            lightSnd = FairyLightSounds.FEATURE_LIGHT_TURNON;
            pitch = 0.6F;
        } else {
            lightSnd = FairyLightSounds.FEATURE_LIGHT_TURNOFF;
            pitch = 0.5F;
        }

        this.world.playSound(null, hit.x, hit.y, hit.z, lightSnd, SoundCategory.BLOCKS, 1, pitch);
        this.computeCatenary();
        return true;
    }

    @Override
    public void onUpdate() {
        final boolean on = !this.isDynamic() && this.isOn;
        for (final Light<?> light : this.features) {
            light.tick(this.world, this.fastener.getConnectionPoint());
        }
        if (on && this.features.length > 0) {
            this.lightUpdateTime++;
            if (this.lightUpdateTime > LIGHT_UPDATE_WAIT && this.lightUpdateTime % LIGHT_UPDATE_RATE == 0) {
                if (this.lightUpdateIndex >= this.features.length) {
                    this.lightUpdateIndex = 0;
                    this.lightUpdateTime = this.world.random.nextInt(LIGHT_UPDATE_WAIT / 2);
                } else {
                    this.setLight(BlockPos.ofFloored(this.features[this.lightUpdateIndex++].getAbsolutePoint(this.fastener)));
                }
            }
        }
    }

    private void updateNeighbors(final Fastener<?> fastener) {
        this.world.updateComparators(fastener.getPos(), FairyLightBlocks.FASTENER);
    }

    @Override
    protected Light<?>[] createFeatures(final int length) {
        return new Light<?>[length];
    }

    @Override
    protected boolean canReuse(final Light<?> feature, final int index) {
        return ItemStack.areEqual(feature.getItem(), this.getPatternStack(index));
    }

    @Override
    protected Light<?> createFeature(final int index, final Vec3d point, final float yaw, final float pitch) {
        final ItemStack lightData = this.getPatternStack(index);
        return this.createLight(index, point, yaw, pitch, lightData, LightVariant.get(lightData).orElse(SimpleLightVariant.FAIRY_LIGHT));
    }

    private ItemStack getPatternStack(final int index) {
        return this.pattern.isEmpty() ? ItemStack.EMPTY : this.pattern.get(index % this.pattern.size());
    }

    @Override
    protected void updateFeature(final Light<?> light) {
        super.updateFeature(light);
        if (!this.isDynamic() && this.isOn) {
            final BlockPos pos = BlockPos.ofFloored(light.getAbsolutePoint(this.fastener));
            this.litBlocks.add(pos);
            this.setLight(pos);
        }
    }

    private <T extends LightBehavior> Light<T> createLight(final int index, final Vec3d point, final float yaw, final float pitch, final ItemStack stack, final LightVariant<T> variant) {
        return new Light<>(index, point, yaw, pitch, stack, variant, 0.125F);
    }

    @Override
    protected float getFeatureSpacing() {
        if (this.pattern.isEmpty()) {
            return SimpleLightVariant.FAIRY_LIGHT.getSpacing();
        }
        float spacing = 0;
        for (final ItemStack patternLightData : this.pattern) {
            final float lightSpacing = LightVariant.get(patternLightData).orElse(SimpleLightVariant.FAIRY_LIGHT).getSpacing();
            if (lightSpacing > spacing) {
                spacing = lightSpacing;
            }
        }
        return spacing;
    }

    @Override
    protected void onBeforeUpdateFeatures() {
        this.oldLitBlocks.clear();
        this.oldLitBlocks.addAll(this.litBlocks);
        this.litBlocks.clear();
    }

    @Override
    protected void onAfterUpdateFeatures() {
        final boolean on = !this.isDynamic() && this.isOn;
        for (final Light<?> light : this.features) {
            light.power(on, this.isDynamic() || this.prevCatenary == null);
        }
        this.oldLitBlocks.removeAll(this.litBlocks);
        final Iterator<BlockPos> oldIter = this.oldLitBlocks.iterator();
        while (oldIter.hasNext()) {
            this.removeLight(oldIter.next());
            oldIter.remove();
        }
    }

    @Override
    public void onRemove() {
        // TODO this causes the game to freeze, it cost me 10 hours to track this down
        // TODO check if removing this breaks anything
//        for (final BlockPos pos : this.litBlocks) {
//            this.removeLight(pos);
//        }
    }

    private void removeLight(final BlockPos pos) {
        if (this.world.getBlockState(pos).isOf(Blocks.LIGHT)) {
            this.world.removeBlock(pos, false);
        }
    }

    private void setLight(final BlockPos pos) {
        if (this.world.canSetBlock(pos) && this.world.isAir(pos) && this.world.getLightLevel(LightType.BLOCK, pos) < MAX_LIGHT) {
            this.world.setBlockState(pos, Blocks.LIGHT.getDefaultState().with(LightBlock.LEVEL_15, 15), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    public NbtCompound serialize() {
        final NbtCompound compound = super.serialize();
        compound.putBoolean("isOn", this.isOn);
        final NbtList litBlocks = new NbtList();
        for (final BlockPos litBlock : this.litBlocks) {
            litBlocks.add(NbtHelper.fromBlockPos(litBlock));
        }
        compound.put("litBlocks", litBlocks);
        return compound;
    }

    @Override
    public void deserialize(final NbtCompound compound) {
        super.deserialize(compound);
        this.isOn = compound.getBoolean("isOn");
        this.litBlocks.clear();
        final NbtList litBlocks = compound.getList("litBlocks", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < litBlocks.size(); i++) {
            this.litBlocks.add(NbtHelper.toBlockPos(litBlocks.getCompound(i)));
        }
    }

    @Override
    public NbtCompound serializeLogic() {
        final NbtCompound compound = super.serializeLogic();
        HangingLightsConnectionItem.setString(compound, this.string);
        final NbtList tagList = new NbtList();
        for (final ItemStack light : this.pattern) {
            tagList.add(light.writeNbt(new NbtCompound()));
        }
        compound.put("pattern", tagList);
        return compound;
    }

    @Override
    public void deserializeLogic(final NbtCompound compound) {
        super.deserializeLogic(compound);
        this.string = HangingLightsConnectionItem.getString(compound);
        final NbtList patternList = compound.getList("pattern", NbtElement.COMPOUND_TYPE);
        this.pattern = new ArrayList<>();
        for (int i = 0; i < patternList.size(); i++) {
            final NbtCompound lightCompound = patternList.getCompound(i);
            this.pattern.add(ItemStack.fromNbt(lightCompound));
        }
    }
}
