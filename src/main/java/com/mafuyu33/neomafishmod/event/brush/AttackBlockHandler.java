package com.mafuyu33.neomafishmod.event.brush;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class AttackBlockHandler {
    static BlockPos startPos;
    @SubscribeEvent
    public static void attackBlock(PlayerInteractEvent.LeftClickBlock event){
        Level level = event.getLevel();
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        interact(player,level,pos);
    }

    public static void interact(Player player, Level world, BlockPos pos) {
        if(!world.isClientSide) {
            Iterable<ItemStack> handItemStacks = player.getHandSlots();
            for (ItemStack itemstack : handItemStacks) {
                if (itemstack.is(Items.BRUSH)) {
                    //有附魔
                    if (itemstack.isEnchanted()) {
                        if (startPos == null) {
                            startPos = pos;
                        } else {
                            brushAllBlocks(world, pos, itemstack);
                            startPos = null;
                        }
                    } else {//没附魔，清除附魔方块
                        if (startPos == null) {
                            startPos = pos;
                        } else {
                            clearAllBlocks(world, pos);
                            startPos = null;
                        }
                    }
                }
            }
        }
    }

    private static void brushAllBlocks(Level world, BlockPos pos, ItemStack itemStack) {
        // 获取立方体对角方块的坐标
        int minX = Math.min(startPos.getX(), pos.getX());
        int minY = Math.min(startPos.getY(), pos.getY());
        int minZ = Math.min(startPos.getZ(), pos.getZ());
        int maxX = Math.max(startPos.getX(), pos.getX());
        int maxY = Math.max(startPos.getY(), pos.getY());
        int maxZ = Math.max(startPos.getZ(), pos.getZ());

        // 遍历立方体内的所有方块
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    BlockState blockState = world.getBlockState(currentPos);

                    // 排除空气、水、岩浆等特定方块
                    if (blockState.is(Blocks.AIR) ||
                            blockState.is(Blocks.WATER) ||
                            blockState.is(Blocks.LAVA)) {
                        continue;
                    }

                    // 在这里对满足条件的方块进行处理
                    ListTag enchantments = InjectHelper.enchantmentsToNbtList(itemStack);
                    BlockEnchantmentStorage.addBlockEnchantment(currentPos, enchantments);
                    NeoMafishMod.LOGGER.info("Found block: " + blockState.getBlock() + " at " + currentPos);
                }
            }
        }
    }
    private static void clearAllBlocks(Level world, BlockPos pos) {
        // 获取立方体对角方块的坐标
        int minX = Math.min(startPos.getX(), pos.getX());
        int minY = Math.min(startPos.getY(), pos.getY());
        int minZ = Math.min(startPos.getZ(), pos.getZ());
        int maxX = Math.max(startPos.getX(), pos.getX());
        int maxY = Math.max(startPos.getY(), pos.getY());
        int maxZ = Math.max(startPos.getZ(), pos.getZ());

        // 遍历立方体内的所有方块
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    BlockState blockState = world.getBlockState(currentPos);

                    // 在这里对满足条件的方块进行处理
                    BlockEnchantmentStorage.removeBlockEnchantment(currentPos);
                    NeoMafishMod.LOGGER.info("Found block: " + blockState.getBlock() + " at " + currentPos);
                }
            }
        }
    }
}
