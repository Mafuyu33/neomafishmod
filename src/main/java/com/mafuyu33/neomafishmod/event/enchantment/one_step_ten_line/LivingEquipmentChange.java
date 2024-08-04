package com.mafuyu33.neomafishmod.event.enchantment.one_step_ten_line;

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
    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        LivingEntity entity = event.getEntity();

        EquipmentSlot slot = event.getSlot();
        ItemStack from = event.getFrom();
        ItemStack to = event.getTo();

        // 确保检测的是脚部装备槽
        if (slot == EquipmentSlot.FEET) {
            // 获取生物的 step_height 属性
            AttributeInstance stepHeightAttribute = entity.getAttribute(Attributes.STEP_HEIGHT);

            // 保存默认值
            if (!defaultStepHeightMap.containsKey(entity) && stepHeightAttribute != null) {
                defaultStepHeightMap.put(entity, stepHeightAttribute.getBaseValue());
            }

            // 卸下装备时重置 step_height 属性
            if (from.isEnchanted() && ModEnchantmentHelper.getEnchantmentLevel(ModEnchantments.ONE_STEP_TEN_LINE, from) > 0) {
                if (stepHeightAttribute != null) {
                    Double defaultStepHeight = defaultStepHeightMap.get(entity);
                    if (defaultStepHeight != null) {
                        stepHeightAttribute.setBaseValue(defaultStepHeight); // 重置为默认值
                    }
                }
            }

            // 装备新物品时设置 step_height 属性
            if (to.isEnchanted() && ModEnchantmentHelper.getEnchantmentLevel(ModEnchantments.ONE_STEP_TEN_LINE, to) > 0) {
                if (stepHeightAttribute != null) {
                    stepHeightAttribute.setBaseValue(10.0D); // 设置新的 step_height 值
                }
            }
        }
    }
}
