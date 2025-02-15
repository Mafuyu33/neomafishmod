package com.mafuyu33.neomafishmod.enchantmentblock;

import com.mafuyu33.neomafishmod.ServerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Mafuyu33
 */
public class BlockEnchantmentStorage {
    private static final Map<String, Integer> LEVEL_CACHE = new ConcurrentHashMap<>();
    private static final Logger LOGGER = Logger.getLogger(BlockEnchantmentStorage.class.getName());


    // 添加方块附魔信息（直接操作哈希表）
    public static void addBlockEnchantment(BlockPos blockPos, ListTag enchantments) {
        MinecraftServer server = ServerManager.getServerInstance();
        BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);
        if (state != null) {
            state.blockEnchantments.put(blockPos.immutable(), enchantments);
            state.setDirty();
            // 假设1000是缓存的最大条目数
            if (LEVEL_CACHE.size() > 1000) {
                LEVEL_CACHE.clear();
            }
            // 更新缓存
            LEVEL_CACHE.put(generateCacheKey(blockPos, enchantments), calculateMaxLevel(enchantments));

        }
    }

    // 移除方块的附魔数据
    public static void removeBlockEnchantment(BlockPos blockPos) {
        MinecraftServer server = ServerManager.getServerInstance();
        BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);
        if (state != null) {
            state.blockEnchantments.remove(blockPos);
            state.setDirty();
            // 清除缓存
            LEVEL_CACHE.keySet().removeIf(key -> key.startsWith(blockPos.toString() + "-"));
        }
    }

    // 获取指定方块的附魔列表
    public static ListTag getEnchantmentsAtPosition(BlockPos blockPos) {
        MinecraftServer server = ServerManager.getServerInstance();
        BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);
        return (state != null) ? state.blockEnchantments.getOrDefault(blockPos, new ListTag()) : new ListTag();
    }

    // 获取特定附魔的等级（带缓存优化）
    public static int getLevel(ResourceKey<Enchantment> enchantment, BlockPos blockPos) {
        String cacheKey = generateCacheKey(blockPos, enchantment);
        if (LEVEL_CACHE.containsKey(cacheKey)) {
            return LEVEL_CACHE.get(cacheKey);
        }

        ListTag enchantments = getEnchantmentsAtPosition(blockPos);
        int level = findEnchantmentLevel(enchantment, enchantments);
        LEVEL_CACHE.put(cacheKey, level);
        return level;
    }
    // 获取所有附魔方块的坐标集合（直接返回键集合）
    public static Set<BlockPos> getAllEnchantedBlocks() {
        MinecraftServer server = ServerManager.getServerInstance();
        BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);
        return (state != null) ? state.blockEnchantments.keySet() : Collections.emptySet();
    }

    //-----------------------------
    //    私有辅助方法
    //-----------------------------
    private static int findEnchantmentLevel(ResourceKey<Enchantment> enchantment, ListTag enchantments) {
        for (int i = 0; i < enchantments.size(); i++) {
            CompoundTag tag = enchantments.getCompound(i);
            String enchantmentId = tag.getString("id");
            if (enchantment.location().toString().equals(enchantmentId)) {
                return tag.getInt("lvl");
            }
        }
        return 0;
    }

    private static int calculateMaxLevel(ListTag enchantments) {
        int maxLevel = 0;
        for (int i = 0; i < enchantments.size(); i++) {
            CompoundTag tag = enchantments.getCompound(i);
            String enchantmentId = tag.getString("id");
            if (enchantmentId != null && !enchantmentId.isEmpty()) {
                maxLevel = Math.max(maxLevel, tag.getInt("lvl"));
            }
        }
        return maxLevel;
    }

    private static String generateCacheKey(BlockPos blockPos, ResourceKey<Enchantment> enchantment) {
        return blockPos.toString() + "-" + enchantment.location().toString();
    }

    private static String generateCacheKey(BlockPos blockPos, ListTag enchantments) {
        return blockPos.toString() + "-" + enchantments.hashCode();
    }


}
