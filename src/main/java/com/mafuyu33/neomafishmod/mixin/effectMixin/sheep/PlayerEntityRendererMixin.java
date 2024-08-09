package com.mafuyu33.neomafishmod.mixin.effectMixin.sheep;

import com.mafuyu33.neomafishmod.effect.ModEffects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerEntityRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

//    @Shadow
//    private static HumanoidModel.ArmPose getArmPose(AbstractClientPlayer player, InteractionHand hand) {
//        return null;
//    }

    @Redirect(
            method = "render*" ,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    private void redirectRender(PlayerRenderer renderer,AbstractClientPlayer entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        LivingEntity sheep = new Sheep(EntityType.SHEEP, Minecraft.getInstance().level);

        if(entity.hasEffect(ModEffects.SHEEP_EFFECT)) {
            EntityRenderer sheepRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(sheep);

            LimbAnimatorAccessor target = (LimbAnimatorAccessor) sheep.walkAnimation;
            LimbAnimatorAccessor source = (LimbAnimatorAccessor) entity.walkAnimation;
            target.setSpeedOld(source.getSpeedOld());
            target.setSpeed(source.getSpeed());
            target.setPosition(source.getPosition());
            sheep.swinging = entity.swinging;
            sheep.swingTime = entity.swingTime;
            sheep.oAttackAnim = entity.oAttackAnim;
            sheep.attackAnim = entity.attackAnim;
            sheep.yBodyRot = entity.yBodyRot;
            sheep.yBodyRotO = entity.yBodyRotO;
            sheep.yHeadRot = entity.yHeadRot;
            sheep.yHeadRotO = entity.yHeadRotO;
            sheep.tickCount = entity.tickCount;
            sheep.swingingArm = entity.swingingArm;
            sheep.setOnGround(entity.onGround());
            sheep.setDeltaMovement(entity.getDeltaMovement());

            sheep.setPose(entity.getPose());
            // 将俯仰角度设置为玩家的俯仰角度
            // 将前一帧俯仰角度设置为玩家的前一帧俯仰角度
            sheep.setXRot(entity.getXRot());
            sheep.xRotO = entity.xRotO;

            sheepRenderer.render(sheep,entityYaw, partialTicks, poseStack, buffer, packedLight);
        } else {
            super.render((AbstractClientPlayer) entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }


}
