package com.mafuyu33.neomafishmod.entity.custom;

import com.mafuyu33.neomafishmod.entity.ModEntities;
import com.mafuyu33.neomafishmod.item.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class StoneBallProjectileEntity extends ThrowableItemProjectile {
    public StoneBallProjectileEntity(EntityType<? extends StoneBallProjectileEntity> entityType, Level world) {
        super(entityType, world);
    }

    public StoneBallProjectileEntity(LivingEntity livingEntity, Level world) {
        super(ModEntities.STONE_PROJECTILE.get(), livingEntity, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.STONE_BALL.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!this.level().isClientSide){
            Entity entity = result.getEntity();
            Entity owner = this.getOwner();
            entity.hurt(this.damageSources().thrown(this,owner),6f);
            if (entity instanceof LivingEntity){
                // todo enchantment effect
            }
            this.discard();
        }
    }
}