package com.mafuyu33.neomafishmod.enchantmentblock;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mafuyu33
 */
public class BlockStateSaverAndLoader extends SavedData {
    public final ConcurrentHashMap<BlockPos, ListTag> blockEnchantments = new ConcurrentHashMap<>();

    // 创建一个简单的自定义Codec来处理ListTag
    private static final Codec<ListTag> LIST_TAG_CODEC = CompoundTag.CODEC.xmap(
            // 移除类型参数或使用正确的类型
            compoundTag -> compoundTag.getList("Enchantments").get(),
            listTag -> {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.put("Enchantments", (Tag)listTag);  // 显式转换为Tag类型
                return compoundTag;
            }
    );

    // 创建一个Map的编解码器
    private static final Codec<Map<BlockPos, ListTag>> BLOCK_ENCHANTMENTS_CODEC = Codec.unboundedMap(
            BlockPos.CODEC,
            LIST_TAG_CODEC
    );

    // 创建类的编解码器
    private static final Codec<BlockStateSaverAndLoader> CODEC = BLOCK_ENCHANTMENTS_CODEC.xmap(
            map -> {
                BlockStateSaverAndLoader data = new BlockStateSaverAndLoader();
                data.blockEnchantments.putAll(map);
                return data;
            },
            data -> new HashMap<>(data.blockEnchantments)
    );

    // 创建SavedDataType实例
    public static final SavedDataType<BlockStateSaverAndLoader> TYPE = new SavedDataType<>(
            NeoMafishMod.MODID + "_block_enchantments",
            () -> new BlockStateSaverAndLoader(),
            CODEC,
            DataFixTypes.LEVEL
    );

    public void removeBlockEnchantment(BlockPos targetBlockPos) {
        blockEnchantments.remove(targetBlockPos);
        this.setDirty();
    }

    public static BlockStateSaverAndLoader getServerState(MinecraftServer server) {
        if (server != null) {
            DimensionDataStorage storage = server.getLevel(Level.OVERWORLD).getDataStorage();

            // 使用computeIfAbsent方法获取或创建数据
            return storage.computeIfAbsent(TYPE);
        }
        return null;
    }
}