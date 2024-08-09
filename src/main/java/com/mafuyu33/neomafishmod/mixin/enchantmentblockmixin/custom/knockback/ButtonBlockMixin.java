package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.knockback;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ButtonBlock.class)
public abstract class ButtonBlockMixin extends Block {

    public ButtonBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(at = @At("HEAD"), method = "entityInside")
    private void init3(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        int k = BlockEnchantmentStorage.getLevel(Enchantments.KNOCKBACK,pos);
        if (!level.isClientSide() && k > 0) {
            Vec3 velocity = new Vec3(direction.step()).scale(k * 0.5);
            entity.push(velocity.x, velocity.y, velocity.z);
        }
        if (level.isClientSide() && k > 0 && entity instanceof Player player) {//如果有击退附魔
            Vec3 velocity = new Vec3(direction.step()).scale(k * 0.5);
            player.push(velocity.x, velocity.y, velocity.z);
        }
    }
}
