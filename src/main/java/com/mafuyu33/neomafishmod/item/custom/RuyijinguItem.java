package com.mafuyu33.neomafishmod.item.custom;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import com.mafuyu33.neomafishmod.item.ModFoods;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class RuyijinguItem extends SwordItem {

    public RuyijinguItem() {
        super(Tiers.NETHERITE, new Properties());
    }
    //物品对方块使用
    @Override
    public InteractionResult useOn(UseOnContext context) {
        //返回对应方块的附魔
        ListTag listTag = BlockEnchantmentStorage.getEnchantmentsAtPosition(context.getClickedPos());
        context.getPlayer().sendSystemMessage(Component.literal(listTag.toString()));
        return InteractionResult.SUCCESS;
    }
}
