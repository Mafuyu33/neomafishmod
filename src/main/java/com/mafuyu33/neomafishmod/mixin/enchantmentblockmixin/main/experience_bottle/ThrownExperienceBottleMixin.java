package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.main.experience_bottle;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Mafuyu33
 */
@Mixin(ThrownExperienceBottle.class)
public abstract class ThrownExperienceBottleMixin extends ThrowableItemProjectile {
	public ThrownExperienceBottleMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "onHit",at = @At("HEAD"),cancellable = true)
	private void init(HitResult result, CallbackInfo ci) {
		if (result instanceof BlockHitResult blockHit) {
			Level level = this.level();
			BlockPos pos = blockHit.getBlockPos();
			if (level.isClientSide()) {
				return;
			}
			ThrownExperienceBottle self = (ThrownExperienceBottle)(Object)this;
			CompoundTag entityData = self.getPersistentData();

			if (entityData.contains("block_enchantments")) {
				ListTag enchantments = entityData.getListOrEmpty("block_enchantments");
				neomafishmod$applyFakeExplosionEffect(level, pos, enchantments, 3);
			}
        }
		// 不会产生经验。
		super.onHit(result);
		if (this.level() instanceof ServerLevel) {
			this.level().levelEvent(2002, this.blockPosition(), -13083194);
			this.discard();
		}
		ci.cancel();
	}

	@Unique
	private void neomafishmod$applyFakeExplosionEffect(Level level, BlockPos center, ListTag enchantments, int radius) {
		Set<BlockPos> affected = new HashSet<>();

		for (int dx = -radius; dx <= radius; dx++) {
			for (int dy = -radius; dy <= radius; dy++) {
				for (int dz = -radius; dz <= radius; dz++) {
					double distSq = dx * dx + dy * dy + dz * dz;
					if (distSq > radius * radius) continue;

					BlockPos pos = center.offset(dx, dy, dz);
					BlockState blockState = level.getBlockState(pos);
					// 只处理非空气、非水、非岩浆的方块
					if (!blockState.is(Blocks.AIR) && !blockState.is(Blocks.WATER) && !blockState.is(Blocks.LAVA)) {
					affected.add(pos.immutable());
				}
			}
		}

		for (BlockPos pos : affected) {
			BlockEnchantmentStorage.addBlockEnchantment(pos, enchantments);
		}
	}
	}
}