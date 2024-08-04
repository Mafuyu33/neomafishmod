package com.mafuyu33.neomafishmod.event.pickup_mafish;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;

@EventBusSubscriber(modid = NeoMafishMod.MODID)
public class ItemEntityPickup {
    @SubscribeEvent
    public static void ItemEntityPickupEvent(ItemEntityPickupEvent.Pre event) {//去Event类，按ctrl+h，展开看所有events
        if (!event.getItemEntity().hasPickUpDelay() && event.getItemEntity().getItem().is(ModItems.MAFISH) && !event.getPlayer().level().isClientSide) {
            if (event.getPlayer() instanceof Player player) {
                player.sendSystemMessage(Component.literal(  "被"+ player.getName().getString() +"捡起来了!(*/ω＼*)"));
            }
        }
    }
}
