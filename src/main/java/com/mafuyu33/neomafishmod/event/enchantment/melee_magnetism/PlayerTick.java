package com.mafuyu33.neomafishmod.event.enchantment.melee_magnetism;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static com.mafuyu33.neomafishmod.event.enchantment.melee_magnetism.OnPlayerAttack.DURATION;
@EventBusSubscriber(modid = NeoMafishMod.MODID)
public class PlayerTick {
    @SubscribeEvent
    public static void onPlayerTick(RenderFrameEvent.Pre event) {
        if (Minecraft.getInstance().level != null) {
            for (Player player : Minecraft.getInstance().level.players()) {
                updatePlayerView(player);
            }
        }
    }

    public static void updatePlayerView(Player player) {
        if (OnPlayerAttack.targetPosition != null) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - OnPlayerAttack.startTime;
            if (elapsedTime < DURATION) {
                double t = (double) elapsedTime / DURATION;
                Vec3 currentLookVec = player.getLookAngle();
                Vec3 targetLookVec = OnPlayerAttack.targetPosition.subtract(player.position()).normalize();
                Vec3 interpolatedLookVec = currentLookVec.scale(1 - t).add(targetLookVec.scale(t)).normalize();
                player.lookAt(player.createCommandSourceStack().getAnchor(), player.position().add(interpolatedLookVec));
            } else {
                player.lookAt(player.createCommandSourceStack().getAnchor(), OnPlayerAttack.targetPosition);
                OnPlayerAttack.targetPosition = null;
            }
        }
    }
}
