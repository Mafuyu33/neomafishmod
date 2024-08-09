package com.mafuyu33.neomafishmod.network.packet.C2S;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class FuC2SPacket  implements CustomPacketPayload{
    static Vec3 direction;
    static UUID uuid;


    public Vec3 directionMessage;
    public  UUID uuidMessage;
    public int flag;
    public static void receive(FuC2SPacket data) {
        // Everything here happens ONLY on the Server!
        // Do whatever processing you need here, then send a packet to the client to inform them of the game mode change.
        int flag = data.flag;
        if(flag==1) {
            direction = data.directionMessage;
        }else if(flag==2) {
            uuid = data.uuidMessage;
        }
//      itemStack.addVelocity(direction.x * horizontalSpeed, 0.3, direction.z * horizontalSpeed);
    }
    public static Vec3 getDirection(){
        return direction;
    }
    public static UUID getUuid(){
        if(uuid == null){
            return null;
        }else {
            return uuid;
        }
    }
    public static final Type<FuC2SPacket> TYPE = new Type<FuC2SPacket>(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,"fu_c2s"));
    public static final StreamCodec<FriendlyByteBuf,FuC2SPacket> STREAM_CODEC =
            CustomPacketPayload.codec(FuC2SPacket::write,FuC2SPacket::new);

    private  FuC2SPacket(FriendlyByteBuf buf) {
        this.flag = buf.readInt();
        if (flag == 1){
            this.directionMessage = buf.readVec3();
        }else if (flag == 2){
            this.uuidMessage = buf.readUUID();
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(flag);
        if (flag==1){
            buf.writeVec3(directionMessage);
        }else if (flag==2){
            buf.writeUUID(this.uuidMessage);
        }
    }

    public FuC2SPacket(int flag,UUID uuid,Vec3 directionMessage) {
        this.flag =flag;
        this.uuidMessage = uuid;
        this.directionMessage = directionMessage;
    }

    public static void handle(final FuC2SPacket data, final IPayloadContext context){
        context.enqueueWork(()->{
            receive(data);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
