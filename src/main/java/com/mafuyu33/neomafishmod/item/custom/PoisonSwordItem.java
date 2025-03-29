package com.mafuyu33.neomafishmod.item.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PoisonSwordItem extends Item {
    private int delayTimer = 0;

    public PoisonSwordItem(Properties properties) {
        // 注册时请调用 properties.sword(...) 来设置武器相关数据组件
        super(properties);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (delayTimer < 16) {
            delayTimer++;
        } else {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 400, 0));
        }

        if (livingEntity.getHealth() == 1) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 10, 0));
        }
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
        super.hurtEnemy(stack, target, attacker);
    }
}
