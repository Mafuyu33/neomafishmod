package com.mafuyu33.neomafishmod.mixin.itemmixin.swimmingtripwire;

import com.mafuyu33.neomafishmod.Config;
import com.mafuyu33.neomafishmod.mixinhelper.TripwireBlockMixinHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
    @Inject(at = @At("RETURN"), method = "updatePlayerPose")
    private void init(CallbackInfo ci) {
        boolean isSwimTripwire = Config.isSwimTripwire();
        if (isSwimTripwire) {
            if (TripwireBlockMixinHelper.getEntityValue(this.getId()) > 0) {
                Pose entityPose3 = Pose.SWIMMING;
                this.setPose(entityPose3);
                TripwireBlockMixinHelper.storeEntityValue(this.getId(), TripwireBlockMixinHelper.getEntityValue(this.getId()) - 1);
            }
        }
    }
}
