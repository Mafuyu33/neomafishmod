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
import java.util.logging.Logger;

public class BlockEnchantmentStorage {
     private static final Logger LOGGER = Logger.getLogger(BlockEnchantmentStorage.class.getName());

    public static void addBlockEnchantment(BlockPos blockPos, ListTag enchantments) {
        MinecraftServer server = ServerManager.getServerInstance();
        BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);

        // Check if the blockPos already exists in the list
        for (BlockStateSaverAndLoader.BlockEnchantInfo blockEnchantment : state.blockEnchantments) {
            if (blockEnchantment.blockPos.equals(blockPos)) {
                // Update the existing enchantments
                blockEnchantment.enchantments = enchantments;
//                LOGGER.info("Updated enchantments at BlockPos: " + blockPos + " with enchantments: " + enchantments);
                state.setDirty();
                return;
            }
        }
        // If not found, add a new entry
        state.blockEnchantments.add(new BlockStateSaverAndLoader.BlockEnchantInfo(blockPos, enchantments));
//        LOGGER.info("Added enchantments at BlockPos: " + blockPos + " with enchantments: " + enchantments);
        state.setDirty();
    }

        public static void removeBlockEnchantment(BlockPos blockPos) {
            MinecraftServer server = ServerManager.getServerInstance();
            BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);
            state.removeBlockEnchantment(blockPos);
        }

        public static ListTag getEnchantmentsAtPosition(BlockPos blockPos) {
            MinecraftServer server = ServerManager.getServerInstance();
            BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);

            for (BlockStateSaverAndLoader.BlockEnchantInfo blockEnchantment : state.blockEnchantments) {
                if (blockEnchantment.blockPos.equals(blockPos)) {
                    return blockEnchantment.enchantments;
                }
            }
            return new ListTag();
        }

        public static int getLevel(ResourceKey<Enchantment> enchantment, BlockPos blockPos) {
            ListTag enchantments = getEnchantmentsAtPosition(blockPos);

            for (int i = 0; i < enchantments.size(); i++) {
                CompoundTag enchantmentInfo = enchantments.getCompound(i);
                String enchantmentName = enchantmentInfo.getString("id");
                int level = enchantmentInfo.getInt("lvl");

                if (enchantmentName.equals(String.valueOf(enchantment))) {
                    return level;
                }
            }
            return 0;
        }
}
