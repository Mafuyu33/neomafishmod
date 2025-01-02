package com.mafuyu33.neomafishmod.network.packet.C2S;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PlayerActionC2SPacket implements CustomPacketPayload {
    private static int aimedEntityId;

    public static final Type<PlayerActionC2SPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,"player_attack_action_c2s"));
    public static final StreamCodec<FriendlyByteBuf,PlayerActionC2SPacket> STREAM_CODEC =
            CustomPacketPayload.codec(PlayerActionC2SPacket::write,PlayerActionC2SPacket::new);
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public PlayerActionC2SPacket(FriendlyByteBuf buf){
        aimedEntityId = buf.readInt();
    }

    public PlayerActionC2SPacket(int aimedEntityId){
        PlayerActionC2SPacket.aimedEntityId = aimedEntityId;
    }


    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(aimedEntityId);
    }
    public static void handle(final PlayerActionC2SPacket data, final IPayloadContext context){
            Entity nearestEntity = context.player().level().getEntity(aimedEntityId);
            // 将玩家瞬移到最近实体的附近
            Vec3 directionToEntity = nearestEntity.position().subtract(context.player().position()).normalize();
            Vec3 teleportPos = nearestEntity.position().subtract(directionToEntity.scale(2.0));
            context.player().teleportTo(teleportPos.x, teleportPos.y, teleportPos.z);

            // 对最近的实体造成等同于一次攻击的伤害
//            float attackDamage = (float) context.player().getAttribute(Attributes.ATTACK_DAMAGE).getValue();
//            nearestEntity.hurt(context.player().damageSources().playerAttack(context.player()), attackDamage);
            context.player().attack(nearestEntity);
    }
}
