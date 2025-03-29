package com.mafuyu33.neomafishmod.entity.custom;

import com.mafuyu33.neomafishmod.entity.ModEntities;
import com.mafuyu33.neomafishmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class DiamondProjectileEntity extends ThrowableItemProjectile {
    public DiamondProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public DiamondProjectileEntity(LivingEntity livingEntity, Level level) {
        super(ModEntities.STONE_PROJECTILE.get(), level);
        this.setOwner(livingEntity);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.STONE_BALL.get();
    }

    private int bounceCount = 0;
    private long lastBounceTime = 0; // 用于记录上一次增加计数的时间

    @Override
    public void tick() {
        super.tick();
        Level level = this.level();
        BlockPos blockPos = this.blockPosition();
        FluidState fluidState = level.getFluidState(blockPos);
        long currentTime = System.currentTimeMillis();

        boolean isInWater = false;

        if (fluidState.is(FluidTags.WATER)) {
            isInWater = true;
            long timeSinceLastBounce = currentTime - lastBounceTime;

            if (timeSinceLastBounce > 200) {
                bounceCount++;
                this.setDeltaMovement(this.getDeltaMovement().multiply(1, -1, 1));
                if (getOwner() instanceof Player player) {
                    player.displayClientMessage(Component.literal(bounceCount + "次"), true);
                }
            }
            lastBounceTime = currentTime;

        } else {
            isInWater = false;
        }

        if (isInWater) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.95, 1, 0.95));
        }

        Vec3 deltaMovement = this.getDeltaMovement();
        double horizontalSpeed = Math.sqrt(deltaMovement.x * deltaMovement.x + deltaMovement.z * deltaMovement.z);

        if (bounceCount != 0 & (horizontalSpeed < 0.095 || this.getDeltaMovement().y > 0.5)) {
            spawnDiamondItem();
            this.discard();
            bounceCount = 0;
            if (getOwner() instanceof Player player) {
                player.displayClientMessage(Component.literal("沉了(╯‵□′)╯"), true);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        spawnDiamondItem();
        this.discard();
        super.onHitBlock(result);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        result.getEntity().hurt(this.damageSources().thrown(result.getEntity(), getOwner()), 10);
        spawnDiamondItem();
        this.discard();
    }

    private void spawnDiamondItem() {
        if (this.level() instanceof ServerLevel serverLevel) {
            ItemEntity itemEntity = new ItemEntity(serverLevel, this.getX(), this.getY(), this.getZ(), new ItemStack(Items.DIAMOND));
            serverLevel.addFreshEntity(itemEntity);
        }
    }
}