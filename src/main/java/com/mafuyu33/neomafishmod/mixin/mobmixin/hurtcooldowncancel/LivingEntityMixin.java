package com.mafuyu33.neomafishmod.mixin.mobmixin.hurtcooldowncancel;

import com.mafuyu33.neomafishmod.Config;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Mafuyu33
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "hurtServer",at = @At("TAIL"))
	private void init(ServerLevel p_376221_, DamageSource p_376460_, float p_376610_, CallbackInfoReturnable<Boolean> cir) {
		if(Config.isHurtCoolDownCanceled()) {
			this.invulnerableTime = 0;
		}
	}
	@Inject(method = "handleDamageEvent",at = @At("TAIL"))
	private void init2(DamageSource damageSource, CallbackInfo ci) {
		if(Config.isHurtCoolDownCanceled()) {
			this.invulnerableTime = 0;
		}
	}
}