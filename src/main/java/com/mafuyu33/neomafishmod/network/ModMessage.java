package com.mafuyu33.neomafishmod.network;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.network.packet.C2S.*;
import com.mafuyu33.neomafishmod.network.packet.S2C.BellSoundS2CPacket;
import com.mafuyu33.neomafishmod.network.packet.S2C.CustomWindChargeS2CPacket;
import com.mafuyu33.neomafishmod.network.packet.S2C.NeverGonnaS2CPacket;
import com.mafuyu33.neomafishmod.network.packet.S2C.WindChargeStormS2CPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.checkerframework.checker.units.qual.N;

@EventBusSubscriber(modid = NeoMafishMod.MODID,bus = EventBusSubscriber.Bus.MOD)
public class ModMessage {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(NeoMafishMod.MODID);
        registrar.playBidirectional(
                ThrowPowerC2SPacket.TYPE,
                ThrowPowerC2SPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        ThrowPowerC2SPacket::handle
                )
        );


        registrar.playBidirectional(
                SheepBreedingC2SPacket.TYPE,
                SheepBreedingC2SPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        SheepBreedingC2SPacket::handle
                )
        );

        registrar.playBidirectional(
                GameOptionsC2SPacket.TYPE,
                GameOptionsC2SPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<GameOptionsC2SPacket>(
                        null,
                        GameOptionsC2SPacket::handle
                )
        );

        registrar.playBidirectional(
                FuC2SPacket.TYPE,
                FuC2SPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        FuC2SPacket::handle
                )
        );

        registrar.playBidirectional(
                ShieldDashC2SPacket.TYPE,
                ShieldDashC2SPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        ShieldDashC2SPacket::handle
                )
        );



        registrar.playBidirectional(
                BowDashC2SPacket.TYPE,
                BowDashC2SPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        BowDashC2SPacket::receive
                )
        );


        // server to client

        registrar.playBidirectional(
                BellSoundS2CPacket.TYPE,
                BellSoundS2CPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        BellSoundS2CPacket::handle,
                        null
                )
        );

        registrar.playBidirectional(
                NeverGonnaS2CPacket.TYPE,
                NeverGonnaS2CPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        NeverGonnaS2CPacket::handle,
                        null
                )
        );

        registrar.playBidirectional(
                CustomWindChargeS2CPacket.TYPE,
                CustomWindChargeS2CPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        CustomWindChargeS2CPacket::handle,
                        null
                )
        );
        registrar.playBidirectional(
                WindChargeStormS2CPacket.TYPE,
                WindChargeStormS2CPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        WindChargeStormS2CPacket::handle,
                        null
                )
        );
        registrar.playBidirectional(
                PlayerActionC2SPacket.TYPE,
                PlayerActionC2SPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        PlayerActionC2SPacket::handle
                )
        );
    }
}
