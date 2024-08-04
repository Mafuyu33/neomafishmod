package com.mafuyu33.neomafishmod.mixin.item.lead;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Leashable.class)
public interface LeashableMixin {
    @Shadow
    private static <E extends Entity & Leashable> void restoreLeashFromSave(E p_entity, Leashable.LeashData leashData) {
    }
    @Shadow
    private static <E extends Entity & Leashable> void dropLeash(E entity, boolean broadcastPacket, boolean dropItem) {
    }

    /**
     * @author
     * Mafuyu33
     * @reason
     * Change Lead Logic
     */
    @Overwrite
    static <E extends Entity & Leashable> void tickLeash(E p_entity) {
        Leashable.LeashData leashable$leashdata = ((Leashable)p_entity).getLeashData();
        if (leashable$leashdata != null && leashable$leashdata.delayedLeashInfo != null) {
            restoreLeashFromSave(p_entity, leashable$leashdata);
        }

        if (leashable$leashdata != null && leashable$leashdata.leashHolder != null) {

            if (!p_entity.isAlive() || !leashable$leashdata.leashHolder.isAlive()) {
                dropLeash(p_entity, true, true);
            }

            Entity entity = ((Leashable)p_entity).getLeashHolder();
            if (entity != null && entity.level() == p_entity.level()) {
                float f = p_entity.distanceTo(entity);
                if (!((Leashable)p_entity).handleLeashAtDistance(entity, f)) {
                    return;
                }


                if(leashable$leashdata.leashHolder instanceof Player player) {
                    if ((double)f > 10.0) {
                        ((Leashable)p_entity).leashTooFarBehaviour();
                    } else if ((double)f > 6.0) {
                        ((Leashable)p_entity).elasticRangeLeashBehaviour(entity, f);
                        p_entity.checkSlowFallDistance();
                        player.hurt(player.damageSources().generic(),1);//尝试伤害玩家
                        player.setDeltaMovement(0,1,0);//尝试位移玩家
                    } else {
                        ((Leashable)p_entity).closeRangeLeashBehaviour(entity);
                    }
                }
            }
        }

    }
}