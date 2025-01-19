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
			renderEnchantedBlockParticles();
		}
	}


	private void renderEnchantedBlockParticles() {
		// 从存储中获取所有附魔方块
		Set<BlockPos> enchantedBlocks = BlockEnchantmentStorage.getAllEnchantedBlocks();

		// 为每个附魔方块生成粒子效果
		for (BlockPos blockPos : enchantedBlocks) {
			// 检查方块是否仍然存在（可能已被破坏）
			if (this.level().getBlockState(blockPos).isAir()) {
				// 从存储中移除不存在的方块
				BlockEnchantmentStorage.removeBlockEnchantment(blockPos);
			} else {
				addParticles(blockPos);
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