//package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.mega_size;
//
//import com.mafuyu33.neomafishmod.NeoMafishMod;
//import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
//import com.mafuyu33.neomafishmod.event.KeyInputHandler;
//import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.entity.ItemRenderer;
//import net.minecraft.client.resources.model.BakedModel;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.ItemDisplayContext;
//import net.minecraft.world.item.ItemStack;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
///**
// * @author Mafuyu33
// */
//@Mixin(ItemRenderer.class)
//public class ItemRendererMixin {
//
//	@Shadow @Final private Minecraft minecraft;
//
//	@Inject(method = "render",at= @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
//	public void renderItem(
//			ItemStack itemStack,
//			ItemDisplayContext displayContext,
//			boolean leftHand,
//			PoseStack poseStack,
//			MultiBufferSource bufferSource,
//			int combinedLight,
//			int combinedOverlay,
//			BakedModel p_model,
//			CallbackInfo ci
//	){
//		int i = InjectHelper.getEnchantmentLevel(itemStack, ModEnchantments.PAY_TO_PLAY);
//		if (i>0)
//		{
//			if(displayContext != ItemDisplayContext.GUI) {
//				if(BuiltInRegistries.ITEM.getKey(itemStack.getItem()).equals(ResourceLocation.fromNamespaceAndPath("minecraft","shield"))){
//					// 对盾牌设置缩放的增量
//					float scaleFactor = 1.0f + (2.0f * i / 5.0f);
//
//					poseStack.scale(scaleFactor, scaleFactor, scaleFactor);
//					float translationFactor = 0.1f + (0.2f * i / 5.0f);
//					poseStack.translate(0, 0, 0);
//				}else {
//					// 对其他物品设置缩放的增量
//					float scaleFactor = 1.0f + (2.0f * i / 5.0f);
//
//					poseStack.scale(scaleFactor, scaleFactor, scaleFactor);
//				}
//			}
//		}
//
//	}
//}