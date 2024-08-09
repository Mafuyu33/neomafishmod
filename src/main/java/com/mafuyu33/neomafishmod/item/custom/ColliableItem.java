package com.mafuyu33.neomafishmod.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import org.jetbrains.annotations.NotNull;

public class ColliableItem extends Item {

    private static boolean colliable = false;

    public ColliableItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, @NotNull InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (!world.isClientSide()) {
            colliable = !colliable;
            if(user instanceof ServerPlayer player) {
                player.sendSystemMessage(Component.literal("已切换碰撞模式"),true);
            }
        }
        return InteractionResultHolder.success(user.getItemInHand(hand));
    }

    public static boolean isColliable(){
        return colliable;
    }
}

