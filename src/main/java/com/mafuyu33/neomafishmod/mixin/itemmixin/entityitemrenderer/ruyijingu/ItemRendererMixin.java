package com.mafuyu33.neomafishmod.mixin.itemmixin.entityitemrenderer.ruyijingu;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.event.KeyInputHandler;
import com.mafuyu33.neomafishmod.item.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
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
	private  final Minecraft mc = Minecraft.getInstance();

	@Unique
	private float neomafishmod$currentScale = 1.0f;

	@Inject(method = "render",at= @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
	public void renderItem(
			ItemStack itemStack,
			ItemDisplayContext displayContext,
			boolean leftHand,
			PoseStack poseStack,
			MultiBufferSource bufferSource,
			int combinedLight,
			int combinedOverlay,
			BakedModel p_model,
			CallbackInfo ci
	){
		if (BuiltInRegistries.ITEM.getKey(itemStack.getItem()).equals(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,"ruyijingu")))
		{
			if(displayContext != ItemDisplayContext.GUI) {
				// 设置缩放的增量
				float scaleIncrement = 0.1f;

				if (KeyInputHandler.plusKey.isDown()) {
					neomafishmod$currentScale += scaleIncrement;
				} else if (KeyInputHandler.minusKey.isDown()) {
					neomafishmod$currentScale = Math.max(0.1f, neomafishmod$currentScale - scaleIncrement);
				}
				poseStack.scale(neomafishmod$currentScale, neomafishmod$currentScale, neomafishmod$currentScale);
			}
		}

	}
}