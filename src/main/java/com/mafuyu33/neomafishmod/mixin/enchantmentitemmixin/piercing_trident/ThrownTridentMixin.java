package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.piercing_trident;

import com.llamalad7.mixinextras.sugar.Local;
import com.mafuyu33.neomafishmod.Config;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.runIterationOnItem;

/**
 * @author Mafuyu33
 */
@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {
	protected ThrownTridentMixin(EntityType<? extends AbstractArrow> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	private boolean dealtDamage;

	@Inject(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ThrownTrident;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"),cancellable = true)
	private void init(EntityHitResult result, CallbackInfo ci, @Local(ordinal = 0) Entity entity , @Local(ordinal = 0) DamageSource damageSource) {
		//懒得写附魔了
		if(Config.isTridentPiercing()) {
			this.dealtDamage = false;
			this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
			ci.cancel();
		}
	}
}

//	@Inject(method = "onHitEntity",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),cancellable = true)
//	private void init(EntityHitResult result, CallbackInfo ci, @Local(ordinal = 0) Entity entity, @Local(ordinal = 0) DamageSource damagesource,@Local(ordinal = 0) float f) {
//		if (附魔了能打到小黑的方法) {
//			this.dealtDamage = true;
//			if (entity.hurt(damagesource, f)) {
//				if (entity.getType() == EntityType.ENDERMAN) {
//					return;
//				}
//				Level var7 = this.level();
//				if (var7 instanceof ServerLevel serverlevel1) {
//					EnchantmentHelper.doPostAttackEffectsWithItemSource(serverlevel1, entity, damagesource, this.getWeaponItem());
//				}
//
//				if (entity instanceof LivingEntity) {
//					LivingEntity livingentity = (LivingEntity)entity;
//					this.doKnockback(livingentity, damagesource);
//					this.doPostHurtEffects(livingentity);
//				}
//			}
//			this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
//			this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
//			ci.cancel();
//		}
//	}