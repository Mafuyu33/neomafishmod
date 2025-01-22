package com.mafuyu33.neomafishmod.effect;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.effect.custom.NormalEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEffects {
    public static  DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, NeoMafishMod.MODID);

    public static final DeferredHolder<MobEffect,MobEffect> IRONMAN =registerDeferredHolder("ironman",()->new NormalEffect(MobEffectCategory.BENEFICIAL,0xFF0000)) ;
    public static final DeferredHolder<MobEffect,MobEffect>  FLOWER_EFFECT =registerDeferredHolder("flower_effect",()->new NormalEffect(MobEffectCategory.BENEFICIAL,0xFF0000)) ;
    public static final DeferredHolder<MobEffect,MobEffect>  TELEPORT_EFFECT = registerDeferredHolder("teleport_effect",()->new NormalEffect(MobEffectCategory.NEUTRAL,0x00FFFF));
    public static final DeferredHolder<MobEffect,MobEffect>  SPIDER_EFFECT = registerDeferredHolder("spider_effect",()->new NormalEffect(MobEffectCategory.BENEFICIAL,0x800000));
    public static final DeferredHolder<MobEffect,MobEffect>  SHEEP_EFFECT = registerDeferredHolder("sheep_effect",()->new NormalEffect(MobEffectCategory.NEUTRAL,0x80F18BEB));
    public static final DeferredHolder<MobEffect,MobEffect>  ANTIDOTE_EFFECT =registerDeferredHolder("antidote_effect",()->new NormalEffect(MobEffectCategory.BENEFICIAL,0x80FFFFFF));
    public static final DeferredHolder<MobEffect,MobEffect>  EMERGENCY_TELEPORT_EFFECT =registerDeferredHolder("emergency_teleport_effect",()->new NormalEffect(MobEffectCategory.BENEFICIAL,0xD8BFD8));
    public static final DeferredHolder<MobEffect,MobEffect>  BAD_LUCK_OF_SEA_EFFECT =registerDeferredHolder("bad_luck_of_sea_effect",()->new NormalEffect(MobEffectCategory.HARMFUL,0x4682B4));


    public static DeferredHolder<MobEffect,MobEffect> registerDeferredHolder(String name, Supplier<MobEffect> supplier){
        return EFFECTS.register(name,supplier);
    }

    public static void register(IEventBus eventBus){
        EFFECTS.register(eventBus);
    }
}
