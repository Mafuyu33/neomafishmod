package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.fasthopper;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BooleanSupplier;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {

    @Inject(at = @At("RETURN"), method = "tryMoveItems")
    private static void init1(Level level, BlockPos pos, BlockState state, HopperBlockEntity blockEntity, BooleanSupplier validator, CallbackInfoReturnable<Boolean> cir) {
        int k = BlockEnchantmentStorage.getLevel(Enchantments.QUICK_CHARGE,pos);//漏斗的快速装填
//		System.out.println("传递一次");
        if(k>0){
//			System.out.println("设置冷却");
            blockEntity.setCooldown(0);
        }
    }

    @Inject(at = @At("HEAD"), method = "ejectItems",cancellable = true)
    private static void init2(Level level, BlockPos pos, HopperBlockEntity blockEntity, CallbackInfoReturnable<Boolean> cir){
        int k = BlockEnchantmentStorage.getLevel(Enchantments.BINDING_CURSE,pos);//漏斗的绑定诅咒
        if(k>0){
            System.out.println("取消传递！");
            cir.cancel();
        }
    }
}
