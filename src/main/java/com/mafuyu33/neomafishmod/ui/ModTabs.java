package com.mafuyu33.neomafishmod.ui;

import com.mafuyu33.neomafishmod.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
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




                    }).build());
}
