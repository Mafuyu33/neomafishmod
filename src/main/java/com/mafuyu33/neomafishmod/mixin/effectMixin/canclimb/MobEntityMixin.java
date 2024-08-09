package com.mafuyu33.neomafishmod.mixin.effectMixin.canclimb;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Targeting;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.security.auth.callback.Callback;

@Mixin(Mob.class)
public abstract class MobEntityMixin extends LivingEntity implements Targeting {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"),method = "createNavigation",cancellable = true)
    private void init1(Level level, CallbackInfoReturnable<PathNavigation> cir){
        cir.setReturnValue(new WallClimberNavigation((Mob) (Object)this,level));
    }
}
