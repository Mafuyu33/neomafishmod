package com.mafuyu33.neomafishmod.event.enchantment.trident_redirect;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = NeoMafishMod.MODID)
public class OnPlayerLeftClick {
    private static boolean playerLeftClicked = false;
    @SubscribeEvent
    private static void onPlayerLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        playerLeftClicked = true;
    }
    private static void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        playerLeftClicked = true;
    }


    public static boolean onPlayerLeftClicked() {
        boolean clicked = playerLeftClicked;
        playerLeftClicked = false;
        return clicked;
    }


}
