package com.mafuyu33.neomafishmod.item;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.mafuyu33.neomafishmod.item.custom.*;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NeoMafishMod.MODID);

    public static final DeferredItem<Item> MAFISH = ITEMS.register("mafish",()->
            new Item(new Item.Properties().fireResistant().food(ModFoods.Mafish)));



    public static final DeferredItem<Item> RUBY = registerItem("ruby",()-> new Item(new Item.Properties().fireResistant()));
    public static final DeferredItem<Item> RAW_RUBY=registerItem("raw_ruby",()-> new Item(new Item.Properties().fireResistant()));

    public static final DeferredItem<Item> BREAD_SWORD = registerItem("bread_sword", BreadSwordItem::new);
    public static final DeferredItem<Item> BREAD_SWORD_HOT = registerItem("bread_sword_hot", BreadSwordHotItem::new);
    public static final DeferredItem<Item> BREAD_SWORD_VERY_HOT = registerItem("bread_sword_very_hot", BreadSwordVeryHotItem::new);
    public static final DeferredItem<Item>  TNT_BALL = registerItem("tnt_ball", ()-> new TNTBallItem(new Item.Properties()));
    public static final DeferredItem<Item>  STONE_BALL = registerItem("stone_ball", ()-> new StoneBallItem(new Item.Properties()));
    public static final DeferredItem<Item> ZHU_GE = registerItem("zhuge",()-> new ZhuGeItem(new Item.Properties().stacksTo(1).durability(425)));
    public static final DeferredItem<Item> POISON_SWORD = registerItem("poison_sword",
            ()->  new PoisonSwordItem(Tiers.DIAMOND, new Item.Properties().food(ModFoods.POISON_SWORD)));
    public static final DeferredItem<Item> LIGHTNING_BALL = registerItem("lightning_ball",
            ()-> new LightningBallItem(new Item.Properties()));
    public static final DeferredItem<Item> LIGHTNING_ITEM = registerItem("lightning_item",
            ()-> new LightningItem(Tiers.NETHERITE,new Item.Properties().fireResistant().stacksTo(1)));
    public static final DeferredItem<Item> CHEESE_BERGER = registerItem("cheese_berger",
            ()-> new CheeseBergerItem(new Item.Properties().food(ModFoods.CHEESE_BERGER)));
    public static final DeferredItem<Item> COLLIABLE = registerItem("colliable",
            ()-> new ColliableItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> MATH_SWORD = registerItem("math_sword",
            ()-> new MathSwordItem(Tiers.NETHERITE, new Item.Properties()));
    public static final DeferredItem<Item> VILLAGER_ITEM = registerItem("villager_item",() -> new VillagerItem(new Item.Properties().food(ModFoods.VILLAGER_ITEM).stacksTo(1)));
    public static final DeferredItem<Item> LLAMA_ITEM = registerItem("llama_item", ()-> new LlamaItem(new Item.Properties()));
    public static final DeferredItem<Item> IRON_GOLEM_ITEM = registerItem("iron_golem_item",()-> new Item(new Item.Properties().food(ModFoods.IRON_GOLEM_ITEM).stacksTo(1)));
    public static final DeferredItem<Item> RUBY_STAFF=registerItem("ruby_staff",
            ()-> new RubyStuffItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SWITCH = registerItem("switch",() -> new SwitchItem(new Item.Properties().food(ModFoods.SWITCH)));
    public static final DeferredItem<Item> RTX4090 = registerItem("rtx4090", ()-> new RTX4090Item(Tiers.NETHERITE,new Item.Properties()));
    public static final DeferredItem<Item> MILK_FLESH = registerItem("milk_flesh",()->new MilkFleshItem(new Item.Properties().food(ModFoods.MILK_FLESH)));
    public static final DeferredItem<Item> STARGAZY_PIE = registerItem("stargazy_pie",
            ()-> new StargazyPieItem(new Item.Properties().food(ModFoods.STARGAZY_PIE)));
    public static final DeferredItem<Item> TIME_STOP = registerItem("time_stop",
            ()-> new TimeStopItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FU = registerItem("fu",FuItem::new);
    public static final DeferredItem<Item> RUYIJINGU = registerItem("ruyijingu", RuyijinguItem::new);
    public static final DeferredItem<Item> LawnMowerItem = registerItem("lawn_mower", LawnMowerItem::new);

    public static DeferredItem<Item> registerItem(String name, Supplier<Item> itemSupplier){
        return ITEMS.register(name,itemSupplier);
    }

}
