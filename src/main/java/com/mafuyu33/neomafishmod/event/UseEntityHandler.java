package com.mafuyu33.neomafishmod.event;

import com.mafuyu33.neomafishmod.Config;
import com.mafuyu33.neomafishmod.item.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;


@EventBusSubscriber
public class UseEntityHandler  {

    @SubscribeEvent
    public static void useEntity(PlayerInteractEvent.EntityInteract event){
        InteractionHand hand = event.getHand();
        Level level = event.getLevel();
        Player player = event.getEntity();
        Entity target = event.getTarget();
        interact(player,level,hand, target);
    }

    public static void interact(Player player, Level world, InteractionHand hand, Entity entity) {
        if(Config.isQinNa()) {
            if (entity instanceof Llama && !world.isClientSide()) {//羊驼
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.LLAMA_HURT, SoundSource.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
                entity.spawnAtLocation(ModItems.LLAMA_ITEM);
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
            if (entity instanceof Villager && !world.isClientSide()) {//村民
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.VILLAGER_HURT, SoundSource.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
                entity.spawnAtLocation(ModItems.VILLAGER_ITEM);
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
            if (entity instanceof IronGolem && !world.isClientSide()) {//铁傀儡
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.IRON_GOLEM_HURT, SoundSource.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
                entity.spawnAtLocation(ModItems.IRON_GOLEM_ITEM);
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }
}
