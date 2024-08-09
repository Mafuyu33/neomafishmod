package com.mafuyu33.neomafishmod.event;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;


@EventBusSubscriber(modid = NeoMafishMod.MODID,value = Dist.CLIENT,bus = EventBusSubscriber.Bus.GAME)
@OnlyIn(Dist.CLIENT)
public class KeyInputHandler {


    public static final String KEY_CATEGORY_TUTORIAL = "key.category.mafishmod.tutorial";
    public static final String KEY_THROW_ITEM = "key.mafishmod.throw_item";

    public static KeyMapping throwingKey = new KeyMapping(KEY_THROW_ITEM,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Y,
            KEY_CATEGORY_TUTORIAL);

    private static float throwPower = 0.0f;
    private static boolean isCharging = false;
    private static final float maxThrowPower = 2.0f; // 最大力度上限
    private static long chargeStartTime = 0;
    private static long chargeEndTime = 0;


    @SubscribeEvent
    public static void registerKeyInputs(ClientTickEvent.Post event) {
        var client = Minecraft.getInstance();
        if (throwingKey.isDown())
        {
            if (!isCharging)
            {
                chargeStartTime = System.currentTimeMillis();
                isCharging = true;
            }
            else
            {
                chargeEndTime = System.currentTimeMillis();
                long chargeDuration = chargeEndTime - chargeStartTime;

                throwPower = Math.min((float) (chargeDuration/1000.0),maxThrowPower);
                client.player.displayClientMessage(Component.literal("蓄力: "+throwPower),true);

            }
        }else if(isCharging)
        {
            chargeEndTime = System.currentTimeMillis();
            long chargeDuration = chargeEndTime - chargeStartTime;
            throwPower = Math.min((float)(chargeDuration / 1000.0),maxThrowPower);
            client.player.displayClientMessage(Component.literal("重置为0"),true);
            throwPower = 0.0f;
            isCharging = false;
            chargeStartTime = 0;
            chargeEndTime = 0;
        }
    }

    @EventBusSubscriber(modid = NeoMafishMod.MODID,value = Dist.CLIENT,bus = EventBusSubscriber.Bus.MOD)
    public static class KeyRegistries{
        @SubscribeEvent
        public static void register(RegisterKeyMappingsEvent event){
            event.register(KeyInputHandler.throwingKey);
        }
    }


    public static float getThrowPower(){
        return throwPower;
    }


}
