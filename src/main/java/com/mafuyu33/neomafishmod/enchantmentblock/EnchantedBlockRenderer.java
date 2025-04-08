//package com.mafuyu33.neomafishmod.enchantmentblock;
//
//import com.mafuyu33.neomafishmod.NeoMafishMod;
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.textures.GpuTexture;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.LightTexture;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.client.renderer.texture.TextureManager;
//import net.minecraft.core.BlockPos;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.block.state.BlockState;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.api.distmarker.OnlyIn;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
//
//import java.util.Set;
//
///**
// * @author Mafuyu33
// */
//@OnlyIn(Dist.CLIENT)
//public class EnchantedBlockRenderer {
//    // 附魔效果使用的纹理资源位置
////    private static final ResourceLocation GLINT_LOCATION = new ResourceLocation("minecraft", "textures/misc/enchanted_item_glint.png");
//
//    @SubscribeEvent
//    public static void onRenderLevel(RenderLevelStageEvent event) {
//        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
//            return;
//        }
//
//        Minecraft minecraft = Minecraft.getInstance();
//        if (minecraft.level == null || minecraft.player == null) {
//            return;
//        }
//
//        Set<BlockPos> enchantedBlocks = BlockEnchantmentStorage.getAllEnchantedBlocks();
//        if (enchantedBlocks.isEmpty()) {
//            return;
//        }
//
//        PoseStack poseStack = event.getPoseStack();
//        poseStack.pushPose();
//
//        // 获取相机位置
//        double cameraX = minecraft.gameRenderer.getMainCamera().getPosition().x;
//        double cameraY = minecraft.gameRenderer.getMainCamera().getPosition().y;
//        double cameraZ = minecraft.gameRenderer.getMainCamera().getPosition().z;
//
//        poseStack.translate(-cameraX, -cameraY, -cameraZ);
//
//        // 获取缓冲源
//        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
//
//        // 渲染每个附魔方块
//        for (BlockPos pos : enchantedBlocks) {
//            if (!minecraft.level.getBlockState(pos).isAir()) {
//                renderEnchantedBlock(poseStack, bufferSource, pos);
//            }
//        }
//
//        // 结束批量渲染
//        bufferSource.endBatch();
//        poseStack.popPose();
//    }
//
//    private static void renderEnchantedBlock(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, BlockPos pos) {
//        Minecraft minecraft = Minecraft.getInstance();
//        BlockState state = minecraft.level.getBlockState(pos);
//
//        // 保存渲染状态
//        poseStack.pushPose();
//        // 移动到方块位置
//        poseStack.translate(pos.getX(), pos.getY(), pos.getZ());
//
//        // 首先正常渲染方块
//        minecraft.getBlockRenderer().renderSingleBlock(
//                state,
//                poseStack,
//                bufferSource,
//                LightTexture.FULL_BRIGHT,
//                OverlayTexture.NO_OVERLAY
//        );
//
//        // 在1.21.x中，使用entity_glint可能是最接近原版附魔效果的选择
//        RenderType glintType = RenderType.entityGlint();
//
//        // 获取附魔效果的VertexConsumer
//        VertexConsumer enchantedConsumer = bufferSource.getBuffer(glintType);
//
//        // 获取纹理管理器和GpuTexture
//        TextureManager textureManager = minecraft.getTextureManager();
//        // 获取与GLINT_LOCATION对应的纹理，这在1.21.x中使用了GpuTexture
//        GpuTexture glintTexture = RenderSystem.getShaderTexture(0); // 保存当前纹理
//
//        // 设置附魔效果的变换矩阵
//        poseStack.pushPose();
//        poseStack.scale(1.01F, 1.01F, 1.01F); // 略微放大以避免z-fighting
//        poseStack.translate(-0.005F, -0.005F, -0.005F);
//
//        // 设置着色器纹理 - 在现代版本中，RenderType已经配置了正确的纹理，不需要手动设置
//
//        // 再次渲染方块，但这次使用附魔效果渲染类型
//        minecraft.getBlockRenderer().renderSingleBlock(
//                state,
//                poseStack,
//                bufferSource,  // 使用原始bufferSource
//                LightTexture.FULL_BRIGHT,
//                OverlayTexture.NO_OVERLAY
//        );
//
//        // 恢复原始纹理状态
//        RenderSystem.setShaderTexture(0, glintTexture);
//
//        poseStack.popPose();
//        poseStack.popPose();
//    }
//}