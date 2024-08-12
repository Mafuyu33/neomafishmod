package com.mafuyu33.neomafishmod.network.packet.S2C;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WindChargeStormS2CPacket implements CustomPacketPayload {
    public static Type<WindChargeStormS2CPacket> TYPE =
            new Type<WindChargeStormS2CPacket>(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,"wind_charge_storm"));

    // stream codec
    public static final StreamCodec<FriendlyByteBuf, WindChargeStormS2CPacket> STREAM_CODEC =
            CustomPacketPayload.codec(WindChargeStormS2CPacket::write, WindChargeStormS2CPacket::new);
    public Vec3 finalVelocity;
    public int id;

    public WindChargeStormS2CPacket(int id,Vec3 finalVelocity){
        this.id=id;
        this.finalVelocity=finalVelocity;
    }

    public WindChargeStormS2CPacket(FriendlyByteBuf buf){
        this.id=buf.readInt();
        this.finalVelocity=buf.readVec3();
    }

    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(this.id);
        pBuffer.writeVec3(this.finalVelocity);
    }
    public static void handle(WindChargeStormS2CPacket data, IPayloadContext context){

        context.enqueueWork(()->{
            if(data.id!=-1 && Minecraft.getInstance().level!=null) {
                Entity entity = Minecraft.getInstance().level.getEntity(data.id);
                if(entity!=null) {
                    entity.setDeltaMovement(data.finalVelocity);
                }
            }
        });
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static class WindChargeStormData {
        private static final Map<Integer, Vec3> dataMap = new ConcurrentHashMap<>();

        public static void set(int newid, Vec3 newVec3) {
            dataMap.put(newid, newVec3);
        }

        public static Vec3 getVec3(int id) {
            return dataMap.getOrDefault(id, new Vec3(0,0,0));
        }
    }
}
