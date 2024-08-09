package com.mafuyu33.neomafishmod.item.custom;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;



public class TimeStopItem extends Item {
    private static boolean startStop = false;

    public TimeStopItem(Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if(!world.isClientSide){
        startStop=!startStop;


            System.out.println("切换");
            if (startStop) {
                if (user instanceof ServerPlayer) {
                    MinecraftServer server = user.getServer();
                    // 获取服务器命令调度程序
                    CommandDispatcher<CommandSourceStack> dispatcher = server.getCommands().getDispatcher();
                    try {
                        // 解析指令并获取命令源
                        ParseResults<CommandSourceStack> parseResults
                                = dispatcher.parse("gamerule sendCommandFeedback false", server.createCommandSourceStack());
                        // 执行指令
                        dispatcher.execute(parseResults);

                        // 在控制台输出提示信息
                    } catch (CommandSyntaxException e) {
                        // 指令语法异常处理
                        e.printStackTrace();
                    }
                    try {
                        // 解析指令并获取命令源
                        ((ServerPlayer) user).sendSystemMessage(Component.literal(("启动时间停止")),true);
                        ParseResults<CommandSourceStack> parseResults
                                = dispatcher.parse("tick freeze", server.createCommandSourceStack());
                        // 执行指令
                        dispatcher.execute(parseResults);

                        // 在控制台输出提示信息
                    } catch (CommandSyntaxException e) {
                        // 指令语法异常处理
                        e.printStackTrace();
                    }
                }
            }else {
                MinecraftServer server = user.getServer();
                // 获取服务器命令调度程序
                CommandDispatcher<CommandSourceStack> dispatcher = server.getCommands().getDispatcher();
                try {
                    // 解析指令并获取命令源
                    ((ServerPlayer)user).sendSystemMessage(Component.literal(("停止时间停止")),true);
                    ParseResults<CommandSourceStack> parseResults
                            = dispatcher.parse("tick unfreeze", server.createCommandSourceStack());
                    // 执行指令
                    dispatcher.execute(parseResults);

                    // 在控制台输出提示信息
                } catch (CommandSyntaxException e) {
                    // 指令语法异常处理
                    e.printStackTrace();
                }
            }
        }
        return InteractionResultHolder.success(itemStack);
    }

    private static Vec3 lastPos= new Vec3(0, 0, 0);
    private static Vec3 lastPosMainController= new Vec3(0, 0, 0);
    private static Vec3 lastPosOffController= new Vec3(0, 0, 0);
    private static Vec3 lastPosHMD= new Vec3(0, 0, 0);
    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

//        if (startStop && entity instanceof Player user && !world.isClientSide
//                && VRPlugin.canRetrieveData(user)) {//有MC-VR-API并且在VR中的时候
        if (false){// todo vr
//            Vec3 currentPosMainController = VRDataHandler.getControllerPosition(user, 0);
//            Vec3 currentPosOffController = VRDataHandler.getControllerPosition(user, 1);
//            Vec3 currentPosHMD = VRDataHandler.getHMDPosition(user);
//
//            double mainControllerDistance = currentPosMainController.distanceTo(lastPosMainController); // 计算当前位置和上一个位置之间的距离
//            double offControllerDistance = currentPosOffController.distanceTo(lastPosOffController);
//            double HMDDistance = currentPosHMD.distanceTo(lastPosHMD);
//
//            System.out.println("main"+mainControllerDistance);
//            System.out.println("off"+offControllerDistance);
//            System.out.println("HMD"+HMDDistance);
//
//            boolean isMoving = mainControllerDistance > 0.051
//                    || offControllerDistance > 0.051
//                    || HMDDistance > 0.051;// 设置阈值
//
//            //你帮我写判断语句的部分...
//            CommandProcessing(user,isMoving);
//
//            lastPosMainController=currentPosMainController;
//            lastPosOffController=currentPosOffController;
//            lastPosHMD=currentPosHMD;

        }else if(!world.isClientSide && entity instanceof Player user && startStop) {//普通版本
            Vec3 currentPos = user.position();
            double distance = currentPos.distanceTo(lastPos); // 计算当前位置和上一个位置之间的距离
            boolean isMoving = distance > 0.09; // 设置一个小于的阈值，比如0.1

            lastPos = currentPos;

//            System.out.println(distance);

            CommandProcessing(user,isMoving);
        }
    }
    private static void CommandProcessing(Player user,boolean isMoving){
        if (isMoving) {
            MinecraftServer server = user.getServer();
            // 获取服务器命令调度程序
            CommandDispatcher<CommandSourceStack> dispatcher = server.getCommands().getDispatcher();
            try {
                // 解析指令并获取命令源
                ParseResults<CommandSourceStack> parseResults
                        = dispatcher.parse("tick unfreeze", server.createCommandSourceStack());
                // 执行指令
                dispatcher.execute(parseResults);

                // 在控制台输出提示信息
            } catch (CommandSyntaxException e) {
                // 指令语法异常处理
                e.printStackTrace();
            }
        } else {
            MinecraftServer server = user.getServer();
            // 获取服务器命令调度程序
            CommandDispatcher<CommandSourceStack> dispatcher = server.getCommands().getDispatcher();
            try {
                // 解析指令并获取命令源
                ParseResults<CommandSourceStack> parseResults
                        = dispatcher.parse("tick freeze", server.createCommandSourceStack());
                // 执行指令
                dispatcher.execute(parseResults);

                // 在控制台输出提示信息
            } catch (CommandSyntaxException e) {
                // 指令语法异常处理
                e.printStackTrace();
            }
        }
    }
}
