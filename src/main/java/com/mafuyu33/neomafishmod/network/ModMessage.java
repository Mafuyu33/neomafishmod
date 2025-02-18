package com.mafuyu33.neomafishmod.network;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.network.packet.C2S.*;
import com.mafuyu33.neomafishmod.network.packet.S2C.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

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
        registrar.playBidirectional(
                RedirectTridentC2SPacket.TYPE,
                RedirectTridentC2SPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        RedirectTridentC2SPacket::handle
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
        registrar.playBidirectional(
                EntityVelocityUpdateC2SPacket.TYPE,
                EntityVelocityUpdateC2SPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        EntityVelocityUpdateC2SPacket::handle
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

//        registrar.playBidirectional(
//                NeverGonnaS2CPacket.TYPE,
//                NeverGonnaS2CPacket.STREAM_CODEC,
//                new DirectionalPayloadHandler<>(
//                        NeverGonnaS2CPacket::handle,
//                        null
//                )
//        );

        registrar.playBidirectional(
                CustomWindChargeS2CPacket.TYPE,
                CustomWindChargeS2CPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        CustomWindChargeS2CPacket::handle,
                        null
                )
        );
        registrar.playBidirectional(
                EntityVelocityUpdateS2CPacket.TYPE,
                EntityVelocityUpdateS2CPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        EntityVelocityUpdateS2CPacket::handle,
                        null
                )
        );
        registrar.playBidirectional(
                OneWithShadowS2CPacket.TYPE,
                OneWithShadowS2CPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        OneWithShadowS2CPacket::handle,
                        null
                )
        );

        registrar.playBidirectional(
                AddEnchantedBlockParticleS2CPacket.TYPE,
                AddEnchantedBlockParticleS2CPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        AddEnchantedBlockParticleS2CPacket::handle,
                        null
                )
        );
    }
}
