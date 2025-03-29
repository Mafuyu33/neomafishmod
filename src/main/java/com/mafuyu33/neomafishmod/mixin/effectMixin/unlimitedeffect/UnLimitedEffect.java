package com.mafuyu33.neomafishmod.mixin.effectMixin.unlimitedeffect;


import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author mafuyu33
 */
@Mixin(MobEffectInstance.class)
public class UnLimitedEffect {

    @Shadow private int amplifier;

    @Inject(
            method = "<init>(Lnet/minecraft/core/Holder;IIZZZLnet/minecraft/world/effect/MobEffectInstance;)V",
            at = @At("RETURN")
    )
    private void onConstructed(
            net.minecraft.core.Holder<?> effect,
            int duration,
            int amplifier,
            boolean ambient,
            boolean visible,
            boolean showIcon,
            MobEffectInstance hiddenEffect,
            CallbackInfo ci
    ) {
        // 构造函数执行完毕后，将字段值直接设置为传入的原始值
        if (amplifier > 255) {
            this.amplifier = amplifier;
        }
    }
}
