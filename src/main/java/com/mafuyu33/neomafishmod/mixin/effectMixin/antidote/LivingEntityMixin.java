package com.mafuyu33.neomafishmod.mixin.effectMixin.antidote;

import com.mafuyu33.neomafishmod.effect.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Final
    @Shadow private Map<MobEffect, MobEffectInstance> activeEffects;

    @Shadow public abstract boolean hasEffect(Holder<MobEffect> effect);

    @Shadow protected abstract void onEffectRemoved(MobEffectInstance effectInstance);

    @Inject(at=@At("HEAD"),method = "tick")
    private void init(CallbackInfo ci){
        if(this.hasEffect(ModEffects.ANTIDOTE_EFFECT)){
            if(!this.level().isClientSide){
                var iterator = this.activeEffects.values().iterator();
                boolean b1;
                for(b1 = false;iterator.hasNext();b1=true){
                    MobEffectInstance next = iterator.next();
                    if(mafishmod$isNegativeEffect(next)){
                        this.onEffectRemoved(next); // 调用onStatusEffectRemoved方法处理当前状态效果
                        iterator.remove(); // 从活跃状态效果列表中移除当前状态效果
                    }
                }
            }
        }
    }


    @Unique
    private boolean mafishmod$isNegativeEffect(MobEffectInstance effect) {
        // 你需要根据实际情况实现此方法，判断效果类型是否为负面效果
        // 例如，根据效果类型进行判断
        return effect.getEffect().value().getCategory()== MobEffectCategory.HARMFUL;
    }
}
