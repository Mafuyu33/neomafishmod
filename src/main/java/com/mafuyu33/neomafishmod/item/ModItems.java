package com.mafuyu33.neomafishmod.item;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NeoMafishMod.MODID);

    public static final DeferredItem<Item> MAFISH = ITEMS.register("mafish",()->
            new Item(new Item.Properties().stacksTo(1).fireResistant().food(ModFoods.Mafish)));
}
