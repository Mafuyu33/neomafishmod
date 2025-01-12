package com.mafuyu33.neomafishmod.item.custom;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class LawnMowerItem extends Item {

    public LawnMowerItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
    //检测玩家范围内的草，并且破坏
        for (int x = -20; x < 20; x++) {
            for (int y = -4; y < 4; y++) {
                for (int z = -20; z < 20; z++) {
                    System.out.println("破坏了草");
                    if (context.getLevel().getBlockState(context.getPlayer().blockPosition().offset(x, y, z)).getBlock() == Blocks.SHORT_GRASS
                            || context.getLevel().getBlockState(context.getPlayer().blockPosition().offset(x, y, z)).getBlock() == Blocks.TALL_GRASS
                            || context.getLevel().getBlockState(context.getPlayer().blockPosition().offset(x, y, z)).getBlock() == Blocks.FERN
                            || context.getLevel().getBlockState(context.getPlayer().blockPosition().offset(x, y, z)).getBlock() == Blocks.LARGE_FERN) {
                        context.getLevel().destroyBlock(context.getPlayer().blockPosition().offset(x, y, z), true);
                        //输出
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
}
