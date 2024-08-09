package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.waterbroken;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowingFluid.class)
public abstract class FlowableFluidMixin {
	@Inject(at = @At("HEAD"), method = "canSpreadTo", cancellable = true)
	private void init(BlockGetter level, BlockPos fromPos, BlockState fromBlockState, Direction direction, BlockPos toPos, BlockState toBlockState, FluidState toFluidState, Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
		if(BlockEnchantmentStorage.getLevel(Enchantments.AQUA_AFFINITY,toPos)>0){
			System.out.println("canFlow");
			cir.setReturnValue(true);
		}
	}
	@Inject(at = @At("HEAD"), method = "canPassThroughWall", cancellable = true)
	private void init1(Direction direction, BlockGetter level, BlockPos pos, BlockState state, BlockPos spreadPos, BlockState spreadState, CallbackInfoReturnable<Boolean> cir) {
		if(BlockEnchantmentStorage.getLevel(Enchantments.AQUA_AFFINITY,pos)>0){
			System.out.println("receivesFlow");
			// 获取当前方块的世界对象，必须确保world是World类型
			if (level instanceof Level mutableWorld) {
				// 破坏方块
				mutableWorld.destroyBlock(pos, true);
			}
			cir.setReturnValue(true);
		}
	}
}