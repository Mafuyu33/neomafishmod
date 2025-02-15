package com.mafuyu33.neomafishmod.network.packet.S2C;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityVelocityUpdateS2CPacket implements CustomPacketPayload {
    public static Type<EntityVelocityUpdateS2CPacket> TYPE =
            new Type<EntityVelocityUpdateS2CPacket>(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,"entity_velocity_update_s2c"));

    // stream codec
    public static final StreamCodec<FriendlyByteBuf, EntityVelocityUpdateS2CPacket> STREAM_CODEC =
            CustomPacketPayload.codec(EntityVelocityUpdateS2CPacket::write, EntityVelocityUpdateS2CPacket::new);
    public Vec3 finalVelocity;
    public int id;

    public EntityVelocityUpdateS2CPacket(int id, Vec3 finalVelocity){
        this.id=id;
        this.finalVelocity=finalVelocity;
    }

    public EntityVelocityUpdateS2CPacket(FriendlyByteBuf buf){
        this.id=buf.readInt();
        this.finalVelocity=buf.readVec3();
    }

    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(this.id);
        pBuffer.writeVec3(this.finalVelocity);
    }
    public static void handle(EntityVelocityUpdateS2CPacket data, IPayloadContext context){
        runEnqueue(data, context);
    }


    private static void runEnqueue(EntityVelocityUpdateS2CPacket data, IPayloadContext context) {
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
