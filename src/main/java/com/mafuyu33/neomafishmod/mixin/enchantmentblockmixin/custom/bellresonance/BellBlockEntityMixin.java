package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.bellresonance;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import com.mafuyu33.neomafishmod.mixinhelper.BellBlockDelayMixinHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BellBlockEntity.class)
public abstract class BellBlockEntityMixin extends BlockEntity {
    public BellBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }


    @Inject(at = @At("HEAD"), method = "onHit")
    private void init1(Direction direction, CallbackInfo ci) {
        int k = BlockEnchantmentStorage.getLevel(ModEnchantments.RESONANCE,worldPosition);
        if(k>0){
            Level world = level;
            if (world != null && !world.isClientSide) {
                // 在当前方块位置周围 K+2 格范围内搜索其他的钟
                for (BlockPos nearbyPos : BlockPos.withinManhattan(worldPosition, k + 2, k + 2, k + 2)) {
                    // 排除当前位置
                    if (nearbyPos.equals(worldPosition)) {
                        continue;
                    }
                    // 如果搜索到的方块是钟并且之前没有搜索过
                    if (world.getBlockState(nearbyPos).getBlock() instanceof BellBlock) {
                        // 激活找到的钟
                        BlockEntity nearbyBlockEntity = world.getBlockEntity(nearbyPos);
                        if (nearbyBlockEntity instanceof BellBlockEntity bellBlockEntity) {
                            if (!bellBlockEntity.shaking) {
                                BellBlockDelayMixinHelper.storeBellBlockEntity(worldPosition,bellBlockEntity);
                                BellBlockDelayMixinHelper.storeDirection(worldPosition,direction);
                                BellBlockDelayMixinHelper.storeHitCoolDown(worldPosition,0);
//                                // 将找到的钟的位置添加到Map中，准备延迟激活
//                                storeValue(nearbyPos, direction);
//                                System.out.println("将找到的钟的位置添加到Map中，准备延迟激活" + delayedActivationPositions);
                            }
                        }
                    }
                }
            }
        }
    }
}
