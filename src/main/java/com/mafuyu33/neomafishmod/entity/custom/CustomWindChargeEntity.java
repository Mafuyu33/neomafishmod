package com.mafuyu33.neomafishmod.entity.custom;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.entity.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public class CustomWindChargeEntity extends AbstractWindCharge {
    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR;
    private static final float RADIUS = 1.2F;
    private int noDeflectTicks = 5;

    public CustomWindChargeEntity(EntityType<? extends AbstractWindCharge> entityType, Level level) {
        super(entityType, level);
    }

    public CustomWindChargeEntity(Player player, Level level, double x, double y, double z) {
        super(ModEntities.CUSTOM_WIND_CHARGE.get(), level, player, x, y, z);
    }

    public void tick() {
        super.tick();

        // 持续减速
        double decelerationFactor = 0.8; // 每次tick减速的比例，接近1.0表示缓慢减速，越小减速越快
        this.setDeltaMovement(this.getDeltaMovement().multiply(decelerationFactor, decelerationFactor, decelerationFactor));


        if (this.noDeflectTicks > 0) {
            --this.noDeflectTicks;
        }

    }

    public boolean deflect(ProjectileDeflection deflection, @Nullable Entity entity, @Nullable Entity owner, boolean deflectedByPlayer) {
        return this.noDeflectTicks > 0 ? false : super.deflect(deflection, entity, owner, deflectedByPlayer);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        NeoMafishMod.LOGGER.info("BoundingBox Size: {}", this.getBoundingBox().getSize());
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        if(entity instanceof CustomWindChargeEntity){//如果是自己
            entity.setDeltaMovement(0,0,0);
            entity.setBoundingBox(entity.getBoundingBox().inflate(0.1));
            entity.refreshDimensions();
            //速度变为0，碰撞箱变大
        }else {
            super.onHitEntity(result);
        }
    }

    protected void explode(Vec3 pos) {
        this.level().explode(this, (DamageSource)null, EXPLOSION_DAMAGE_CALCULATOR, pos.x(), pos.y(), pos.z(), 1.2F, false, Level.ExplosionInteraction.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE, SoundEvents.WIND_CHARGE_BURST);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return true;
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return true;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return true;
    }

    static {
        EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(true, false, Optional.of(1.22F), BuiltInRegistries.BLOCK.getTag(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()));
    }
}
