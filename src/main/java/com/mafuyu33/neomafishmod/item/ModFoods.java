package com.mafuyu33.neomafishmod.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties Mafish = new FoodProperties.Builder().nutrition(4)
            .saturationModifier(0.3f).alwaysEdible()
            .effect(new MobEffectInstance(MobEffects.JUMP),1).build();
}
