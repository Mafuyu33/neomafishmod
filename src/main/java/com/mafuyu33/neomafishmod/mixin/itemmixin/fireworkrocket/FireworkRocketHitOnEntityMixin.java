package com.mafuyu33.neomafishmod.mixin.itemmixin.fireworkrocket;

import com.mafuyu33.neomafishmod.Config;
import com.mafuyu33.neomafishmod.mixinhelper.FireworkRocketEntityMixinHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class FireworkRocketHitOnEntityMixin extends Entity implements Attackable {
    @Shadow @Nullable public abstract LivingEntity getLastHurtByMob();

    @Shadow
    @Nullable
    private LivingEntity lastHurtMob;

    @Shadow public abstract void kill();

    @Shadow public abstract void stopSleeping();

    public FireworkRocketHitOnEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private static final double velocityY = 1.15; // Y轴方向速度，重力0.08
    @Unique
    private static final double INITIAL_VELOCITY = 0.3; // 初始速度
    @Unique
    private static double angle = Math.toRadians(Math.random() * 360); // 随机初始角度
    @Unique
    private static double velocityX = INITIAL_VELOCITY * Math.cos(angle); // 初始X轴速度
    @Unique
    private static double velocityZ = INITIAL_VELOCITY * Math.sin(angle); // 初始Z轴速度
    @Unique
    private int delayCounter = 0; // 延迟计数器
    @Unique
    int ParticleLifes=0;

    // 假设您想在特定事件发生后延迟5秒执行一些操作
    @Unique
    public void triggerDelayedAction() {
        this.delayCounter = 30; // 延迟5秒，假设游戏帧率是每秒20帧
    }
    @Unique
    private static void applyContinuousForce(Entity entity) {


        double velocityMagnitude = Math.sqrt(velocityX*velocityX+velocityZ*velocityZ);
        // 改变角度增量，以改变方向
        double angleIncrement = Math.toRadians(5); // 你可以根据需要调整增量
        angle += angleIncrement;

        // 更新合速度的方向，保持大小不变
        velocityX = velocityMagnitude * Math.cos(angle);
        velocityZ = velocityMagnitude * Math.sin(angle);

        entity.setDeltaMovement(velocityX, velocityY, velocityZ);
    }


    @Unique
    private void explode() {
        this.level().explode( null, this.getX(), this.getY(), this.getZ(),  2.5F, Level.ExplosionInteraction.TNT);
    }

    @Unique
    private void addParticles() {
        Vec3 Pos = this.position();
        Level world =level();
        world.addParticle(ParticleTypes.EXPLOSION,Pos.x,Pos.y,Pos.z,0,0,0);
    }


    @Inject(method = "tick", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        boolean isFireworkCanHitOnEntity = Config.isFireworkCanHitOnEntity();
        if(isFireworkCanHitOnEntity) {

            Vec3 Pos = null;
            int entityId = this.getId();// 获取实体的ID
            Entity entity = level().getEntity(entityId);
            int times = FireworkRocketEntityMixinHelper.getEntityValue(entityId);// 获取与实体ID关联的值
            if (entity != null) {
                Pos = entity.position();
            }
            if (times > 0) {
                applyContinuousForce(this);
                FireworkRocketEntityMixinHelper.storeEntityValue(entityId, times - 1);
            }
            if (times == 1) {
                ParticleLifes = 30;
                triggerDelayedAction();
            }


            if (ParticleLifes > 0 & entity != null) {
                addParticles();
                ParticleLifes--;
            }

            if (this.delayCounter > 0 && entity != null) {
                this.delayCounter--;
                if (this.delayCounter == 0) {
                    if (entity instanceof Player) {
                        entity.kill();
                        explode();
                    } else {
                        ((LivingEntity) entity).remove(RemovalReason.KILLED);
                        explode();
                        entity.discard();
                    }
                }
            }

        }
    }
}
