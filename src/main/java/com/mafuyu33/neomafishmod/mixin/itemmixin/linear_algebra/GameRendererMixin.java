package com.mafuyu33.neomafishmod.mixin.itemmixin.linear_algebra;

import com.llamalad7.mixinextras.sugar.Local;
import com.mafuyu33.neomafishmod.event.AttackKeyCheckHandler;
import com.mafuyu33.neomafishmod.event.KeyInputHandler;
import com.mafuyu33.neomafishmod.util.TransformManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Mafuyu33
 */
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Inject(method = "renderLevel",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V"))
	private void init(DeltaTracker deltaTracker, CallbackInfo ci , @Local(ordinal = 0) Matrix4f matrix4f) {
		if(KeyInputHandler.minusKey.isDown()){
			//对matrix4f进行一些变换
			TransformManager.applyTransform(matrix4f);
		}
	}
}