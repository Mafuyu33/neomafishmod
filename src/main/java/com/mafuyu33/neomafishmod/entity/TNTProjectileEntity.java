package com.mafuyu33.neomafishmod.entity;

import com.mafuyu33.neomafishmod.item.ModItems;
import com.mafuyu33.neomafishmod.render.CustomParticleRenderer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class TNTProjectileEntity extends ThrowableItemProjectile {
    public TNTProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public TNTProjectileEntity(LivingEntity livingEntity, Level world){
        super(ModEntities.TNT_PROJECTILE.get(),livingEntity,world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.TNT_BALL.get();
    }


    @Override
    protected void onHitBlock(BlockHitResult result) {

        if(!this.level().isClientSide()) {
//            this.level().sendEntityStatus(this, (byte)3);
            explode();
            CustomParticleRenderer.spawnFlameParticles(result.getLocation());
        }

        this.discard();
        super.onHitBlock(result);
    }


    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
    }


    private void explode(){
        float f = 4.0F;
        this.level().explode(this,this.getX(),this.getY(0.0625),this.getZ(),f,Level.ExplosionInteraction.TNT);
    }
}
