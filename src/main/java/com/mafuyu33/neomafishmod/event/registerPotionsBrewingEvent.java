package com.mafuyu33.neomafishmod.event;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.item.ModItems;
import com.mafuyu33.neomafishmod.potion.ModPotions;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

@EventBusSubscriber(modid = NeoMafishMod.MODID)
public class registerPotionsBrewingEvent {

    @SubscribeEvent
    public static void onRegisterPotionsBrewing(RegisterBrewingRecipesEvent event){
        PotionBrewing.Builder builder = event.getBuilder();

        // 传送药水
        builder.addMix(Potions.AWKWARD,
                Items.POPPED_CHORUS_FRUIT, ModPotions.TELEPORT_POTION);
        //蜘蛛药水
        builder.addMix(Potions.AWKWARD,
                Items.FERMENTED_SPIDER_EYE, ModPotions.SPIDER_POTION);
        //变形魔药
        builder.addMix(Potions.AWKWARD,
                Items.GOAT_HORN, ModPotions.SHEEP_POTION);
        //百毒不侵
        builder.addMix(Potions.AWKWARD,
                Items.MILK_BUCKET, ModPotions.ANTIDOTE_POTION);
        //应激传送
        builder.addMix(Potions.AWKWARD,
                Items.CHORUS_FRUIT, ModPotions.EMERGENCY_TELEPORT_POTION);
        //海之嫌弃
        builder.addMix(Potions.AWKWARD,
                ModItems.MAFISH.get(), ModPotions.BAD_LUCK_OF_SEA_POTION);
    }
}
