package com.mafuyu33.neomafishmod.enchantment;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import org.apache.commons.lang3.mutable.MutableFloat;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 辅助类，用于处理与附魔相关的逻辑。
 */
public class ModEnchantmentHelper {

    /**
     * 检查给定的物品堆栈的附魔等级。
     *
     * @param enchantments 附魔的类型
     * @param stack 要检查的物品堆栈。
     * @return 如果物品具有Adam附魔，则返回true，否则返回false。
     */
    public static int getEnchantmentLevel(ResourceKey<Enchantment> enchantments, ItemStack stack) {
        // 从物品堆栈中获取附魔信息，如果没有则使用空的附魔集合。
        ItemEnchantments itemenchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        // 获得物品的所有附魔
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemenchantments.entrySet()) {
            @Nullable ResourceKey<Enchantment> enchantmentKey = entry.getKey().getKey();
            // key是附魔，value是附魔的等级
            if (enchantmentKey != null && enchantmentKey.equals(enchantments)) {
                return entry.getIntValue();
            }
        }

        //没有附魔则返回0。
        return 0;
    }
}