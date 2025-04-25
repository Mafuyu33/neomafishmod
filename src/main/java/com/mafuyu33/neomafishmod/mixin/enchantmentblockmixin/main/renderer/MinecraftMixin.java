package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.main.renderer;

import com.mafuyu33.neomafishmod.enchantmentblock.EnchantedBlockRenderer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

	@Inject(method = "resizeDisplay",at = @At("RETURN"))
	private void neomafishmodResizeHud(CallbackInfo ci){
		EnchantedBlockRenderer.resize();
	}
}