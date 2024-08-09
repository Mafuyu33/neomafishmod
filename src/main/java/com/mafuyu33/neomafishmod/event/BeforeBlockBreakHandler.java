package com.mafuyu33.neomafishmod.event;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber
public class BeforeBlockBreakHandler {

    @SubscribeEvent
    public static void OnBlockBreak (BlockEvent.BreakEvent event){
        BlockPos pos = event.getPos();
        int level = BlockEnchantmentStorage.getLevel(Enchantments.PROTECTION, pos);
        if (level>0 && !event.getPlayer().isCreative()){
            event.setCanceled(true);
        }
    }
}
