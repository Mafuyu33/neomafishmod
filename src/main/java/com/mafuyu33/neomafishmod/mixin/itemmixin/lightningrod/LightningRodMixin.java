package com.mafuyu33.neomafishmod.mixin.itemmixin.lightningrod;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningRodBlock.class)
public abstract class LightningRodMixin {

    @Inject(method = "updateNeighbours", at = @At("HEAD"))
    private void init(BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
        Player user = level.getNearestPlayer(pos.getX(),pos.getY(),pos.getZ(),10f,true);
        int k = BlockEnchantmentStorage.getLevel(Enchantments.CHANNELING, pos);
        if (k > 0) {
            LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(level);
            if (lightningEntity != null) {
                lightningEntity.moveTo(Vec3.atBottomCenterOf(pos));
                lightningEntity.setCause(user instanceof ServerPlayer ? (ServerPlayer) user : null);
                level.addFreshEntity(lightningEntity);
                SoundEvent soundEvent = SoundEvents.TRIDENT_THUNDER.value();
                level.playSound(user,pos,soundEvent, SoundSource.BLOCKS,5, 1.0F);
            }
        }
    }
}
