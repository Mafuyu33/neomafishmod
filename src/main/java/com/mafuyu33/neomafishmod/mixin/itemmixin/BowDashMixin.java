package com.mafuyu33.neomafishmod.mixin.itemmixin;

import com.mafuyu33.neomafishmod.Config;
import com.mafuyu33.neomafishmod.mixinhelper.BowDashMixinHelper;
import com.mafuyu33.neomafishmod.network.packet.C2S.BowDashC2SPacket;
import com.mafuyu33.neomafishmod.sound.ModSounds;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class BowDashMixin extends LivingEntity {
    protected BowDashMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract void tick();

    @Unique
    int BowDashCoolDown = 0;

    @Inject(at = @At("HEAD"), method = "tick")
    private void init1(CallbackInfo ci) {
        boolean isBowDashable = Config.isBowDashable();
        if (isBowDashable){
            if (level().isClientSide && this.isHolding(Items.BOW) && this.isUsingItem()
                    && BowDashMixinHelper.isAttackKeyPressed() && BowDashCoolDown <= 0) {//弓箭手突击
                // 获取玩家的水平朝向角度（角度值）
                Vec3 velocity = this.getDeltaMovement();
                // 投影到水平平面上
                Vec3 horizontalMotion = new Vec3(velocity.x, 0, velocity.z);
                // 将水平移动向量标准化
                if (horizontalMotion.lengthSqr() > 0) {
                    horizontalMotion = horizontalMotion.normalize();
                }
                float amp = 2;
                this.push(amp * horizontalMotion.x, 0.14, amp * horizontalMotion.z);

                sendC2S();

                System.out.println("突进");

                level().playSound(this, this.blockPosition(),
                        ModSounds.DASH_SOUND.value(), SoundSource.PLAYERS, 1f, 1f);
                BowDashCoolDown = 20;

            }
            if (level().isClientSide && BowDashCoolDown > 0) {//弓箭手突击内置冷却部分，传递数据包到服务端
                BowDashCoolDown--;
//            System.out.println(BowDashCoolDown);
                sendC2S();
            }


            if (!level().isClientSide && BowDashMixinHelper.getHitCoolDown(this.getId()) != 0) {//时间变慢部分
                MinecraftServer server = this.getServer();
                if (server != null) {
                    if (BowDashMixinHelper.getHitCoolDown(this.getId()) >= 12) {
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
                            ParseResults<CommandSourceStack> parseResults
                                    = dispatcher.parse("tick rate 6", server.createCommandSourceStack());
                            // 执行指令
                            dispatcher.execute(parseResults);

                            // 在控制台输出提示信息
                        } catch (CommandSyntaxException e) {
                            // 指令语法异常处理
                            e.printStackTrace();
                        }
                        try {
                            // 解析指令并获取命令源
                            ParseResults<CommandSourceStack> parseResults
                                    = dispatcher.parse("effect give @p minecraft:slowness infinite 3 true", server.createCommandSourceStack());
                            // 执行指令
                            dispatcher.execute(parseResults);

                            // 在控制台输出提示信息
                        } catch (CommandSyntaxException e) {
                            // 指令语法异常处理
                            e.printStackTrace();
                        }
                        try {
                            // 解析指令并获取命令源
                            ParseResults<CommandSourceStack> parseResults
                                    = dispatcher.parse("effect give @p minecraft:night_vision infinite 0 true", server.createCommandSourceStack());
                            // 执行指令
                            dispatcher.execute(parseResults);

                            // 在控制台输出提示信息
                        } catch (CommandSyntaxException e) {
                            // 指令语法异常处理
                            e.printStackTrace();
                        }
                    } else {
                        // 获取服务器命令调度程序
                        CommandDispatcher<CommandSourceStack> dispatcher = server.getCommands().getDispatcher();
                        try {
                            // 解析指令并获取命令源
                            ParseResults<CommandSourceStack> parseResults
                                    = dispatcher.parse("tick rate 20", server.createCommandSourceStack());
                            // 执行指令
                            dispatcher.execute(parseResults);

                            // 在控制台输出提示信息
                        } catch (CommandSyntaxException e) {
                            // 指令语法异常处理
                            e.printStackTrace();
                        }
                        try {
                            // 解析指令并获取命令源
                            ParseResults<CommandSourceStack> parseResults
                                    = dispatcher.parse("effect clear @p minecraft:slowness", server.createCommandSourceStack());
                            // 执行指令
                            dispatcher.execute(parseResults);

                            // 在控制台输出提示信息
                        } catch (CommandSyntaxException e) {
                            // 指令语法异常处理
                            e.printStackTrace();
                        }
                        try {
                            // 解析指令并获取命令源
                            ParseResults<CommandSourceStack> parseResults
                                    = dispatcher.parse("effect clear @p minecraft:night_vision", server.createCommandSourceStack());
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
        }
    }
    @Unique
    @OnlyIn(Dist.CLIENT)
    private void sendC2S(){
//        PacketByteBuf buf = PacketByteBufs.create();//传输到服务端
//        buf.writeInt(BowDashCoolDown);
//        ClientPlayNetworking.send(ModMessages.Bow_Dash_ID, buf);
        PacketDistributor.sendToServer(new BowDashC2SPacket(BowDashCoolDown));
    }
}