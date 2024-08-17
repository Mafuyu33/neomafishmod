package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.elytra;

import com.llamalad7.mixinextras.sugar.Local;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantmentHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}
	@Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot1);

	@Shadow private SoundEvent getFallDamageSound(int height) {
        return null;
    }

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",ordinal = 6), method = "travel", cancellable = true)
	private void init1(CallbackInfo ci, @Local (ordinal = 1)Vec3 vec3 ,@Local (ordinal = 3) double d3) {
		ItemStack itemstack = this.getItemBySlot(EquipmentSlot.CHEST);
		if(ModEnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY,itemstack)>0) {//鞘翅有无限附魔的时候。
			this.setDeltaMovement(vec3);
			ci.cancel();
			this.move(MoverType.SELF, this.getDeltaMovement());
			if (this.horizontalCollision && !this.level().isClientSide) {
				double d11 = this.getDeltaMovement().horizontalDistance();
				double d7 = d3 - d11;
				float f1 = (float) (d7 * 10.0 - 3.0);
				if (f1 > 0.0F) {
					this.playSound(this.getFallDamageSound((int) f1), 1.0F, 1.0F);
					this.hurt(this.damageSources().flyIntoWall(), f1);
				}
			}

			if (this.onGround() && !this.level().isClientSide) {
				this.setSharedFlag(7, false);
			}
		}
	}
}