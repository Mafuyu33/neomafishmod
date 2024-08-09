package com.mafuyu33.neomafishmod.network.packet.S2C;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Random;

public class NeverGonnaS2CPacket implements CustomPacketPayload {
    public static Type<NeverGonnaS2CPacket> TYPE =
            new Type<NeverGonnaS2CPacket>(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,"never_gonna"));

    // stream codec
    public static final StreamCodec<FriendlyByteBuf, NeverGonnaS2CPacket> STREAM_CODEC =
            CustomPacketPayload.codec(NeverGonnaS2CPacket::write,NeverGonnaS2CPacket::new);



    public NeverGonnaS2CPacket(){
    }

    public NeverGonnaS2CPacket(FriendlyByteBuf buf){
    }

    public void write(FriendlyByteBuf pBuffer) {
    }
    public static void handle(NeverGonnaS2CPacket data, IPayloadContext context){

        context.enqueueWork(()->{
            playRandomSound(Minecraft.getInstance().level,Minecraft.getInstance().player);

        });
    }

    public static void playRandomSound(ClientLevel world, LocalPlayer player) {
        // 生成一个 0 到 9 之间的随机数
        int randomIndex = new Random().nextInt(10);

        // 根据随机数选择要执行的代码
        switch (randomIndex) {
            case 0:
                world.playSound(player, player.blockPosition(), ModSounds.NEVER1.value(), SoundSource.MASTER, 1.0f, 1.0f);
                break;
            case 1:
                world.playSound(player, player.blockPosition(), ModSounds.NEVER2.value(), SoundSource.MASTER, 1.0f, 1.0f);
                break;
            case 2:
                world.playSound(player, player.blockPosition(), ModSounds.NEVER3.value(), SoundSource.MASTER, 1.0f, 1.0f);
                break;
            case 3:
                world.playSound(player, player.blockPosition(), ModSounds.NEVER4.value(), SoundSource.MASTER, 1.0f, 1.0f);
                break;
            case 4:
                world.playSound(player, player.blockPosition(), ModSounds.NEVER5.value(), SoundSource.MASTER, 1.0f, 1.0f);
                break;
            case 5:
                world.playSound(player, player.blockPosition(), ModSounds.NEVER6.value(), SoundSource.MASTER, 1.0f, 1.0f);
                break;
            case 6:
                world.playSound(player, player.blockPosition(), ModSounds.NEVER7.value(), SoundSource.MASTER, 1.0f, 1.0f);
                break;
            case 7:
                world.playSound(player, player.blockPosition(), ModSounds.NEVER8.value(), SoundSource.MASTER, 1.0f, 1.0f);
                break;
            case 8:
                world.playSound(player, player.blockPosition(), ModSounds.NEVER9.value(), SoundSource.MASTER, 1.0f, 1.0f);
                break;
            case 9:
                world.playSound(player, player.blockPosition(), ModSounds.NEVER10.value(), SoundSource.MASTER, 1.0f, 1.0f);
                break;
        }
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
