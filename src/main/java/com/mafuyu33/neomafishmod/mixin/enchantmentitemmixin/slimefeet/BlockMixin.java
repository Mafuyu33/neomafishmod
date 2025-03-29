package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.slimefeet;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
	@Inject(at = @At("HEAD"), method = "fallOn", cancellable = true)
	private void init(Level p_152426_, BlockState p_152427_, BlockPos p_152428_, Entity p_152429_, double p_397222_, CallbackInfo ci) {
		if (p_152429_ instanceof LivingEntity livingEntity) {
			// 直接检查靴子装备槽位
			ItemStack bootsItem = livingEntity.getItemBySlot(EquipmentSlot.FEET);

			if (!bootsItem.isEmpty()) {
				int k = InjectHelper.getEnchantmentLevel(bootsItem, ModEnchantments.STICKY);
				if (k > 0) {
					ci.cancel();
				}
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "updateEntityMovementAfterFallOn", cancellable = true)
	private void init1(BlockGetter level, Entity entity, CallbackInfo ci) {
		if (entity instanceof LivingEntity livingEntity) {
			// 直接检查靴子装备槽位
			ItemStack bootsItem = livingEntity.getItemBySlot(EquipmentSlot.FEET);

			if (!bootsItem.isEmpty()) {
				int k = InjectHelper.getEnchantmentLevel(bootsItem, ModEnchantments.STICKY);
				if (k > 0) {
					mafishmod$bounce(entity);
					ci.cancel();
				}
			}
		}
	}

	@Unique
	private void mafishmod$bounce(Entity entity) {
		Vec3 vec3d = entity.getDeltaMovement();
		if (vec3d.y < 0.0) {
			double d = entity instanceof LivingEntity ? 1.25 : 0.8;
			entity.setDeltaMovement(vec3d.x, -vec3d.y * d, vec3d.z);
		}
	}
}