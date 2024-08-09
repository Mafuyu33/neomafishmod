package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.main;

import com.llamalad7.mixinextras.sugar.Local;
import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mixin(PistonBaseBlock.class)
public abstract class PistonBlockMixin {

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;relative(Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos;"
			,ordinal = 1), method = "moveBlocks")//活塞推拉方块的部分
	private void init(Level world, BlockPos pos, Direction dir
			, boolean retract, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 2) BlockPos blockPos3) {
		if (!world.isClientSide) {//只在服务端运行,获取信息
			System.out.println(blockPos3);

			Direction direction = retract ? dir : dir.getOpposite();

			if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(blockPos3), new ListTag())) {//如果原位置方块有附魔
				ListTag enchantments = BlockEnchantmentStorage.getEnchantmentsAtPosition(blockPos3); //获取附魔信息列表
				BlockEnchantmentStorage.addBlockEnchantment(blockPos3.relative(direction).immutable(), enchantments);//在新位置储存信息
			}
			if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(blockPos3), new ListTag())) {
				BlockEnchantmentStorage.removeBlockEnchantment(blockPos3.immutable());//删除信息
			}
		}
	}
//	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"
//			,ordinal = 4), method = "move",locals = LocalCapture.CAPTURE_FAILSOFT)//替换成空气的部分
//	private void init1(World world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir, BlockPos blockPos4) {//活塞推拉方块
//		if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(blockPos4), new NbtList())) {
//			BlockEnchantmentStorage.removeBlockEnchantment(blockPos4.toImmutable());//删除信息
//		}
//	}
}