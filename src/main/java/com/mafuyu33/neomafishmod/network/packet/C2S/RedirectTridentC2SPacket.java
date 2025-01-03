package com.mafuyu33.neomafishmod.network.packet.C2S;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class RedirectTridentC2SPacket implements CustomPacketPayload {
    public static final Type<RedirectTridentC2SPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,"redirect_trident_c2s"));
    public Vec3 finalVelocity;
    public int id;
    public static final StreamCodec<FriendlyByteBuf, RedirectTridentC2SPacket> STREAM_CODEC =
            CustomPacketPayload.codec(RedirectTridentC2SPacket::write, RedirectTridentC2SPacket::new);
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public RedirectTridentC2SPacket(int id, Vec3 finalVelocity){
        this.finalVelocity = finalVelocity;
        this.id = id;
    }

    public RedirectTridentC2SPacket(FriendlyByteBuf buf){
        this.id = buf.readInt();
        this.finalVelocity = buf.readVec3();
    }

    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(this.id);
        pBuffer.writeVec3(this.finalVelocity);
    }
    public static void handle(final RedirectTridentC2SPacket data, final IPayloadContext context){
        context.enqueueWork(()->{
            if(data.id!=-1 && context.player().level()!=null) {
                Entity entity = context.player().level().getEntity(data.id);
                if(entity!=null) {
                    if(!data.finalVelocity.equals(Vec3.ZERO)) {
                        entity.setDeltaMovement(data.finalVelocity);
                    }else {
                        entity.setNoGravity(false);
                    }
                }
            }
        });
    }
}
