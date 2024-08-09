package com.mafuyu33.neomafishmod.entity;

import com.mafuyu33.neomafishmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class FuProjectileEntity extends ThrowableItemProjectile {
    public FuProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public FuProjectileEntity(LivingEntity livingEntity,Level world){
        super(ModEntities.STONE_PROJECTILE.get(),livingEntity,world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.STONE_BALL.get();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        Level level = level();
        BlockPos blockPos = result.getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);
        boolean isWoodenBlock  = blockState.is(BlockTags.LOGS);

        if(!this.level().isClientSide){
            level.broadcastEntityEvent(this,(byte)3);
            if(isWoodenBlock){
                level.removeBlock(blockPos,false);
                spawnAtLocation(blockState.getBlock());
            }
            ItemStack itemStack = this.getItem();
            ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), itemStack);
            itemEntity.setThrower(getOwner());
            this.level().addFreshEntity(itemEntity);
        }
        this.discard();
        super.onHitBlock(result);
    }
    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!this.level().isClientSide){
            Entity entity = result.getEntity();
            Entity owner = this.getOwner();
            entity.hurt(this.damageSources().thrown(this,owner),4f);
            ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(),this.getItem());
            itemEntity.setThrower(getOwner());
            this.level().addFreshEntity(itemEntity);
            this.discard();
        }
    }


}
