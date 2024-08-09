package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.main.sigleinject;

import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SmallDripleafBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmallDripleafBlock.class)
public abstract class SmallDripleafBlockMixin {
    @Inject(at = @At("HEAD"), method = "setPlacedBy")//存储方块的附魔
    private void init1(Level p_154599_, BlockPos p_154600_, BlockState p_154601_, LivingEntity p_154602_, ItemStack p_154603_, CallbackInfo ci) {
        InjectHelper.onPlacedInject(p_154599_,p_154603_,p_154600_);
    }
}
