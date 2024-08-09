package com.mafuyu33.neomafishmod.mixin.effectMixin.sheep;

import com.mafuyu33.neomafishmod.effect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
    @Shadow public abstract boolean hasEffect(Holder<MobEffect> effect);

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }


    @Inject(at = @At("HEAD"), method = "tick")
    private void init(CallbackInfo info) {
        if(!this.level().isClientSide) {
            if (this.hasEffect(ModEffects.SHEEP_EFFECT)) {//变羊药水
                if (!this.isAlwaysTicking()) {//如果不是玩家的话
                    Vec3 pos = this.position();
                        EntityType.SHEEP.spawn(((ServerLevel) this.level()), BlockPos.containing(pos), MobSpawnType.TRIGGERED);
                    this.remove(RemovalReason.KILLED);
                }
            }
        }
    }

}
