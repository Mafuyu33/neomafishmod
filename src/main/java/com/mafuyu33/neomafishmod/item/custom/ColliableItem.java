package com.mafuyu33.neomafishmod.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @author Mafuyu33
 */
public class ColliableItem extends Item {
    public ColliableItem(Properties properties) {
        super(properties);
    }

    private static boolean colliable = false;

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            colliable = !colliable;
            player.displayClientMessage(Component.literal("已切换碰撞模式"), true);
            // 在服务器端处理逻辑，返回服务器成功结果
            return InteractionResult.SUCCESS_SERVER;
        }
        // 在客户端返回成功结果
        return InteractionResult.SUCCESS;
    }

    public static boolean isColliable() {
        return colliable;
    }
}