package com.mafuyu33.neomafishmod.mixin;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(Item.class)
public abstract class ExampleMixin {
//	@Inject(method = "on",at = @At("HEAD"))
//	private void init() {
//		// This code is injected into the start of MinecraftServer.loadWorld()V
//	}
}