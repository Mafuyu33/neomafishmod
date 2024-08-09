package com.mafuyu33.neomafishmod.mixin.itemmixin.entityitemrenderer.villageritemrenderer;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Unique
    private final Minecraft mc = Minecraft.getInstance();

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void renderItem(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel p_model, CallbackInfo ci) {
        // 检查是否是特定物品
        if (BuiltInRegistries.ITEM.getKey(itemStack.getItem()).equals(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID, "villager_item"))) {
            // 取消默认渲染
            ci.cancel();

            // 渲染生物模型，例如羊驼
            Villager villager = new Villager(EntityType.VILLAGER, mc.level);
            poseStack.pushPose();
            // 使用 org.joml.Quaternionf 进行旋转
//            Quaternionf rotation = new Quaternionf().rotateY((float) Math.toRadians(180));
//            matrices.multiply(rotation);

            poseStack.scale(0.5F, 0.5F, 0.5F);// 调整缩放比例
            mc.getEntityRenderDispatcher().render(villager, 0, 0, 0, 0.0F, 1.0F, poseStack, bufferSource, combinedLight);
            poseStack.popPose();
        }
    }
}