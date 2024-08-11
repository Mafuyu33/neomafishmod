package com.mafuyu33.neomafishmod.entity.custom;

import com.mafuyu33.neomafishmod.entity.ModEntities;
import com.mafuyu33.neomafishmod.item.ModItems;
import com.mafuyu33.neomafishmod.render.CustomParticleRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class LightningProjectileEntity extends ThrowableItemProjectile {
    public LightningProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public LightningProjectileEntity(LivingEntity livingEntity, Level level) {
        super(ModEntities.LIGHTNING_PROJECTILE.get(), livingEntity, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.LIGHTNING_ITEM.get();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity p_entity) {
        return super.getAddEntityPacket(p_entity);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!this.level().isClientSide){
            this.level().broadcastEntityEvent(this,(byte)3);
            lightning(result.getBlockPos());
            CustomParticleRenderer.spawnFlameParticles(result.getLocation());
        }
        this.discard();
        super.onHitBlock(result);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!this.level().isClientSide){
            lightning(result.getEntity().blockPosition());
            CustomParticleRenderer.spawnFlameParticles(result.getLocation());
        }
        this.discard();
        super.onHitEntity(result);
    }

    private void lightning(BlockPos blockPos) {
        LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(this.level());
        if (lightningBolt!=null){
            lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPos));
            this.level().addFreshEntity(lightningBolt);
            Holder<SoundEvent> tridentThunder = SoundEvents.TRIDENT_THUNDER;
            this.playSound(tridentThunder.value());
        }

    }
}
