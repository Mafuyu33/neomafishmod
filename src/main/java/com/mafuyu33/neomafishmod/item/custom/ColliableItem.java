package com.mafuyu33.neomafishmod.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ColliableItem extends Item{
    public ColliableItem(Properties properties) {
        super(properties);
    }

    private static boolean colliable = false;

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!level.isClientSide){
            colliable = !colliable;
            player.displayClientMessage(Component.literal("已切换碰撞模式"),true);
        }
        return InteractionResultHolder.success(itemStack);
    }

    public static boolean isColliable(){
        return colliable;
    }

}
