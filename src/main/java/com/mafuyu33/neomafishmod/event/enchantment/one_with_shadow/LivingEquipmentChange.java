package com.mafuyu33.neomafishmod.event.enchantment.one_with_shadow;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantmentHelper;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.network.packet.S2C.OneWithShadowS2CPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = NeoMafishMod.MODID)
public class LivingEquipmentChange {
    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        LivingEntity entity = event.getEntity();
        EquipmentSlot slot = event.getSlot();
        ItemStack from = event.getFrom();
        ItemStack to = event.getTo();
        // 卸下装备时重置
        if (from.isEnchanted() && ModEnchantmentHelper.getEnchantmentLevel(ModEnchantments.ONE_WITH_SHADOWS, from) > 0) {
            PacketDistributor.sendToAllPlayers(new OneWithShadowS2CPacket(entity.getId(),-1));
        }
    }

}
