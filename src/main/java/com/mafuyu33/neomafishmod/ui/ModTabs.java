package com.mafuyu33.neomafishmod.ui;

import com.mafuyu33.neomafishmod.block.ModBlock;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Consumer;

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
//
//                        // 获取附魔注册表查询接口
//                        final HolderGetter<Enchantment> enchantments = itemDisplayParameters.holders().lookupOrThrow(Registries.ENCHANTMENT);
//                        // 定义一个用于快速添加“附魔书”的 Consumer 函数式接口
//                        Consumer<ResourceKey<Enchantment>> addEnchantedBook = enchantKey -> {
//                            // 通过附魔注册表键获取对应的 Holder 容器
//                            Holder.Reference<Enchantment> holder = enchantments.getOrThrow(enchantKey);
//                            // 从 Holder 中提取具体的附魔实例对象
//                            Enchantment enchantment = holder.value();
//                            // 获取附魔的最高等级
//                            int maxLevel = enchantment.getMaxLevel();
//                            // 使用附魔描述作为书名（这里用 enchantment.description()），作者固定为 "system"，代数设为 0，
//                            // 页内容包含附魔全称（示例中使用 getString() 来获取翻译后的等级名称）
//                            WrittenBookContent content = new WrittenBookContent(
//                                    Filterable.passThrough(enchantment.description().getString()),
//                                    "system",
//                                    0,
//                                    List.of(Filterable.passThrough(Component.literal("Enchanted Book: "
//                                            + enchantment.description().getString() + " "
//                                            + Component.translatable("enchantment.level." + maxLevel).getString()))),
//                                    true
//                            );
//                            // 创建一个新的书籍物品（使用 Items.WRITTEN_BOOK）
//                            ItemStack bookStack = new ItemStack(Items.WRITTEN_BOOK);
//                            // 将书籍内容写入物品数据组件中
//                            bookStack.set(DataComponents.WRITTEN_BOOK_CONTENT, content);
//                            // 将该物品传递到输出
//                            output.accept(bookStack);
//                        };

                    //在这里添加item
                        output.accept(ModItems.MAFISH.get());
//                        output.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(ModEnchantments.ONE_STEP_TEN_LINE, 1)));
//                        output.accept((ItemLike) ModEnchantments.ONE_STEP_TEN_LINE);
                        output.accept(ModBlock.WHATE_CAT_BLOCK.get());
//                        output.accept(ModItems.RUBY_STAFF.get());

                        output.accept(ModItems.COLLIABLE.get());
                        output.accept(ModItems.TIME_STOP.get());

                        output.accept(ModItems.BREAD_SWORD_VERY_HOT.get());
                        output.accept(ModItems.BREAD_SWORD.get());
                        output.accept(ModItems.BREAD_SWORD_HOT.get());

//                        output.accept(ModItems.LIGHTNING_ITEM.get());

                        output.accept(ModItems.TNT_BALL.get());
                        output.accept(ModItems.STONE_BALL.get());

                        output.accept(ModItems.ZHU_GE.get());
                        output.accept(ModItems.POISON_SWORD.get());
                        output.accept(ModItems.LIGHTNING_BALL.get());

                        //会崩溃
//                        output.accept(ModItems.MATH_SWORD.get());

                        output.accept(ModItems.MILK_FLESH.get());
                        output.accept(ModItems.STARGAZY_PIE.get());
                        output.accept(ModItems.CHEESE_BERGER.get());

                        output.accept(ModBlock.POTATO_TNT.get());

                        output.accept(ModItems.SWITCH.get());
                        output.accept(ModItems.RUYIJINGU.get());
                        output.accept(ModItems.LawnMowerItem.get());

//                        // 添加自定义的附魔书
//                        addEnchantedBook.accept(ModEnchantments.BAD_LUCK_OF_SEA);
//                        addEnchantedBook.accept(ModEnchantments.BOW_LEFT);
//                        addEnchantedBook.accept(ModEnchantments.VERY_EASY);
//                        addEnchantedBook.accept(ModEnchantments.A_LEAF);
//                        addEnchantedBook.accept(ModEnchantments.BUTTERFLY);
//                        addEnchantedBook.accept(ModEnchantments.EIGHT_GODS_PASS_SEA);
//                        addEnchantedBook.accept(ModEnchantments.FANGSHENG);
//                        addEnchantedBook.accept(ModEnchantments.FEAR);
//                        addEnchantedBook.accept(ModEnchantments.GO_TO_SKY);
//                        addEnchantedBook.accept(ModEnchantments.GONG_XI_FA_CAI);
//                        addEnchantedBook.accept(ModEnchantments.HOT_POTATO);
//                        addEnchantedBook.accept(ModEnchantments.KILL_CHICKEN_GET_EGG);
//                        addEnchantedBook.accept(ModEnchantments.KILL_MY_HORSE);
//                        addEnchantedBook.accept(ModEnchantments.KILL_MY_HORSE_PLUS);
//                        addEnchantedBook.accept(ModEnchantments.MELEE_MAGNETISM);
//                        addEnchantedBook.accept(ModEnchantments.MERCY);
//                        addEnchantedBook.accept(ModEnchantments.MUTE);
//                        addEnchantedBook.accept(ModEnchantments.NEVER_GONNA);
//                        addEnchantedBook.accept(ModEnchantments.NEVER_STOP);
//                        addEnchantedBook.accept(ModEnchantments.NO_BLAST_PROTECTION);
//                        addEnchantedBook.accept(ModEnchantments.ONE_STEP_TEN_LINE);
//                        addEnchantedBook.accept(ModEnchantments.ONE_WITH_SHADOWS);
//                        addEnchantedBook.accept(ModEnchantments.PAY_TO_PLAY);
//                        addEnchantedBook.accept(ModEnchantments.REDIRECT_PROJECTILE);
//                        addEnchantedBook.accept(ModEnchantments.RESONANCE);
//                        addEnchantedBook.accept(ModEnchantments.REVERSE);
//                        addEnchantedBook.accept(ModEnchantments.SLIPPERY);
//                        addEnchantedBook.accept(ModEnchantments.STICKY);
//                        addEnchantedBook.accept(ModEnchantments.SUPER_PROJECTILE_PROTECTION);
                    }).build());
}
