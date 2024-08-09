package com.mafuyu33.neomafishmod.network.packet.C2S;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SheepBreedingC2SPacket implements CustomPacketPayload {
    static int times;
    int timesMessage;

    public static final Type<SheepBreedingC2SPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,"sheep_breeding"));
    public static final StreamCodec<FriendlyByteBuf,SheepBreedingC2SPacket> STREAM_CODEC =
            CustomPacketPayload.codec(SheepBreedingC2SPacket::write,SheepBreedingC2SPacket::new);

    private void write(FriendlyByteBuf buf) {
        buf.writeInt(this.timesMessage);
    }

    public SheepBreedingC2SPacket(FriendlyByteBuf buf) {
        this.timesMessage = buf.readInt();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static int getTimes() {
        return times;
    }

    public static void handle(final SheepBreedingC2SPacket data, final IPayloadContext context){
        context.enqueueWork(()->{
            SheepBreedingC2SPacket.times = data.timesMessage;
        });
    }
}
