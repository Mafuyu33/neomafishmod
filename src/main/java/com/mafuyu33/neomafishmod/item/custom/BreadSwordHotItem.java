package com.mafuyu33.neomafishmod.item.custom;

import com.ibm.icu.text.UnicodeSet;
import com.mafuyu33.neomafishmod.item.ModFoods;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;

public class BreadSwordHotItem extends SwordItem {

    public BreadSwordHotItem(){
        super(Tiers.STONE,new Properties().food(ModFoods.BREAD_SWORD_HOT));
    }
}
