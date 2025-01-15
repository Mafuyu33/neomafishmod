package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.main;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(at = @At("TAIL"), method = "tick")
	private void init(CallbackInfo info) {
		if (this.level().isClientSide && this.isHolding(Items.BRUSH)) {
				updateEnchantedBlocks();
			// 每个tick在附魔方块位置生成粒子
			updateEnchantedBlocks();
		}
	}

	private void updateEnchantedBlocks() {
		BlockPos playerPos = this.blockPosition();

		// 遍历玩家周围 8 格范围内的方块
		for (int x = -8; x <= 8; x++) {
			for (int y = -8; y <= 8; y++) {
				for (int z = -8; z <= 8; z++) {
					BlockPos blockPos = playerPos.offset(x, y, z);

					// 检查方块是否有附魔
					if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(blockPos), new ListTag())) {
						addParticles(blockPos);
					}
				}
			}
		}
	}

	private void addParticles(BlockPos blockPos) {
		// 在方块顶部创建粒子效果
		this.level().addParticle(ParticleTypes.COMPOSTER,
				blockPos.getX() + 0.5,
				blockPos.getY() + 1.1,
				blockPos.getZ() + 0.5,
				0.0, 0.0, 0.0);

		// 在方块底部创建粒子效果
		this.level().addParticle(ParticleTypes.COMPOSTER,
				blockPos.getX() + 0.5,
				blockPos.getY() - 0.1,
				blockPos.getZ() + 0.5,
				0.0, 0.0, 0.0);

		// 在方块北侧创建粒子效果
		this.level().addParticle(ParticleTypes.COMPOSTER,
				blockPos.getX() + 0.5,
				blockPos.getY() + 0.5,
				blockPos.getZ() - 0.1,
				0.0, 0.0, 0.0);

		// 在方块南侧创建粒子效果
		this.level().addParticle(ParticleTypes.COMPOSTER,
				blockPos.getX() + 0.5,
				blockPos.getY() + 0.5,
				blockPos.getZ() + 1.1,
				0.0, 0.0, 0.0);

		// 在方块西侧创建粒子效果
		this.level().addParticle(ParticleTypes.COMPOSTER,
				blockPos.getX() - 0.1,
				blockPos.getY() + 0.5,
				blockPos.getZ() + 0.5,
				0.0, 0.0, 0.0);

		// 在方块东侧创建粒子效果
		this.level().addParticle(ParticleTypes.COMPOSTER,
				blockPos.getX() + 1.1,
				blockPos.getY() + 0.5,
				blockPos.getZ() + 0.5,
				0.0, 0.0, 0.0);
	}
}