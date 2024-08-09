package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.bowleft;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.item.BowItem.getPowerForTime;

@Mixin(BowItem.class)
public abstract class BowItemMixin  extends ProjectileWeaponItem {



	@Shadow protected abstract void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target);

	private float newYaw = 0 ;

	private float newVelocity;
    public BowItemMixin(Properties properties) {
		super(properties);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BowItem;shoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/world/entity/LivingEntity;)V"), method = "releaseUsing")
	private void init(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft, CallbackInfo ci) {
		int o = InjectHelper.getEnchantmentLevel(stack, ModEnchantments.BOW_LEFT);
		if(entityLiving instanceof Player playerEntity && o>0) {
			int i = this.getUseDuration(stack,entityLiving) - timeLeft;
			float f = getPowerForTime(i);
			float leftOffset = i * 1.0F; // 偏移角度（度）
			float newYaw = playerEntity.getYRot() - leftOffset; // 调整后的偏航角
			this.newYaw = newYaw;
			this.newVelocity = f * 3.0F;
		}
	}


	@Inject(method = "shootProjectile",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"))
	public void init1(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, LivingEntity target, CallbackInfo ci){
		projectile.shootFromRotation(shooter, shooter.getXRot(), this.newYaw, 0.0F, this.newVelocity, inaccuracy);
	}
}