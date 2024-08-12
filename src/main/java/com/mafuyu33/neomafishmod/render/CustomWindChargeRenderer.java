package com.mafuyu33.neomafishmod.render;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.entity.custom.CustomWindChargeEntity;
import com.mafuyu33.neomafishmod.network.packet.S2C.CustomWindChargeS2CPacket;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.WindChargeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import com.mojang.blaze3d.vertex.PoseStack;

public class CustomWindChargeRenderer extends EntityRenderer<CustomWindChargeEntity> {
    private static final float MIN_CAMERA_DISTANCE_SQUARED = Mth.square(3.5F);
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/projectiles/wind_charge.png");
    private final WindChargeModel model;

    public CustomWindChargeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new WindChargeModel(context.bakeLayer(ModelLayers.WIND_CHARGE));
    }

    @Override
    public ResourceLocation getTextureLocation(CustomWindChargeEntity customWindChargeEntity) {
        return TEXTURE_LOCATION;
    }

    public void render(CustomWindChargeEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < (double)MIN_CAMERA_DISTANCE_SQUARED)) {
            float f = (float) entity.tickCount + partialTick;

            poseStack.pushPose();
//            NeoMafishMod.LOGGER.info("x="+ CustomWindChargeS2CPacket.CustomWindChargeData.getRadius(entity.getUUID()));

            if(CustomWindChargeS2CPacket.CustomWindChargeData.getRadius(entity.getUUID()) != 0) {
                int r = CustomWindChargeS2CPacket.CustomWindChargeData.getRadius(entity.getUUID());
                // 使用计算出来的缩放比例来调整实体的渲染大小
                poseStack.scale(r, r, r);
            }

            VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.breezeWind(TEXTURE_LOCATION, this.xOffset(f) % 1.0F, 0.0F));
            this.model.setupAnim(entity, 0.0F, 0.0F, f, 0.0F, 0.0F);
            this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
            poseStack.popPose();
        }
    }



    protected float xOffset(float tickCount) {
        return tickCount * 0.03F;
    }
}
