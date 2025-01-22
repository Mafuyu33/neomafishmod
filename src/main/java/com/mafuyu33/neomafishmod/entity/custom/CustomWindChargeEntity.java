package com.mafuyu33.neomafishmod.entity.custom;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.entity.ModEntities;
import com.mafuyu33.neomafishmod.network.packet.S2C.CustomWindChargeS2CPacket;
import com.mafuyu33.neomafishmod.network.packet.S2C.WindChargeStormS2CPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CustomWindChargeEntity extends AbstractWindCharge {
    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR;
    public int radius = 1;

    public CustomWindChargeEntity(EntityType<? extends AbstractWindCharge> entityType, Level level) {
        super(entityType, level);
    }

    public CustomWindChargeEntity(Player player, Level level, double x, double y, double z) {
        super(ModEntities.CUSTOM_WIND_CHARGE.get(), level, player, x, y, z);
    }

    @Override
    public void onExplosionHit(@Nullable Entity entity) {
        super.onExplosionHit(entity);
    }

    public void tick() {
        super.tick();
        if (radius==1){
            // 持续减速
            double decelerationFactor = 0.8; // 每次tick减速的比例，接近1.0表示缓慢减速，越小减速越快
            this.setDeltaMovement(this.getDeltaMovement().multiply(decelerationFactor, decelerationFactor, decelerationFactor));
        }
        if (radius > 4) {
            // 定义一个半径为 `radius` 的范围，用于检测实体
            double attractionRadius = 0.3125 * radius + 1.5; // 吸引半径，可以根据需要调整

            AABB boundingBox = new AABB(
                    this.getX() - attractionRadius, this.getY() - attractionRadius, this.getZ() - attractionRadius,
                    this.getX() + attractionRadius, this.getY() + attractionRadius, this.getZ() + attractionRadius
            );

            // 获取该范围内的所有实体
            List<Entity> nearbyEntities = this.level().getEntitiesOfClass(Entity.class, boundingBox);

            // 吸引每个实体到中心附近，并使其沿逆时针方向旋转
            for (Entity entity : nearbyEntities) {
                // 排除自身
                if (entity == this) continue;

                // 排除创造模式和观察者模式的玩家
                if (entity instanceof Player player) {
                    if (player.isCreative() || player.isSpectator()) {
                        continue;
                    }
                }

                // 计算从实体到中心的方向向量
                Vec3 toCenter = new Vec3(this.getX() - entity.getX(), this.getY() - entity.getY(), this.getZ() - entity.getZ());
                double distance = toCenter.length();

                // 归一化方向向量，得到单位方向向量
                Vec3 normalizedToCenter = toCenter.normalize();

                // 指向中心的速度，速度大小与距离的倒数成正比，越靠近中心速度越大
                double attractionSpeed;
                double tangentialSpeed;
                if(entity instanceof CustomWindChargeEntity) {
                    // 这个系数可以根据需要调整
                    attractionSpeed = 2.5 / distance;
                    tangentialSpeed = 0.1 / distance;
                }else {
                    // 这个系数可以根据需要调整
                    attractionSpeed = 0.3 * 0.25 * radius / distance;
                    tangentialSpeed = 0.3 * 0.09375 * radius / distance;
                }
                Vec3 attractionVelocity = normalizedToCenter.scale(attractionSpeed);

                // 计算切向速度，沿逆时针方向。切向方向为(toCenter.z, 0, -toCenter.x)
                Vec3 tangentialDirection = new Vec3(toCenter.z, 0, -toCenter.x).normalize();
                Vec3 tangentialVelocity = tangentialDirection.scale(tangentialSpeed);

                // 抵消重力的速度，保持一个固定的正方向的速度
                Vec3 gravityCounterVelocity = new Vec3(0, entity.getGravity()/3, 0); // 这个值可以根据需要调整

                // 获取实体当前的速度矢量
                Vec3 currentVelocity = entity.getDeltaMovement().multiply(0.7,0.7,0.7);

                // 计算最终的速度矢量
                Vec3 finalVelocity = currentVelocity.add(attractionVelocity).add(tangentialVelocity).add(gravityCounterVelocity);

                // 定义最大速度
                double maxSpeed = 30; // 你可以根据需要调整最大速度的大小

                // 检查速度的大小是否超过最大速度
                if (finalVelocity.length() > maxSpeed) {
                    // 将速度缩放到最大速度
                    NeoMafishMod.LOGGER.info("finalVelocity="+ finalVelocity);
                    finalVelocity = finalVelocity.normalize().scale(maxSpeed);
                }


                // 设置实体的运动矢量
                entity.setDeltaMovement(finalVelocity);

                //S2C发包！同步客户端的运动矢量
                PacketDistributor.sendToAllPlayers(new WindChargeStormS2CPacket(entity.getId(),finalVelocity));
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        if(entity instanceof CustomWindChargeEntity){//如果是自己
            //半径相加
            ((CustomWindChargeEntity) entity).radius = ((CustomWindChargeEntity) entity).radius + this.radius;
            //发包给客户端
            PacketDistributor.sendToAllPlayers(new CustomWindChargeS2CPacket(entity.getUUID(),((CustomWindChargeEntity) entity).radius));
            this.discard();
        }else {
            super.onHitEntity(result);
        }
    }

    protected void explode(Vec3 pos) {
        this.level().explode(this, (DamageSource)null, EXPLOSION_DAMAGE_CALCULATOR, pos.x(), pos.y(), pos.z(), radius, false, Level.ExplosionInteraction.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE, SoundEvents.WIND_CHARGE_BURST);
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
