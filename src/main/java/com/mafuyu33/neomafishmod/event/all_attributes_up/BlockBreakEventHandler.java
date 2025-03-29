package com.mafuyu33.neomafishmod.event;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.effect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import static java.lang.Math.min;

/**
 * @author Mafuyu33
 */
@EventBusSubscriber(modid = NeoMafishMod.MODID)
public class BlockBreakEventHandler {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Level level = (Level) event.getLevel();

        // 检查玩家是否有全属性提升效果
        MobEffectInstance effect = player.getEffect(ModEffects.ALL_ATTRIBUTES_UP_EFFECT);
        if (effect != null && !level.isClientSide) {
            int amplifier = effect.getAmplifier();

            // 获取破坏范围 (基于效果等级)
            int range = min(10, 1 + amplifier);
            // 限制最大范围为10格

            // 获取原始破坏的方块位置
            BlockPos centerPos = event.getPos();

            // 获取玩家视线方向以确定破坏区域的朝向
            net.minecraft.world.phys.Vec3 lookVec = player.getLookAngle();
            int mainDirX = Math.abs(lookVec.x) > 0.5 ? (lookVec.x > 0 ? 1 : -1) : 0;
            int mainDirY = Math.abs(lookVec.y) > 0.5 ? (lookVec.y > 0 ? 1 : -1) : 0;
            int mainDirZ = Math.abs(lookVec.z) > 0.5 ? (lookVec.z > 0 ? 1 : -1) : 0;

            // 获取被破坏的方块的状态，用于后续判断相同类型
            BlockState brokenBlockState = event.getState();

            // 破坏周围方块
            for (int x = -range; x <= range; x++) {
                for (int y = -range; y <= range; y++) {
                    for (int z = -range; z <= range; z++) {
                        // 防止破坏太多方块 (使用曼哈顿距离)
                        if (Math.abs(x) + Math.abs(y) + Math.abs(z) > range + 1) continue;

                        // 跳过原始破坏的方块
                        if (x == 0 && y == 0 && z == 0) continue;

                        // 计算目标位置 (偏向玩家视线方向)
                        BlockPos targetPos = centerPos.offset(
                                x + (mainDirX * (range > 1 ? 1 : 0)),
                                y + (mainDirY * (range > 1 ? 1 : 0)),
                                z + (mainDirZ * (range > 1 ? 1 : 0))
                        );

                        BlockState targetState = level.getBlockState(targetPos);

                        // 只破坏与原始方块相同类型的方块
                        if (targetState.getBlock() == brokenBlockState.getBlock() &&
                                !targetState.isAir() &&
                                targetState.getDestroySpeed(level, targetPos) >= 0 &&
                                targetState.getDestroySpeed(level, targetPos) < 50) {

                            // 无论是否有工具，都破坏方块
                            // 如果玩家已经能够破坏原始方块，那么也应该能够破坏同类型的周围方块
                            level.destroyBlock(targetPos, true, player);
                        }
                    }
                }
            }
        }
    }
}