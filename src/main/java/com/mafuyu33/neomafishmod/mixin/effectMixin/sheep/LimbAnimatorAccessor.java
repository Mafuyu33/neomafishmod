package com.mafuyu33.neomafishmod.mixin.effectMixin.sheep;

import net.minecraft.world.entity.WalkAnimationState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WalkAnimationState.class)
public  interface LimbAnimatorAccessor {


    @Accessor("speedOld")
    float getSpeedOld();

    @Accessor("speedOld")
    void setSpeedOld(float speedOld);

    @Accessor
    float getSpeed();

    @Accessor
    void setSpeed(float speed);

    @Accessor
    float getPosition();

    @Accessor
    void setPosition(float pos);


}
