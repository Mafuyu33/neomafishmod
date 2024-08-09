package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.badluckofsea;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(FlowingFluid.class)
public abstract class FlowableFluidMixin {

    @Inject(at = @At("HEAD"), method = "canPassThroughWall", cancellable = true)
    private void init1(Direction face, BlockGetter world, BlockPos pos, BlockState state, BlockPos fromPos, BlockState fromState, CallbackInfoReturnable<Boolean> cir) {
        int k = BlockEnchantmentStorage.getLevel(ModEnchantments.BAD_LUCK_OF_SEA,pos);
        if(k>0){
            System.out.println("receivesFlow");
            // 获取当前方块的世界对象，必须确保world是World类型
            if (world instanceof Level mutableWorld) {
                // 破坏方块
                mafishmod$generateFallingBlock(pos,state,mutableWorld);
            }
            cir.setReturnValue(true);
        }
    }



    @Unique
    private void mafishmod$generateFallingBlock(BlockPos targetPos , BlockState blockState, Level world) {
        if(!world.isClientSide()) {
            BlockEntity blockEntity = world.getBlockEntity(targetPos);

            if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(targetPos), new ListTag())) {
                BlockEnchantmentStorage.removeBlockEnchantment(targetPos.immutable());//删除信息
            }

            FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(EntityType.FALLING_BLOCK, world);

            fallingBlockEntity.blockState = blockState;
            fallingBlockEntity.time = 1;
            fallingBlockEntity.setNoGravity(false);
            fallingBlockEntity.blocksBuilding = true;
            fallingBlockEntity.setPos(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
            fallingBlockEntity.setDeltaMovement(0, 0.2, 0);
            fallingBlockEntity.xOld = targetPos.getX() + 0.5;
            fallingBlockEntity.yOld = targetPos.getY();
            fallingBlockEntity.zOld = targetPos.getZ() + 0.5;
            fallingBlockEntity.setStartPos(targetPos);
            //设置附魔
            //设置伤害
            fallingBlockEntity.setHurtsEntities(0, -1);

            // 如果方块有附加的 BlockEntity 数据，可以设置 blockEntityData 字段
            if (blockEntity != null) {
                CompoundTag blockEntityData = new CompoundTag();
                ((EntityBlockAccessor) blockEntity).saveAdditional(blockEntityData,world.registryAccess());
                fallingBlockEntity.blockData = blockEntityData;
            }

            world.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);

            world.addFreshEntity(fallingBlockEntity);
        }
    }

}
