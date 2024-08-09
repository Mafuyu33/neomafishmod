package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.slippery;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class UnPickUpAbleItemMixin {
	@Shadow
	public abstract void setNeverPickUp();

	@Shadow public abstract ItemStack getItem();

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
//		if(EnchantmentHelper.getLevel(ModEnchantments.SLIPPERY,this.getStack())>0) {
//			this.setPickupDelayInfinite();
//		}
	}
}