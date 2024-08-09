package com.mafuyu33.neomafishmod.enchantmentblock;

import com.mafuyu33.neomafishmod.ServerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

public class BlockEnchantmentStorage {

    public static void addBlockEnchantment(BlockPos blockPos, ListTag enchantments) {
        MinecraftServer server = ServerManager.getServerInstance();
        // 创建 StateSaverAndLoader 实例
        BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);
        // 将方块附魔信息添加到列表中
        state.blockEnchantments.add(new BlockStateSaverAndLoader.BlockEnchantInfo(blockPos, enchantments));
    }


    public static void removeBlockEnchantment(BlockPos blockPos) {
        MinecraftServer server = ServerManager.getServerInstance();
        // 获取 BlockStateSaverAndLoader 实例
        BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);

        // 调用 StateSaverAndLoader 类中的方法来移除指定位置的方块附魔信息
        state.removeBlockEnchantment(blockPos);
    }




    public static ListTag getEnchantmentsAtPosition(BlockPos blockPos) {
        MinecraftServer server = ServerManager.getServerInstance();
        // 获取 BlockStateSaverAndLoader 实例
        BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);

        // 遍历附魔信息列表，找到指定位置的方块附魔信息
        for (BlockStateSaverAndLoader.BlockEnchantInfo blockEnchantment : state.blockEnchantments) {
            if (blockEnchantment.blockPos.equals(blockPos)) {
                // 返回指定位置的方块附魔名称
                return blockEnchantment.enchantments;
            }
        }
        // 如果没有找到指定位置的方块附魔信息，则返回空列表
        return new ListTag();
    }

    public static int getLevel(ResourceKey<Enchantment> enchantment, BlockPos blockPos) {
        MinecraftServer server = ServerManager.getServerInstance();
        // 获取方块的附魔信息
        ListTag enchantments = getEnchantmentsAtPosition(blockPos);

        // 遍历附魔信息
        for (int i = 0; i < enchantments.size(); i++) {
            // 获取单个附魔信息
            CompoundTag enchantmentInfo = enchantments.getCompound(i);

            // 提取附魔名称和等级
            String enchantmentName = enchantmentInfo.getString("id");
            int level = enchantmentInfo.getInt("lvl");

            // 检查附魔名称是否匹配
            if (enchantmentName.equals(String.valueOf(enchantment))) {
                // 返回附魔等级
                return level;
            }
        }

        // 如果没有找到匹配的附魔信息，默认返回0
        return 0;
    }



}
