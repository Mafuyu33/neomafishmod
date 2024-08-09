package com.mafuyu33.neomafishmod.network.packet.C2S;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class GameOptionsC2SPacket implements CustomPacketPayload{
    public static float blocks;
    public static float master;


    public float blocksMessage;
    public float masterMessage;


    public static final Type<GameOptionsC2SPacket> TYPE = new Type<GameOptionsC2SPacket>(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,"game_options"));
    public static final StreamCodec<FriendlyByteBuf,GameOptionsC2SPacket> STREAM_CODEC =
            CustomPacketPayload.codec(GameOptionsC2SPacket::write,GameOptionsC2SPacket::new);

    private void write(FriendlyByteBuf buf) {
        buf.writeFloat(this.blocksMessage);
        buf.writeFloat(this.masterMessage);
    }

    public GameOptionsC2SPacket(FriendlyByteBuf buf) {
        this.blocksMessage = buf.readFloat();
        this.masterMessage = buf.readFloat();
    }

    public GameOptionsC2SPacket(float blocksMessage, float masterMessage) {
        this.blocksMessage = blocksMessage;
        this.masterMessage = masterMessage;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static void handle(final GameOptionsC2SPacket data, final IPayloadContext context){
        context.enqueueWork(()->{
            GameOptionsC2SPacket.blocks = data.blocksMessage;
            GameOptionsC2SPacket.master = data.masterMessage;
        });
    }
}
