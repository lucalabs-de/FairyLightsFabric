package de.lucalabs.fairylights.renderer.block.entity;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.connection.PennantBuntingConnection;
import de.lucalabs.fairylights.feature.Pennant;
import de.lucalabs.fairylights.items.FairyLightItems;
import de.lucalabs.fairylights.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.util.Curve;
import de.lucalabs.fairylights.util.styled.StyledString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import java.util.function.Function;

public class PennantBuntingRenderer extends ConnectionRenderer<PennantBuntingConnection> {
    private static final Identifier TRIANGLE_MODEL = Identifier.of(FairyLights.ID, "entity/triangle_pennant");
    private static final Identifier SQUARE_MODEL = Identifier.of(FairyLights.ID, "entity/square_pennant");

    public static final ImmutableSet<Identifier> MODELS = ImmutableSet.of(TRIANGLE_MODEL, SQUARE_MODEL);

    private final ImmutableMap<Item, Identifier> models = ImmutableMap.of(
            FairyLightItems.TRIANGLE_PENNANT, TRIANGLE_MODEL,
            FairyLightItems.SQUARE_PENNANT, SQUARE_MODEL
    );

    public PennantBuntingRenderer(final Function<EntityModelLayer, ModelPart> baker) {
        super(baker, FairyLightModelLayers.PENNANT_WIRE, 0.25F);
    }

    @Override
    protected void render(
            final PennantBuntingConnection conn,
            final Curve catenary,
            final float delta,
            final MatrixStack matrix,
            final VertexConsumerProvider source,
            final int packedLight,
            final int packedOverlay) {

        super.render(conn, catenary, delta, matrix, source, packedLight, packedOverlay);
        final Pennant[] currLights = conn.getFeatures();
        if (currLights != null) {
            // TODO I decided not to implement pennant text for now
//            final Font font = MinecraftClient.getInstance().font;
            final VertexConsumer buf = source.getBuffer(TexturedRenderLayers.getEntityCutout());
            final int count = currLights.length;
            if (count == 0) {
                return;
            }
//            StyledString text = conn.getText();
//            if (text.length() > count) {
//                text = text.substring(0, count);
//            }
//            final int offset = (count - text.length()) / 2;
            for (int i = 0; i < count; i++) {
                final Pennant currPennant = currLights[i];
                final int color = currPennant.getColor();
                final float r = ((color >> 16) & 0xFF) / 255.0F;
                final float g = ((color >> 8) & 0xFF) / 255.0F;
                final float b = (color & 0xFF) / 255.0F;

                final BakedModel model = MinecraftClient
                        .getInstance()
                        .getBakedModelManager()
                        .getModel(this.models.getOrDefault(currPennant.getItem(), TRIANGLE_MODEL));

                final Vec3d pos = currPennant.getPoint(delta);
                matrix.push();
                matrix.translate(pos.x, pos.y, pos.z);
                matrix.multiply(RotationAxis.POSITIVE_Y.rotation(-currPennant.getYaw(delta)));
                matrix.multiply(RotationAxis.POSITIVE_Z.rotation(currPennant.getPitch(delta)));
                matrix.multiply(RotationAxis.POSITIVE_X.rotation(currPennant.getRoll(delta)));
                matrix.push();
                FastenerRenderer.renderBakedModel(model, matrix, buf, r, g, b, packedLight, packedOverlay);
                matrix.pop();
//                if (i >= offset && i < offset + text.length()) {
//                    this.drawLetter(matrix, source, currPennant, packedLight, font, text, i - offset, 1, delta);
//                    this.drawLetter(matrix, source, currPennant, packedLight, font, text, text.length() - 1 - (i - offset), -1, delta);
//                }
                matrix.pop();
            }
        }
    }

    public static float diffuseLight(float p_144949_, float p_144950_, float p_144951_) {
        return Math.min(p_144949_ * p_144949_ * 0.6F + p_144950_ * p_144950_ * ((3.0F + p_144950_) / 4.0F) + p_144951_ * p_144951_ * 0.8F, 1.0F);
    }


    public static TexturedModelData wireLayer() {
        return WireModel.createLayer(0, 17, 1);
    }
}
