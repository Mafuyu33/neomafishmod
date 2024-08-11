package com.mafuyu33.neomafishmod.entity;

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
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public class CustomWindChargeEntity  extends AbstractWindCharge {
    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR;
    private static final float RADIUS = 1.2F;
    private int noDeflectTicks = 5;

    public CustomWindChargeEntity(EntityType<? extends AbstractWindCharge> entityType, Level level) {
        super(entityType, level);
    }

    public CustomWindChargeEntity(Player player, Level level, double x, double y, double z) {
        super(EntityType.WIND_CHARGE, level, player, x, y, z);
    }

    public void tick() {
        super.tick();
        if (this.noDeflectTicks > 0) {
            --this.noDeflectTicks;
        }

    }

    public boolean deflect(ProjectileDeflection deflection, @Nullable Entity entity, @Nullable Entity owner, boolean deflectedByPlayer) {
        return this.noDeflectTicks > 0 ? false : super.deflect(deflection, entity, owner, deflectedByPlayer);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        //未完成
    }

    protected void explode(Vec3 pos) {
        this.level().explode(this, (DamageSource)null, EXPLOSION_DAMAGE_CALCULATOR, pos.x(), pos.y(), pos.z(), 1.2F, false, Level.ExplosionInteraction.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE, SoundEvents.WIND_CHARGE_BURST);
    }

    static {
        EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(true, false, Optional.of(1.22F), BuiltInRegistries.BLOCK.getTag(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()));
    }
}
