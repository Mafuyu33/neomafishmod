package com.mafuyu33.neomafishmod.event.enchantment.trident_redirect;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = NeoMafishMod.MODID)
public class OnPlayerLeftClick {
    private static boolean playerLeftClicked = false;
    private static long lastClickTime = 0;

    // 公用方法，用于注册点击事件
    private static void registerLeftClick() {
        playerLeftClicked = true;
        lastClickTime = System.currentTimeMillis();
    }
    @SubscribeEvent
    private static void onPlayerLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        registerLeftClick();
    }
    @SubscribeEvent
    private static void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        registerLeftClick();
    }
    @SubscribeEvent
    private static void onPlayerAttackEntity(AttackEntityEvent event) {
        registerLeftClick();
    }

    // 判断玩家是否在 0.1 秒内点击过左键
    public static boolean onPlayerLeftClicked() {
        long currentTime = System.currentTimeMillis();
        if (playerLeftClicked && (currentTime - lastClickTime <= 100)) {
            return true;
        }
        // 超过时间后，回正状态
        playerLeftClicked = false;
        return false;
    }
}
