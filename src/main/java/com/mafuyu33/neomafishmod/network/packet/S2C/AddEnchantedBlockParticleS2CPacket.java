package com.mafuyu33.neomafishmod.network.packet.S2C;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.spongepowered.asm.mixin.Unique;

public class AddEnchantedBlockParticleS2CPacket implements CustomPacketPayload {
    public static Type<AddEnchantedBlockParticleS2CPacket> TYPE =
            new Type<AddEnchantedBlockParticleS2CPacket>(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,"add_enchanted_block_particle"));

    // stream codec
    public static final StreamCodec<FriendlyByteBuf, AddEnchantedBlockParticleS2CPacket> STREAM_CODEC =
            CustomPacketPayload.codec(AddEnchantedBlockParticleS2CPacket::write, AddEnchantedBlockParticleS2CPacket::new);
    public BlockPos blockPos;

    public AddEnchantedBlockParticleS2CPacket( BlockPos blockPos){
        this.blockPos=blockPos;
    }

    public AddEnchantedBlockParticleS2CPacket(FriendlyByteBuf buf){
        this.blockPos=buf.readBlockPos();
    }

    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeBlockPos(this.blockPos);
    }
    public static void handle(AddEnchantedBlockParticleS2CPacket data, IPayloadContext context){
        runEnqueue(data, context);
    }


    private static void runEnqueue(AddEnchantedBlockParticleS2CPacket data, IPayloadContext context) {
        context.enqueueWork(()->{
            neomafishmod$addParticles(data.blockPos);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    @Unique
    private static void neomafishmod$addParticles(BlockPos blockPos) {
        // 在方块顶部创建粒子效果
        Minecraft.getInstance().level.addParticle(ParticleTypes.COMPOSTER,
                blockPos.getX() + 0.5,
                blockPos.getY() + 1.1,
                blockPos.getZ() + 0.5,
                0.0, 0.0, 0.0);

        // 在方块底部创建粒子效果
        Minecraft.getInstance().level.addParticle(ParticleTypes.COMPOSTER,
                blockPos.getX() + 0.5,
                blockPos.getY() - 0.1,
                blockPos.getZ() + 0.5,
                0.0, 0.0, 0.0);

        // 在方块北侧创建粒子效果
        Minecraft.getInstance().level.addParticle(ParticleTypes.COMPOSTER,
                blockPos.getX() + 0.5,
                blockPos.getY() + 0.5,
                blockPos.getZ() - 0.1,
                0.0, 0.0, 0.0);

        // 在方块南侧创建粒子效果
        Minecraft.getInstance().level.addParticle(ParticleTypes.COMPOSTER,
                blockPos.getX() + 0.5,
                blockPos.getY() + 0.5,
                blockPos.getZ() + 1.1,
                0.0, 0.0, 0.0);

        // 在方块西侧创建粒子效果
        Minecraft.getInstance().level.addParticle(ParticleTypes.COMPOSTER,
                blockPos.getX() - 0.1,
                blockPos.getY() + 0.5,
                blockPos.getZ() + 0.5,
                0.0, 0.0, 0.0);

        // 在方块东侧创建粒子效果
        Minecraft.getInstance().level.addParticle(ParticleTypes.COMPOSTER,
                blockPos.getX() + 1.1,
                blockPos.getY() + 0.5,
                blockPos.getZ() + 0.5,
                0.0, 0.0, 0.0);
    }
}
