package com.mafuyu33.neomafishmod.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class PoisonSwordItem extends SwordItem {
    private int delayTimer = 0;

    public PoisonSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }


    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (delayTimer < 16){
            delayTimer++;
        }else{
            livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON,400,0));
        }

        if (livingEntity.getHealth()==1){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.HARM,10,0));
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addEffect(new MobEffectInstance(MobEffects.POISON,200,0));
        return super.hurtEnemy(stack, target, attacker);
    }

}
