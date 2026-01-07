package de.lucalabs.fairylights.connection;

import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.feature.FeatureType;
import de.lucalabs.fairylights.feature.Pennant;
import de.lucalabs.fairylights.items.DyeableItem;
import de.lucalabs.fairylights.sounds.FairyLightSounds;
import de.lucalabs.fairylights.util.ItemHelper;
import de.lucalabs.fairylights.util.Tags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class PennantBuntingConnection extends HangingFeatureConnection<Pennant> {
    private List<ItemStack> pattern;

    public PennantBuntingConnection(
            final ConnectionType<? extends PennantBuntingConnection> type,
            final World world,
            final Fastener<?> fastener,
            final UUID uuid) {

        super(type, world, fastener, uuid);
        this.pattern = new ArrayList<>();
    }

    @Override
    public float getRadius() {
        return 0.045F;
    }

    @Override
    public boolean interact(
            final PlayerEntity player,
            final Vec3d hit,
            final FeatureType featureType,
            final int feature,
            final ItemStack heldStack,
            final Hand hand) {

        if (featureType == FEATURE && heldStack.isIn(Tags.PENNANTS)) { // OreDictUtils.isDye(heldStack)) {
            final int index = feature % this.pattern.size();
            final ItemStack pennant = this.pattern.get(index);
            if (!ItemStack.areEqual(pennant, heldStack)) {
                final ItemStack placed = heldStack.split(1);
                this.pattern.set(index, placed);

                ItemHelper.giveItemToPlayer(player, pennant);

                this.computeCatenary();
                this.world.playSound(null, hit.x, hit.y, hit.z, FairyLightSounds.FEATURE_COLOR_CHANGE, SoundCategory.BLOCKS, 1, 1);
                return true;
            }
        }
        return super.interact(player, hit, featureType, feature, heldStack, hand);
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();
        for (final Pennant light : this.features) {
            light.tick(this.world);
        }
    }

    @Override
    protected boolean canReuse(final Pennant feature, final int index) {
        return false;
    }

    @Override
    protected Pennant[] createFeatures(final int length) {
        return new Pennant[length];
    }

    @Override
    protected Pennant createFeature(final int index, final Vec3d point, final float yaw, final float pitch) {
        final ItemStack data = this.pattern.isEmpty() ? ItemStack.EMPTY : this.pattern.get(index % this.pattern.size());
        return new Pennant(index, point, yaw, pitch, DyeableItem.getColor(data), data.getItem());
    }

    @Override
    protected float getFeatureSpacing() {
        return 0.6875F;
    }

    @Override
    public NbtCompound serializeLogic() {
        final NbtCompound compound = super.serializeLogic();
        final NbtList patternList = new NbtList();
        for (final ItemStack entry : this.pattern) {
            patternList.add(entry.writeNbt(new NbtCompound()));
        }
        compound.put("pattern", patternList);
        return compound;
    }

    @Override
    public void deserializeLogic(final NbtCompound compound) {
        super.deserializeLogic(compound);
        this.pattern = new ArrayList<>();
        final NbtList patternList = compound.getList("pattern", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < patternList.size(); i++) {
            this.pattern.add(ItemStack.fromNbt(patternList.getCompound(i)));
        }
    }
}
