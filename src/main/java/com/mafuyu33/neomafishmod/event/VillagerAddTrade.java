package com.mafuyu33.neomafishmod.event;

import com.mafuyu33.neomafishmod.block.ModBlock;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;

@EventBusSubscriber
public class VillagerAddTrade {

    @SubscribeEvent
    public static void addVillagerTrade(VillagerTradesEvent event){

        if (event.getType() == VillagerProfession.FARMER){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            int tradeLevel = 1;

            trades.get(tradeLevel).add(((trader, random) -> new MerchantOffer(new ItemCost(Items.EMERALD,30),new ItemStack(ModBlock.GOLD_MELON.asItem(),1),6,5,0.05f)));
        }
    }
}
