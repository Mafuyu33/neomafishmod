package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.knockback;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TripWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TripWireBlock.class)
public abstract class TripwireBlockMixin extends Block {

    public TripwireBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(at = @At("HEAD"), method = "entityInside")
    private void init3(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        int knockbackLevel = BlockEnchantmentStorage.getLevel(Enchantments.KNOCKBACK, pos);
        if (knockbackLevel > 0) {
            Vec3 entityPos = entity.position();
            Vec3 blockPosVec = Vec3.atCenterOf(pos);

            // 计算从方块指向实体的向量
            Vec3 directionToEntity = entityPos.subtract(blockPosVec);

            // 归一化向量并乘以击退等级
            Vec3 knockbackVector = directionToEntity.normalize().scale(-knockbackLevel * 0.5);

            // 应用击退力
            if (!level.isClientSide()) {
                entity.push(knockbackVector.x, knockbackVector.y, knockbackVector.z);
            }

            if (level.isClientSide() && entity instanceof Player player) {
                player.push(knockbackVector.x, knockbackVector.y, knockbackVector.z);
            }
        }
    }
}
