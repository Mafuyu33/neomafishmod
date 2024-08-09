package com.mafuyu33.neomafishmod.sound;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {

    public static DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(net.minecraft.core.registries.Registries.SOUND_EVENT, NeoMafishMod.MODID);
    public static final DeferredHolder<SoundEvent,SoundEvent> METAL_DETECTOR_FOUND_ORE = registerSoundEvent("metal_detector_found_ore");
    public static final DeferredHolder<SoundEvent,SoundEvent> SOUND_BLOCK_BREAK = registerSoundEvent("sound_block_break");
    public static final DeferredHolder<SoundEvent,SoundEvent> SOUND_BLOCK_STEP = registerSoundEvent("sound_block_step");
    public static final DeferredHolder<SoundEvent,SoundEvent> SOUND_BLOCK_PLACE = registerSoundEvent("sound_block_place");
    public static final DeferredHolder<SoundEvent,SoundEvent> SOUND_BLOCK_HIT = registerSoundEvent("sound_block_hit");
    public static final DeferredHolder<SoundEvent,SoundEvent> SOUND_BLOCK_FALL = registerSoundEvent("sound_block_fall");
    public static final DeferredHolder<SoundEvent,SoundEvent> DASH_SOUND = registerSoundEvent("dash_sound");
    public static final DeferredHolder<SoundEvent,SoundEvent> NEVER1 = registerSoundEvent("never1");
    public static final DeferredHolder<SoundEvent,SoundEvent> NEVER2 = registerSoundEvent("never2");
    public static final DeferredHolder<SoundEvent,SoundEvent> NEVER3 = registerSoundEvent("never3");
    public static final DeferredHolder<SoundEvent,SoundEvent> NEVER4 = registerSoundEvent("never4");
    public static final DeferredHolder<SoundEvent,SoundEvent> NEVER5 = registerSoundEvent("never5");
    public static final DeferredHolder<SoundEvent,SoundEvent> NEVER6 = registerSoundEvent("never6");
    public static final DeferredHolder<SoundEvent,SoundEvent> NEVER7 = registerSoundEvent("never7");
    public static final DeferredHolder<SoundEvent,SoundEvent> NEVER8 = registerSoundEvent("never8");
    public static final DeferredHolder<SoundEvent,SoundEvent> NEVER9 = registerSoundEvent("never9");
    public static final DeferredHolder<SoundEvent,SoundEvent> NEVER10 = registerSoundEvent("never10");
    public static final DeferredHolder<SoundEvent,SoundEvent> CHEESE_BERGER_MAN = registerSoundEvent("cheese_berger_man");
    public static final DeferredHolder<SoundEvent,SoundEvent> CHEESE_BERGER_CAT = registerSoundEvent("cheese_berger_cat");
    public static final DeferredHolder<SoundEvent,SoundEvent> PIN = registerSoundEvent("pin");

    public static final DeferredSoundType SOUND_BLOCK_SOUNDS = new DeferredSoundType(1f, 1f,
            ModSounds.SOUND_BLOCK_BREAK, ModSounds.SOUND_BLOCK_STEP, ModSounds.SOUND_BLOCK_PLACE,
            ModSounds.SOUND_BLOCK_HIT, ModSounds.SOUND_BLOCK_FALL);


    public static DeferredHolder<SoundEvent,SoundEvent> registerSoundEvent(String name){
        return SOUNDS.register(name,()-> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,name)));
    }

    public static void register(IEventBus eventBus){
        SOUNDS.register(eventBus);
    }


}
