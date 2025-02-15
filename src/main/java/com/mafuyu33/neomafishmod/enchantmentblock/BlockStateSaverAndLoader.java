package com.mafuyu33.neomafishmod.enchantmentblock;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Mafuyu33
 */
public class BlockStateSaverAndLoader extends SavedData {
    public final ConcurrentHashMap<BlockPos, ListTag> blockEnchantments = new ConcurrentHashMap<>();

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag blockEnchantmentsList = new ListTag();
        blockEnchantments.forEach((pos, enchantments) -> {
            CompoundTag blockEnchantmentNbt = new CompoundTag();
            blockEnchantmentNbt.putIntArray("BlockPos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
            blockEnchantmentNbt.put("Enchantments", enchantments);
            blockEnchantmentsList.add(blockEnchantmentNbt);
        });
        tag.put("BlockEnchantments", blockEnchantmentsList);
        return tag;
    }


    public static BlockStateSaverAndLoader createFromNbt(CompoundTag nbt, HolderLookup.Provider lookup) {
        BlockStateSaverAndLoader state = new BlockStateSaverAndLoader();
        ListTag blockEnchantmentsList = nbt.getList("BlockEnchantments", 10);
        for (int i = 0; i < blockEnchantmentsList.size(); i++) {
            CompoundTag blockEnchantmentNbt = blockEnchantmentsList.getCompound(i);
            int[] posArray = blockEnchantmentNbt.getIntArray("BlockPos");
            BlockPos pos = new BlockPos(posArray[0], posArray[1], posArray[2]);
            ListTag enchantments = blockEnchantmentNbt.getList("Enchantments", 10);
            state.blockEnchantments.put(pos, enchantments);
        }
        return state;
    }

    public void removeBlockEnchantment(BlockPos targetBlockPos) {
        blockEnchantments.remove(targetBlockPos);
    }

    private static Factory<BlockStateSaverAndLoader> type = new Factory<>(
            BlockStateSaverAndLoader::new, // 若不存在 'BlockStateSaverAndLoader' 则创建
            BlockStateSaverAndLoader::createFromNbt, // 若存在 'BlockStateSaverAndLoader' NBT, 则调用 'createFromNbt' 传入参数
            null // 此处理论上应为 'DataFixTypes' 的枚举，但我们直接传递为空(null)也可以
    );

    public static BlockStateSaverAndLoader getServerState(MinecraftServer server) {
        // (注：如需在任意维度生效，请使用 'World.OVERWORLD' ，不要使用 'World.END' 或 'World.NETHER')
        if(server!=null) {
            DimensionDataStorage persistentStateManager = server.getLevel(Level.OVERWORLD).getDataStorage();

            // 当第一次调用了方法 'getOrCreate' 后，它会创建新的 'BlockStateSaverAndLoader' 并将其存储于  'PersistentStateManager' 中。
            //  'getOrCreate' 的后续调用将本地的 'BlockStateSaverAndLoader' NBT 传递给 'BlockStateSaverAndLoader::createFromNbt'。
            BlockStateSaverAndLoader state = persistentStateManager.computeIfAbsent(type, NeoMafishMod.MODID + "_block_enchantments");

            // 若状态未标记为脏(dirty)，当 Minecraft 关闭时， 'writeNbt' 不会被调用，相应地，没有数据会被保存。
            // 从技术上讲，只有在事实上发生数据变更时才应当将状态标记为脏(dirty)。
            // 但大多数开发者和模组作者会对他们的数据未能保存而感到困惑，所以不妨直接使用 'markDirty' 。
            // 另外，这只将对应的布尔值设定为 TRUE，代价是文件写入磁盘时模组的状态不会有任何改变。(这种情况非常少见)
            state.setDirty();
            return state;
        }
        return null;
    }
}
