package com.mafuyu33.neomafishmod.item.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.logging.Level;

public class SwitchItem extends Item {
    public SwitchItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onUseTick(net.minecraft.world.level.Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration % 20==0){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));         // 中毒
            livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));       // 虚弱
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));       // 缓慢
            livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0)); // 挖掘疲劳
            livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));      // 失明
            livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, 0));         // 饥饿
            livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0));         // 反胃
            livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0));         // 凋零
            livingEntity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 100, 0));     // 漂浮
            livingEntity.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 100, 0));         // 不幸
            livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0));
        }
    }
}
