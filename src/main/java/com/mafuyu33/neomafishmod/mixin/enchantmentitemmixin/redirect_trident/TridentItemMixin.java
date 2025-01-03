package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.redirect_trident;

import com.llamalad7.mixinextras.sugar.Local;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantmentHelper;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentItem.class)
public abstract class TridentItemMixin {
	@Inject(method = "releaseUsing",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;hasInfiniteMaterials()Z", ordinal = 0))
	private void init(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft, CallbackInfo ci, @Local ThrownTrident throwntrident) {
		//如果三叉戟上有重定向附魔
		if(ModEnchantmentHelper.getEnchantmentLevel(ModEnchantments.REDIRECT_PROJECTILE, stack) > 0) {
			throwntrident.setDeltaMovement(0,0,0);
			throwntrident.shootFromRotation(entityLiving, entityLiving.getXRot(), entityLiving.getYRot(), 0.0F, 0.03F, 1.0F);
			throwntrident.setNoGravity(true);
		}
	}
}