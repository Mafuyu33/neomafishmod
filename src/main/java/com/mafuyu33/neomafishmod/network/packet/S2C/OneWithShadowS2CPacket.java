package com.mafuyu33.neomafishmod.network.packet.S2C;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;

public class OneWithShadowS2CPacket implements CustomPacketPayload {
    public static Type<OneWithShadowS2CPacket> TYPE =
            new Type<OneWithShadowS2CPacket>(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,"one_with_shadow"));

    // stream codec
    public static final StreamCodec<FriendlyByteBuf, OneWithShadowS2CPacket> STREAM_CODEC =
            CustomPacketPayload.codec(OneWithShadowS2CPacket::write, OneWithShadowS2CPacket::new);

    private static final Map<Integer, Integer> ID_FLAG_MAP = new HashMap<>();
    private static int id;
    private static int flag;

    public OneWithShadowS2CPacket(int id,int flag){
        OneWithShadowS2CPacket.id = id;
        OneWithShadowS2CPacket.flag = flag;
        ID_FLAG_MAP.put(id, flag);
    }

    public OneWithShadowS2CPacket(FriendlyByteBuf buf){
        id = buf.readInt();
        flag = buf.readInt();
        ID_FLAG_MAP.put(id, flag);
    }

    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(id);
        pBuffer.writeInt(flag);
    }
    public static void handle(OneWithShadowS2CPacket data, IPayloadContext context){
        context.enqueueWork(()-> {
            ID_FLAG_MAP.put(id, flag);
        });
    }

    public static int getFlagById(int id) {
        return ID_FLAG_MAP.getOrDefault(id, -1);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
