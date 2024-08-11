package com.mafuyu33.neomafishmod.setup;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.entity.ModEntities;
import com.mafuyu33.neomafishmod.render.CustomWindChargeRenderer;
import com.mafuyu33.neomafishmod.render.itemModel.ItemModelEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = NeoMafishMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetUp {

    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(ModEntities.TNT_PROJECTILE.get(),ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.STONE_PROJECTILE.get(),ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.LIGHTNING_PROJECTILE.get(),ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.CUSTOM_WIND_CHARGE.get(),CustomWindChargeRenderer::new);
    }


    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        ItemModelEvent.registerZhuGeItemProperties();
    }
}
