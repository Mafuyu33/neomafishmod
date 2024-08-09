package com.mafuyu33.neomafishmod.event;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ExplosionEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber
public class ExplosionHandler {
    public static final Map<BlockPos, BlockState> protectedBlockStates = new HashMap<>();

    @SubscribeEvent
    public static void onExplosionEventPre(ExplosionEvent.Start event){
        Explosion explosion = event.getExplosion();
        Level level = event.getLevel();
        onExplosionPre(level,explosion);
    }

    @SubscribeEvent
    public static void onExplosionEventPost(ExplosionEvent.Detonate event){
        Explosion explosion = event.getExplosion();
        List<Entity> affectedEntities = event.getAffectedEntities();
        Level level = event.getLevel();
    }

    // 爆炸发生前的处理
    private static void onExplosionPre(Level world, Explosion explosion) {
        // 调用collectBlocksAndDamageEntities方法填充受影响方块列表
        explosion.explode();
        // 获取即将受到影响的方块坐标
        List<BlockPos> affectedBlocks = explosion.getToBlow();
        // 遍历受影响的方块
        for (BlockPos pos : affectedBlocks) {
            // 获取方块的附魔信息
            int blastProtectionLevel = BlockEnchantmentStorage.getLevel(Enchantments.BLAST_PROTECTION,pos);
            // 检查是否存在爆炸保护附魔
            if (blastProtectionLevel > 0) {
                // 存在爆炸保护附魔，阻止该方块被摧毁
                BlockState blockState = world.getBlockState(pos);
                System.out.println(blockState);
                protectedBlockStates.put(pos, blockState);
            }
        }
    }


    // 爆炸发生后的处理
    private static void onExplosionDetonate(Level world, Explosion explosion, List<Entity> entities) {
        // 在这里添加你的逻辑，处理爆炸发生后的事件
        // 还原受到爆炸保护的方块状态
        for (Map.Entry<BlockPos, BlockState> entry : protectedBlockStates.entrySet()) {
            System.out.println("还原受到爆炸保护的方块状态");
            BlockPos pos = entry.getKey();
            BlockState blockState = entry.getValue();
            world.setBlock(pos, blockState, Block.UPDATE_ALL);
        }

        // 清除保存的受保护方块的状态，以便下一次爆炸事件
        protectedBlockStates.clear();
    }
}
