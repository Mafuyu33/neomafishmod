package com.mafuyu33.neomafishmod.mixin.itemmixin.entityitemrenderer.irongolemitemrenderer;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Unique
    private final Minecraft mc = Minecraft.getInstance();
//
//    @Inject(method = "renderStatic(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;IILcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;I)V",
//            at = @At("HEAD"),
//            cancellable = true)
//    public void renderItem(ItemStack itemStack, ItemDisplayContext displayContext, int combinedLight, int combinedOverlay,
//                           PoseStack poseStack, MultiBufferSource bufferSource, Level level, int seed, CallbackInfo ci) {
//        // 检查是否是特定物品
//        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
//        if (itemId != null && itemId.equals(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID, "iron_golem_item"))) {
//            // 取消默认渲染
//            ci.cancel();
//
//            // 确保客户端世界可用
//            if (mc.level == null) return;
//
//            // 创建铁傀儡实例
//            IronGolem ironGolem = new IronGolem(EntityType.IRON_GOLEM, mc.level);
//
//            // 设置渲染参数
//            poseStack.pushPose();
//
//            // 缩放模型使其适合物品显示
//            poseStack.scale(0.5F, 0.5F, 0.5F);
//
//            // 渲染实体
//            mc.getEntityRenderDispatcher().render(
//                    ironGolem,
//                    0, 0, 0,  // 位置
//                    0.0F,     // yaw旋转
//                    1.0F,     // partialTick
//                    poseStack,
//                    bufferSource,
//                    combinedLight
//            );
//
//            poseStack.popPose();
//        }
//    }
}