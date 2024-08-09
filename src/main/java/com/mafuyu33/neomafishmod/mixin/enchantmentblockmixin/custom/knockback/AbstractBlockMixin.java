package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.knockback;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.class)
public abstract class AbstractBlockMixin implements FeatureElement {
    @Inject(at = @At("HEAD"), method = "entityInside")
    private void init3(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        int k = BlockEnchantmentStorage.getLevel(Enchantments.KNOCKBACK,pos);
        if (!level.isClientSide() && k > 0) {
            entity.push(0,k*0.5,0);
        }
        if (level.isClientSide() && k > 0 && entity instanceof Player player) {//如果有击退附魔
            player.push(0,k*0.5,0);
        }
    }
}
