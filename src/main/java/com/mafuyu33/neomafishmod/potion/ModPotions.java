package com.mafuyu33.neomafishmod.potion;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.effect.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * @author Mafuyu33
 */
public class ModPotions {
    public static DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, NeoMafishMod.MODID);

    public static Holder<Potion> FLOWER_POTION = registerPotion("flower_potion",3600,5, ModEffects.FLOWER_EFFECT);
    public static Holder<Potion>  TELEPORT_POTION = registerPotion("teleport_potion",100,0,ModEffects.TELEPORT_EFFECT);
    public static Holder<Potion>  SPIDER_POTION =  registerPotion("spider_potion",3600,0,ModEffects.SPIDER_EFFECT);
    public static Holder<Potion>  SHEEP_POTION = registerPotion("sheep_potion",2000,0,ModEffects.SHEEP_EFFECT);
    public static Holder<Potion>  ANTIDOTE_POTION = registerPotion("antidote_potion",2000,0,ModEffects.ANTIDOTE_EFFECT);
    public static Holder<Potion>  EMERGENCY_TELEPORT_POTION = registerPotion("emergency_teleport_potion",3600,0,ModEffects.EMERGENCY_TELEPORT_EFFECT);
    public static Holder<Potion>  BAD_LUCK_OF_SEA_POTION = registerPotion("bad_luck_of_sea_potion",3600,0,ModEffects.BAD_LUCK_OF_SEA_EFFECT);
    public static Holder<Potion>  ROTATE_SCREEN_180_POTION = registerPotion("rotate_screen_180_potion",2000,0,ModEffects.ROTATE_SCREEN_180_EFFECT);
    public static Holder<Potion>  STRONG_ROTATE_SCREEN_180_POTION = registerPotion("strong_rotate_screen_180_potion",2000,1,ModEffects.ROTATE_SCREEN_180_EFFECT);

    public static void register(IEventBus eventBus){
        POTIONS.register(eventBus);
    }
    public static DeferredHolder<Potion, Potion> registerPotion(String name, int duration, int amplifier, Holder<MobEffect> statusEffects) {
        return POTIONS.register(name,()-> new Potion(new MobEffectInstance(statusEffects,duration,amplifier)));
    }


}
