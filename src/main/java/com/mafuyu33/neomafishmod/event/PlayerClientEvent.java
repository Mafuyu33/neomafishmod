package com.mafuyu33.neomafishmod.event;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.mixinhelper.BellBlockDelayMixinHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;

@EventBusSubscriber(modid = NeoMafishMod.MODID,value = Dist.CLIENT)
public class PlayerClientEvent {
    @SubscribeEvent
    public static void onPlayerDisConnection(EntityLeaveLevelEvent event){
        //客户端已经成功连接到服务器
        // 清空hashmap
        BellBlockDelayMixinHelper.BellBlockEntityMap.clear();
        BellBlockDelayMixinHelper.HitCoolDownMap.clear();
        BellBlockDelayMixinHelper.DirectionMap.clear();
    }
}
