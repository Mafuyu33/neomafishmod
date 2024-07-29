package com.mafuyu33.neomafishmod;

import com.mafuyu33.neomafishmod.item.ModItems;
import com.mafuyu33.neomafishmod.ui.ModTabs;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(NeoMafishMod.MODID)
public class NeoMafishMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "neomafishmod";
    private static final Logger LOGGER = LogUtils.getLogger();
    public NeoMafishMod(IEventBus modEventBus, ModContainer modContainer)
    {
        ModItems.ITEMS.register(modEventBus);
        ModTabs.CREATIVE_TABS.register(modEventBus);
    }
}
