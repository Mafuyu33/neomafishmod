package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.main;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Level.class)
public abstract class WorldMixin {
	@Shadow public abstract boolean isClientSide();

	@Inject(at = @At("HEAD"), method = "destroyBlock")
	private void init(BlockPos pos, boolean dropBlock, Entity entity, int recursionLeft, CallbackInfoReturnable<Boolean> cir) {
		if (!this.isClientSide()) {
			if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(pos), new ListTag())) {
				BlockEnchantmentStorage.removeBlockEnchantment(pos.immutable());//删除信息
			}
		}
	}
}