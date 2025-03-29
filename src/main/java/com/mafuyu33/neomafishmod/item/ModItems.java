package com.mafuyu33.neomafishmod.item;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.mafuyu33.neomafishmod.item.custom.*;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NeoMafishMod.MODID);

    public static final DeferredItem<Item> MAFISH = ITEMS.register("mafish", () ->
            new Item(itemProps("mafish").fireResistant().food(ModFoods.Mafish)));

    public static final DeferredItem<Item> RUBY = registerItem("ruby", () -> new Item(itemProps("ruby").fireResistant()));
    public static final DeferredItem<Item> RAW_RUBY = registerItem("raw_ruby", () -> new Item(itemProps("raw_ruby").fireResistant()));

    public static final DeferredItem<Item> BREAD_SWORD = registerItem("bread_sword",
            () -> new BreadSwordItem(itemProps("bread_sword")
                    .food(ModFoods.BREAD_SWORD)
                    .sword(ToolMaterial.STONE, 3.0F, -2.4F)));
    public static final DeferredItem<Item> BREAD_SWORD_HOT = registerItem("bread_sword_hot",
            () -> new BreadSwordHotItem(itemProps("bread_sword_hot")
                    .food(ModFoods.BREAD_SWORD_HOT)
                    .sword(ToolMaterial.STONE, 3.0F, -2.4F)));
    public static final DeferredItem<Item> BREAD_SWORD_VERY_HOT = registerItem("bread_sword_very_hot",
            () -> new BreadSwordVeryHotItem(itemProps("bread_sword_very_hot")
                    .food(ModFoods.BREAD_SWORD_VERY_HOT)
                    .sword(ToolMaterial.STONE, 3.0F, -2.4F)));
    public static final DeferredItem<Item> TNT_BALL = registerItem("tnt_ball",
            () -> new TNTBallItem(itemProps("tnt_ball")));
    public static final DeferredItem<Item> STONE_BALL = registerItem("stone_ball",
            () -> new StoneBallItem(itemProps("stone_ball")));
    public static final DeferredItem<Item> ZHU_GE = registerItem("zhuge",
            () -> new ZhuGeItem(itemProps("zhuge").stacksTo(1).durability(425)));

    public static final DeferredItem<Item> POISON_SWORD = registerItem("poison_sword",
            () -> new PoisonSwordItem(itemProps("poison_sword").food(ModFoods.POISON_SWORD)));

    public static final DeferredItem<Item> LIGHTNING_BALL = registerItem("lightning_ball",
            () -> new LightningBallItem(itemProps("lightning_ball")));

    public static final DeferredItem<Item> LIGHTNING_ITEM = registerItem("lightning_item",
            () -> new LightningItem(itemProps("lightning_item").fireResistant().stacksTo(1)));

    public static final DeferredItem<Item> CHEESE_BERGER = registerItem("cheese_berger",
            () -> new CheeseBergerItem(itemProps("cheese_berger").food(ModFoods.CHEESE_BERGER)));

    public static final DeferredItem<Item> COLLIABLE = registerItem("colliable",
            () -> new ColliableItem(itemProps("colliable").stacksTo(1)));

    public static final DeferredItem<Item> MATH_SWORD = registerItem("math_sword",
            () -> new MathSwordItem(itemProps("math_sword")));

    public static final DeferredItem<Item> VILLAGER_ITEM = registerItem("villager_item", () ->
            new VillagerItem(itemProps("villager_item").food(ModFoods.VILLAGER_ITEM).stacksTo(1)));

    public static final DeferredItem<Item> LLAMA_ITEM = registerItem("llama_item",
            () -> new LlamaItem(itemProps("llama_item")));
    public static final DeferredItem<Item> IRON_GOLEM_ITEM = registerItem("iron_golem_item", () ->
            new Item(itemProps("iron_golem_item").food(ModFoods.IRON_GOLEM_ITEM).stacksTo(1)));

    public static final DeferredItem<Item> RUBY_STAFF = registerItem("ruby_staff",
            () -> new RubyStuffItem(itemProps("ruby_staff").stacksTo(1)));

    public static final DeferredItem<Item> SWITCH = registerItem("switch", () ->
            new SwitchItem(itemProps("switch").food(ModFoods.SWITCH)));

    public static final DeferredItem<Item> RTX4090 = registerItem("rtx4090", () ->
            new RTX4090Item(itemProps("rtx4090")));

    public static final DeferredItem<Item> MILK_FLESH = registerItem("milk_flesh", () ->
            new MilkFleshItem(itemProps("milk_flesh").food(ModFoods.MILK_FLESH)));

    public static final DeferredItem<Item> STARGAZY_PIE = registerItem("stargazy_pie",
            () -> new StargazyPieItem(itemProps("stargazy_pie").food(ModFoods.STARGAZY_PIE)));

    public static final DeferredItem<Item> TIME_STOP = registerItem("time_stop",
            () -> new TimeStopItem(itemProps("time_stop").stacksTo(1)));

    // 这些自定义物品可能需要额外处理，取决于它们的构造函数
//    public static final DeferredItem<Item> FU = registerItem("fu",
//            () -> new FuItem(itemProps("fu")
//                    .axe(ToolMaterial.IRON, 6.0F, -3.1F)
//                    .durability(250)));
    public static final DeferredItem<Item> RUYIJINGU = registerItem("ruyijingu",
            () -> new RuyijinguItem(itemProps("ruyijingu")));
    public static final DeferredItem<Item> LawnMowerItem = registerItem("lawn_mower",
            () -> new LawnMowerItem(itemProps("lawn_mower").stacksTo(1)));

    public static DeferredItem<Item> registerItem(String name, Supplier<Item> itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }

    public static Item.Properties itemProps(String name) {
        return new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID, name)));
    }
}