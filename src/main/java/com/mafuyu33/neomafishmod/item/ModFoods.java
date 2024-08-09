package com.mafuyu33.neomafishmod.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties Mafish = new FoodProperties.Builder().nutrition(4)
            .saturationModifier(0.3f).alwaysEdible()
            .effect(new MobEffectInstance(MobEffects.JUMP),1).build();


    public static final FoodProperties BREAD_SWORD=
            new FoodProperties.Builder().nutrition(3).saturationModifier(0.25f).alwaysEdible().build();
    public static final FoodProperties BREAD_SWORD_HOT=
            new FoodProperties.Builder().nutrition(3).saturationModifier(0.25f)
                    .effect(()-> new MobEffectInstance(MobEffects.HEALTH_BOOST,200),0.25f).alwaysEdible().build();
    public static final FoodProperties BREAD_SWORD_VERY_HOT=
            new FoodProperties.Builder().nutrition(3).saturationModifier(0.25f).alwaysEdible().build();
    public static final FoodProperties POISON_SWORD =
            new FoodProperties.Builder().nutrition(3).saturationModifier(0.25f).alwaysEdible().build();
    public static final FoodProperties CHEESE_BERGER=new FoodProperties.Builder().nutrition(10).saturationModifier(0.5f)
            .effect(()->new MobEffectInstance(MobEffects.HUNGER,200),0.5f).build();
    public static final FoodProperties VILLAGER_ITEM = new FoodProperties.Builder().nutrition(10).saturationModifier(0.5f)
            .effect(()->new MobEffectInstance(MobEffects.HUNGER,200),1f).alwaysEdible().build();
    public static final FoodProperties IRON_GOLEM_ITEM = new FoodProperties.Builder().nutrition(3).saturationModifier(0.5f)
            .effect(()->new MobEffectInstance(MobEffects.HARM,20),1f).alwaysEdible().build();
    public static final FoodProperties SWITCH =new FoodProperties.Builder().nutrition(3).saturationModifier(0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.HARM,20),1f).alwaysEdible().build();
    public static final FoodProperties STARGAZY_PIE=new FoodProperties.Builder().nutrition(3).saturationModifier(0.25f)
            .effect(()->new MobEffectInstance(MobEffects.POISON,200),1f)
            .effect(()->new MobEffectInstance(MobEffects.CONFUSION,400),1f).alwaysEdible().build();
    public static final FoodProperties MILK_FLESH=
            new FoodProperties.Builder().nutrition(3).saturationModifier(0.25f).alwaysEdible().build();

}
