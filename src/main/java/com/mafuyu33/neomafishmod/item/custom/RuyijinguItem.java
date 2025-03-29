package com.mafuyu33.neomafishmod.item.custom;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class RuyijinguItem extends Item {

    public RuyijinguItem(Properties properties) {
        super(properties);
    }

    // 如果你需要默认构造器，可以这样使用默认的 Properties，
    // 但建议在注册时配置武器属性，例如调用 properties.sword(…)
    public RuyijinguItem() {
        super(new Properties());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        // 返回对应方块的附魔信息
        ListTag listTag = BlockEnchantmentStorage.getEnchantmentsAtPosition(context.getClickedPos());
        if (context.getPlayer() != null) {
            context.getPlayer().displayClientMessage(Component.literal(listTag.toString()), true);
        }
        return InteractionResult.SUCCESS;
    }
}
