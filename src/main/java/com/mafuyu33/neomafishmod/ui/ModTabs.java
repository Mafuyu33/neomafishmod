package com.mafuyu33.neomafishmod.ui;

import com.mafuyu33.neomafishmod.block.ModBlock;

import com.mafuyu33.neomafishmod.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.mafuyu33.neomafishmod.NeoMafishMod.MODID;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab,CreativeModeTab> NEO_MAFISHMOD =
            CREATIVE_TABS.register("neo_mafishmod", ()-> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.neomafishmod"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(()-> ModItems.MAFISH.get().getDefaultInstance())
                    .displayItems((itemDisplayParameters, output) -> {
                    //在这里添加item
                        output.accept(ModItems.MAFISH.get());
//                        output.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(ModEnchantments.ONE_STEP_TEN_LINE, 1)));
//                        output.accept((ItemLike) ModEnchantments.ONE_STEP_TEN_LINE);

                        output.accept(ModItems.RUBY.get());
                        output.accept(ModItems.BREAD_SWORD_VERY_HOT.get());
                        output.accept(ModItems.BREAD_SWORD.get());
                        output.accept(ModItems.BREAD_SWORD_HOT.get());
                        output.accept(ModItems.TNT_BALL.get());
                        output.accept(ModItems.STONE_BALL.get());
                        output.accept(ModItems.FU.get());
                        output.accept(ModItems.ZHU_GE.get());
                        output.accept(ModItems.POISON_SWORD.get());
                        output.accept(ModItems.LIGHTNING_BALL.get());
                        output.accept(ModItems.LIGHTNING_ITEM.get());
                        output.accept(ModItems.CHEESE_BERGER.get());
                        output.accept(ModItems.COLLIABLE.get());
                        output.accept(ModItems.MATH_SWORD.get());
                        output.accept(ModItems.VILLAGER_ITEM.get());
                        output.accept(ModItems.IRON_GOLEM_ITEM.get());
                        output.accept(ModItems.LLAMA_ITEM.get());
                        output.accept(ModItems.RUBY_STAFF.get());
                        output.accept(ModItems.RAW_RUBY.get());
                        output.accept(ModItems.SWITCH.get());
                        output.accept(ModItems.RTX4090.get());
                        output.accept(ModItems.TIME_STOP.get());
                        output.accept(ModItems.MILK_FLESH.get());
                        output.accept(ModItems.STARGAZY_PIE.get());
                        // BLOCK
                        output.accept(ModBlock.GOLD_MELON.asItem());
                        output.accept(ModBlock.POTATO_TNT.get());
                        output.accept(ModBlock.POTATO_TNT_PREPARE.get());
                        output.accept(ModBlock.WHATE_CAT_BLOCK.get());



                    }).build());
}
