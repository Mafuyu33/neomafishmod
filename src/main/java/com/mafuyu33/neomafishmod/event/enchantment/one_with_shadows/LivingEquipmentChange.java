package com.mafuyu33.neomafishmod.event.enchantment.one_with_shadows;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantmentHelper;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = NeoMafishMod.MODID)
public class LivingEquipmentChange {
    // 用于存储生物的默认 step_height 值
    private static final Map<LivingEntity, Double> defaultStepHeightMap = new HashMap<>();
    public  static boolean intoShadows = false;
    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        LivingEntity entity = event.getEntity();

        EquipmentSlot slot = event.getSlot();
        ItemStack from = event.getFrom();
        ItemStack to = event.getTo();

        // 卸下装备时重置
        if (from.isEnchanted() && ModEnchantmentHelper.getEnchantmentLevel(ModEnchantments.ONE_WITH_SHADOWS, from) > 0) {
            intoShadows = false;
        }
        // 装上装备时启动
        if (to.isEnchanted() && ModEnchantmentHelper.getEnchantmentLevel(ModEnchantments.ONE_WITH_SHADOWS, from) > 0) {
            intoShadows = true;
        }
    }
}
