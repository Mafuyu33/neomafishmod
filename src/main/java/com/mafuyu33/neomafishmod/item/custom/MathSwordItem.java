package com.mafuyu33.neomafishmod.item.custom;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MathSwordItem extends Item {
    private static boolean mathMode = false;
    private static int level = 0;

    public MathSwordItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);

        level = InjectHelper.getEnchantmentLevel(itemStack, ModEnchantments.VERY_EASY);

        if (!world.isClientSide()) {
            mathMode = !mathMode;
        }

        if (mathMode) {
            user.displayClientMessage(Component.literal("数学领域展开"), true);
        } else {
            user.displayClientMessage(Component.literal("数学领域关闭"), true);
        }

        return world.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
    }

    public static boolean isMathMode() {
        return mathMode;
    }

    public static int getLevel() {
        return level;
    }
}
