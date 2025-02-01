package com.mafuyu33.neomafishmod;


import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

@ApiStatus.Internal
@Mod(value = NeoMafishModClient.MODID,dist = Dist.CLIENT)
public class NeoMafishModClient
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "neomafishmod";
    public static final Logger LOGGER = LogUtils.getLogger();
    public NeoMafishModClient(IEventBus modEventBus, ModContainer modContainer)
    {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
