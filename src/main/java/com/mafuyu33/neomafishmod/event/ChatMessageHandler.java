package com.mafuyu33.neomafishmod.event;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;

import java.util.UUID;

@EventBusSubscriber(modid = NeoMafishMod.MODID,value = Dist.CLIENT,bus = EventBusSubscriber.Bus.GAME)
public class ChatMessageHandler {
    private static int number = 999999999;

    public ChatMessageHandler(int number) {
        ChatMessageHandler.number = number;
    }

    @SubscribeEvent
    public static void onClientReceiveMessage(ClientChatReceivedEvent event){
        Component message = event.getMessage();
        UUID sender = event.getSender();
        register(message);
    }
    public static void register(Component message) {
        // 注册客户端接收聊天消息事件监听器
        // 在这里可以处理接收到的聊天消息, 获取“<玩家名称> 玩家输入的文字” 后面的文字
        String string = message.getString();
        int index = string.indexOf('>');
        String textAfterArrow = "";
        if (index != -1 && index + 1 < string.length()) {
            textAfterArrow = string.substring(index + 1).trim();
        }

        // 尝试将文本内容转换为整数
        try {
            number = Integer.parseInt(textAfterArrow);
            System.out.println("转换后的数字为: " + number);
        } catch (NumberFormatException e) {
            System.out.println("文本内容不是一个有效的数字。");
        }
    }

    public static int getNumber() {
        return number;
    }
}
