package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.punchtrapdoor;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TrapDoorBlock.class)
public abstract class TrapdoorBlockMixin {
    @Inject(at = @At("HEAD"), method = "playSound")
    private void init(Player player, Level level, BlockPos pos, boolean isOpened, CallbackInfo ci) {
        BlockState state = level.getBlockState(pos);
        Direction facing = state.getValue(TrapDoorBlock.FACING);
        Vec3 directionVector  = mafishmod$get45DegreeVector(facing).normalize();
        List<Entity> entities =  getEntitiesOnBlockPos(level,pos);

        int k = BlockEnchantmentStorage.getLevel(Enchantments.PUNCH,pos);
        System.out.println(entities);
        if (k > 0 && entities!=null && isOpened) {//如果有冲击附魔,并且在活板门上，并且活板门打开
            for (Entity entity : entities) {
                entity.push(directionVector.x * k, directionVector.y * k, directionVector.z * k);
            }
        }
    }


    @Unique
    public Vec3 mafishmod$get45DegreeVector(Direction facing) {
        Vec3 directionVector;

        switch (facing) {
            case NORTH:
                directionVector = new Vec3(0, Math.sqrt(2)/2, Math.sqrt(2)/2);
                break;
            case SOUTH:
                directionVector = new Vec3(0, Math.sqrt(2)/2, -Math.sqrt(2)/2);
                break;
            case WEST:
                directionVector = new Vec3(Math.sqrt(2)/2, Math.sqrt(2)/2, 0);
                break;
            case EAST:
                directionVector = new Vec3(-Math.sqrt(2)/2, Math.sqrt(2)/2, 0);
                break;
            default:
                directionVector = Vec3.ZERO;
                break;
        }

        return directionVector;
    }

    @Unique
    private List<Entity> getEntitiesOnBlockPos(Level world, BlockPos blockPos) {
        // 定义稍微扩展的边界框以覆盖方块的表面区域
        AABB boundingBox = new AABB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1);

        // 获取边界框内的所有实体
        List<Entity> entities = world.getEntitiesOfClass(Entity.class, boundingBox, entity -> {
            // 检查实体是否站在指定的方块上
            BlockPos entityPos = entity.getOnPos();
            return entityPos.equals(blockPos);
        });

        return entities;
    }
}
