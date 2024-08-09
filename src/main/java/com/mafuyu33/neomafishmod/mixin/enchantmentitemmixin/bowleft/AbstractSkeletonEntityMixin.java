package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.bowleft;

import com.llamalad7.mixinextras.sugar.Local;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeleton.class)
public abstract class AbstractSkeletonEntityMixin extends Monster implements RangedAttackMob {
	protected AbstractSkeletonEntityMixin(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"), method = "performRangedAttack")
	private void init(LivingEntity target, float pullProgress, CallbackInfo ci, @Local(ordinal = 0) double d , @Local(ordinal = 1) double e
			, @Local(ordinal = 2) double f , @Local(ordinal = 3) double g, @Local(ordinal = 0) AbstractArrow persistentProjectileEntity) {
		ItemStack bow = this.getItemInHand(this.getUsedItemHand());
		int i = InjectHelper.getEnchantmentLevel(bow, ModEnchantments.BOW_LEFT);
		System.out.println(i);
		if(i>0) {
			// 左偏移的量，可以调整这个值来改变偏移的程度
			double offset = -1.0; // 偏移量，正数表示左偏移，负数表示右偏移
			// 计算偏移方向
			double offsetX = -f * offset / g;
			double offsetZ = d * offset / g;
			// 修改速度向量
			persistentProjectileEntity.shoot(d + offsetX, e + g * 0.20000000298023224, f + offsetZ, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
		}
	}
}