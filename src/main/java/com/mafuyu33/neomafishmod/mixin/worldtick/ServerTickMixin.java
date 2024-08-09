package com.mafuyu33.neomafishmod.mixin.worldtick;

import com.mafuyu33.neomafishmod.mixinhelper.BellBlockDelayMixinHelper;
import com.mafuyu33.neomafishmod.network.packet.S2C.BellSoundS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class ServerTickMixin {

    @Shadow
    private PlayerList playerList;

    @Inject(at = @At("HEAD"), method = "tickServer")
    private void init(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        //获取存储BellBlockEntity的所有键的集合
        Set<BlockPos> bellBlockEntityKeys = BellBlockDelayMixinHelper.BellBlockEntityMap.keySet();
        // 遍历集合并执行操作
        for (BlockPos blockPos : bellBlockEntityKeys) {
            // 根据键从Map中获取对应的值
            BellBlockEntity bellBlockEntity = BellBlockDelayMixinHelper.getBellBlockEntity(blockPos);
            Direction direction = BellBlockDelayMixinHelper.getDirection(blockPos);
            int hitCoolDown = BellBlockDelayMixinHelper.getHitCoolDown(blockPos);

            if (hitCoolDown<20 && hitCoolDown>=0) {
                BellBlockDelayMixinHelper.storeHitCoolDown(blockPos,hitCoolDown+1);
            }

            if (hitCoolDown >= 20) {
                bellBlockEntity.onHit(direction);
//                FriendlyByteBuf buf = PacketByteBufs.create();//S2C
//                buf.writeInt(1);
//                buf.writeBlockPos(blockPos);
                String[] playerNames = playerList.getPlayerNamesArray();
                Player[] players = new Player[playerNames.length];

                for (int i = 0; i < playerNames.length; i++) {
                    Player player = playerList.getPlayerByName(playerNames[i]);
                    players[i] = player;
                }
//                for (Player player : players) {
//                    ServerPlayNetworking.send((ServerPlayerEntity) player, ModMessages.BELL_SOUND_ID, buf);
                PacketDistributor.sendToAllPlayers(new BellSoundS2CPacket(1,blockPos));
//                }
                BellBlockDelayMixinHelper.HitCoolDownMap.remove(blockPos);
                BellBlockDelayMixinHelper.DirectionMap.remove(blockPos);
                BellBlockDelayMixinHelper.BellBlockEntityMap.remove(blockPos);
            }
        }
    }
}
