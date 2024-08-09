package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.blastprotection;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EntityBasedExplosionDamageCalculator.class)
public abstract class ExplosionBehaviorMixin {
	@Inject(at = @At(value = "HEAD"), method = "shouldBlockExplode",cancellable = true)
	private void init(Explosion explosion, BlockGetter world, BlockPos pos, BlockState state, float power, CallbackInfoReturnable<Boolean> cir) {
		int i= BlockEnchantmentStorage.getLevel(Enchantments.BLAST_PROTECTION,pos);
		if(i>0){
			cir.setReturnValue(false);
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "getBlockExplosionResistance",cancellable = true)
	private void init(Explosion explosion, BlockGetter world, BlockPos pos, BlockState blockState, FluidState fluidState, CallbackInfoReturnable<Optional<Float>> cir) {
		int i=BlockEnchantmentStorage.getLevel(ModEnchantments.NO_BLAST_PROTECTION,pos);
		if(i>0){
			// 如果需要取消此方块的爆炸抗性计算，返回一个空的 Optional 对象
			cir.setReturnValue(Optional.empty());
			cir.cancel(); // 取消原始方法的执行
		}
	}
}