package com.mafuyu33.neomafishmod.event;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.mixinhelper.BowDashMixinHelper;
import com.mafuyu33.neomafishmod.mixinhelper.ElytraJumpMixinHelper;
import com.mafuyu33.neomafishmod.mixinhelper.ShieldDashMixinHelper;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = NeoMafishMod.MODID, bus = EventBusSubscriber.Bus.GAME,value = Dist.CLIENT)
public class AttackKeyCheckHandler {
    // 获取 MinecraftClient 实例
//    private static final MinecraftClient client = MinecraftClient.getInstance();
//
//    // 获取 MinecraftClient 中的攻击键绑定
//    private static final KeyBinding attackKeyBinding = client.options.attackKey;
    private static boolean wasKeyPressedLastFrame = false;
    private static boolean wasJumpKeyPressedLastFrame = false;
    private static boolean wasShiftKeyPressedLastFrame = false;

    @SubscribeEvent
    public static void OnClientTickStart(ClientTickEvent.Pre event){
        Minecraft client = Minecraft.getInstance();
        registerAttackKeyListener(client);
    }

    // 在初始化阶段注册攻击键的按下事件监听器
    public static void registerAttackKeyListener(Minecraft client) {
            // 检查攻击键是否在当前帧被按下
            boolean isKeyPressed = client.options.keyAttack.isDown();

            // 如果攻击键在上一帧被按下而在当前帧没有被按下，表示松开了按键
            if (wasKeyPressedLastFrame && !isKeyPressed) {
                // 当攻击键被松开时执行操作
//                System.out.println("Attack key released!");
                ShieldDashMixinHelper.setIsAttackKeyPressed(false);
                BowDashMixinHelper.setIsAttackKeyPressed(false);

                // 在这里执行您的操作
            }

            // 更新攻击键上一帧的按下状态
            wasKeyPressedLastFrame = isKeyPressed;

            // 检查攻击键是否在当前帧被按下
            if (isKeyPressed) {
                // 当攻击键被按下时执行操作
//                System.out.println("Attack key pressed!");
                ShieldDashMixinHelper.setIsAttackKeyPressed(true);
                BowDashMixinHelper.setIsAttackKeyPressed(true);
                // 在这里执行您的操作
            }


            boolean isJumpKeyPressed = client.options.keyJump.isDown();
            // 如果攻击键在上一帧被按下而在当前帧没有被按下，表示松开了按键
            if (wasJumpKeyPressedLastFrame && !isJumpKeyPressed) {
                // 当攻击键被松开时执行操作
//                System.out.println("Attack key released!");
                ElytraJumpMixinHelper.setIsJumpKeyPressed(false);
                // 在这里执行您的操作
            }

            // 更新攻击键上一帧的按下状态
            wasJumpKeyPressedLastFrame = isJumpKeyPressed;

            // 检查攻击键是否在当前帧被按下
            if (isJumpKeyPressed) {
                // 当攻击键被按下时执行操作
//                System.out.println("Attack key pressed!");
                ElytraJumpMixinHelper.setIsJumpKeyPressed(true);
                // 在这里执行您的操作
            }

            boolean isShiftKeyPressed = client.options.keyShift.isDown();
            // 如果攻击键在上一帧被按下而在当前帧没有被按下，表示松开了按键
            if (wasJumpKeyPressedLastFrame && !isJumpKeyPressed) {
                // 当攻击键被松开时执行操作
//                System.out.println("Attack key released!");
                ElytraJumpMixinHelper.setIsJumpKeyPressed(false);
                // 在这里执行您的操作
            }

            // 更新攻击键上一帧的按下状态
            wasJumpKeyPressedLastFrame = isJumpKeyPressed;

            // 检查攻击键是否在当前帧被按下
            if (isJumpKeyPressed) {
                // 当攻击键被按下时执行操作
//                System.out.println("Attack key pressed!");
                ElytraJumpMixinHelper.setIsJumpKeyPressed(true);
                // 在这里执行您的操作
            }
    }
}