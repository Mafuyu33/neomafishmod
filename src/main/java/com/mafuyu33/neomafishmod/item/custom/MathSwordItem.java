package com.mafuyu33.neomafishmod.item.custom;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;


public class MathSwordItem extends SwordItem {
    private static boolean mathMode = false;
    private static int level = 0;

    public MathSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    public MathSwordItem(Tier p_tier, Properties p_properties, Tool toolComponentData) {
        super(p_tier, p_properties, toolComponentData);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);

        level = InjectHelper.getEnchantmentLevel(itemStack, ModEnchantments.VERY_EASY);

        if (!world.isClientSide) {
            mathMode = !mathMode;
        }

        if(mathMode){
            user.displayClientMessage(Component.literal(("数学领域展开")),true);
        }else
        {
            user.displayClientMessage(Component.literal(("数学领域关闭")),true);
        }

        return InteractionResultHolder.success(itemStack);
    }

    public static boolean isMathMode() {
        return mathMode;
    }

    public static int getLevel() {
        return level;
    }
}