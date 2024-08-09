package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.main.sigleinject;

import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin {
    @Inject(at = @At("HEAD"), method = "setPlacedBy")//存储方块的附魔
    private void init1(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack, CallbackInfo ci) {
        InjectHelper.onPlacedInject(level,stack,pos);
    }
}
