package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.one_with_shadows;

import com.mafuyu33.neomafishmod.network.packet.S2C.OneWithShadowS2CPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.renderer.entity.LivingEntityRenderer.class)
public abstract class LivingEntityRenderer {
    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",at = @At("HEAD"), cancellable = true)
    private void init(LivingEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        //穿着特定附魔盔甲的实体,并且实体在运动的时候
        if(OneWithShadowS2CPacket.getFlagById(entity.getId())==1) {
//            System.out.println("实体在运动");
        } else if(OneWithShadowS2CPacket.getFlagById(entity.getId())==0) {
//            System.out.println("实体没有在运动");
            //取消渲染
            ci.cancel();
        }
    }
}