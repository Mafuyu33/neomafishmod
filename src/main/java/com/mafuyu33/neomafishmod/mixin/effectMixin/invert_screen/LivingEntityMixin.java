package com.mafuyu33.neomafishmod.mixin.effectMixin.invert_screen;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.effect.ModEffects;
import com.mafuyu33.neomafishmod.event.KeyInputHandler;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

/**
 * @author Mafuyu33
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
    @Shadow public abstract boolean hasEffect(Holder<MobEffect> effect);

    @Shadow @Nullable public abstract MobEffectInstance getEffect(Holder<MobEffect> effect);
    @Unique
    private int neomafishmod$lastEffectAmp = -1;
    @Unique
    private boolean neomafishmod$effectLoaded = false;
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    @Unique
    private float neomafishmod$lastTimeValue = 0.0f;
    @Unique
    private CameraType neomafishmod$lastCameraType = null;

    @Inject(at=@At("TAIL"),method = "tick")
    private void init(CallbackInfo ci){
        if(this.level().isClientSide){
            Minecraft mc = Minecraft.getInstance();
            // 获取当前的 CameraType
            CameraType currentCameraType = mc.options.getCameraType();
            // 有效果
            if(this.hasEffect(ModEffects.ROTATE_SCREEN_180_EFFECT)){
                int amp = this.getEffect(ModEffects.ROTATE_SCREEN_180_EFFECT).getAmplifier();
                //如果效果的等级等于0
                if(amp == 0){
                    if (!neomafishmod$effectLoaded) {
                        neomafishmod$effectLoaded = true;
                        mc.gameRenderer.loadEffect(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,"shaders/post/rotate_screen_180.json"));
                    }
                    // 检测 F5 视角是否变化
                    if (neomafishmod$lastCameraType != null && neomafishmod$lastCameraType.isFirstPerson() != currentCameraType.isFirstPerson()) {
                        neomafishmod$effectLoaded = true;
                        mc.gameRenderer.loadEffect(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID, "shaders/post/rotate_screen_180.json"));
                    }
                }else if(amp > 0){
                //效果的等级大于0
                    float gameTime = mc.level.getGameTime() + mc.getTimer().getGameTimeDeltaTicks();
                    float rawTimeValue = (gameTime % 360) * 0.025f * (amp + 1);
                    // 进行线性插值
                    float smoothTimeValue = neomafishmod$lastTimeValue + (rawTimeValue - neomafishmod$lastTimeValue) * 0.2f;
                    if (mc.gameRenderer.currentEffect() != null) {
                        mc.gameRenderer.currentEffect().setUniform("time", smoothTimeValue);
                    }
                    neomafishmod$lastTimeValue = smoothTimeValue;
                    if (!neomafishmod$effectLoaded || neomafishmod$lastEffectAmp != amp) {
                        neomafishmod$effectLoaded = true;
                        mc.gameRenderer.loadEffect(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,"shaders/post/rotate_screen_360.json"));
                    }
                    // 检测 F5 视角是否变化
                    if (neomafishmod$lastCameraType != null && neomafishmod$lastCameraType.isFirstPerson() != currentCameraType.isFirstPerson()) {
                        neomafishmod$effectLoaded = true;
                        mc.gameRenderer.loadEffect(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID, "shaders/post/rotate_screen_360.json"));
                    }
                }
                neomafishmod$lastEffectAmp = amp;
            }
            // 没有效果
            if (neomafishmod$effectLoaded && !this.hasEffect(ModEffects.ROTATE_SCREEN_180_EFFECT)) {
                neomafishmod$effectLoaded = false;
                neomafishmod$lastEffectAmp = -1;
                Minecraft.getInstance().gameRenderer.shutdownEffect();
            }
            // 记录当前 Tick 的 CameraType，作为下一个 Tick 的“上一次视角”
            neomafishmod$lastCameraType = currentCameraType;
        }
    }
}
