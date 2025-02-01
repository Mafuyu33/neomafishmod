package com.mafuyu33.neomafishmod;

import com.mafuyu33.neomafishmod.block.ModBlock;
import com.mafuyu33.neomafishmod.effect.ModEffects;
import com.mafuyu33.neomafishmod.entity.custom.CustomWindChargeEntity;
import com.mafuyu33.neomafishmod.entity.ModEntities;
import com.mafuyu33.neomafishmod.item.ModItems;
import com.mafuyu33.neomafishmod.item.component.ModDataComponents;
import com.mafuyu33.neomafishmod.potion.ModPotions;
import com.mafuyu33.neomafishmod.render.CustomWindChargeRenderer;
import com.mafuyu33.neomafishmod.sound.ModSounds;
import com.mafuyu33.neomafishmod.ui.ModTabs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
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
    public static final Logger LOGGER = LogUtils.getLogger();
    public NeoMafishMod(IEventBus modEventBus, ModContainer modContainer)
    {
        ModDataComponents.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModTabs.CREATIVE_TABS.register(modEventBus);
        ModBlock.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEffects.register(modEventBus);
        ModPotions.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON,Config.SPEC);
    }
}
