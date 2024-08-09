package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.luoyangshovel;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {

	public FallingBlockEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
		FallingBlockEntity fallingBlockEntity = (FallingBlockEntity) (Object) this;
		AABB boundingBox = fallingBlockEntity.getBoundingBox();

		// 获取世界中的所有实体
		List<Entity> entities = fallingBlockEntity.level().getEntities(fallingBlockEntity, boundingBox);

		// 遍历这些实体并检查是否为生物实体
		for (Entity entity : entities) {
			if (entity instanceof LivingEntity livingEntity) {
				// 检查碰撞箱是否重叠
				if (boundingBox.intersects(livingEntity.getBoundingBox())) {
					// 对生物实体造成伤害
					DamageSource damageSource = damageSources().fallingBlock(fallingBlockEntity);
					livingEntity.hurt(damageSource, fallingBlockEntity.fallDistance);
				}
			}
		}
	}
}