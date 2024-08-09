package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.fallprotection;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(at = @At("HEAD"), method = "fallOn",cancellable = true)
    private void init(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {
        int i = BlockEnchantmentStorage.getLevel(Enchantments.FEATHER_FALLING, pos);
        if (i > 0){
            entity.causeFallDamage(fallDistance, (-0.25F * i + 1), entity.damageSources().fall());
            ci.cancel();
        }
    }
}
