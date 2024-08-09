package com.mafuyu33.neomafishmod.mixin.itemmixin;

import com.mafuyu33.neomafishmod.Config;
import com.mafuyu33.neomafishmod.item.custom.ColliableItem;
import com.mafuyu33.neomafishmod.mixinhelper.ShieldDashMixinHelper;
import com.mafuyu33.neomafishmod.network.packet.C2S.ShieldDashC2SPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class ShieldDashMixin extends Entity implements Attackable {

    public ShieldDashMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract Iterable<ItemStack> getArmorSlots();

    @Shadow public abstract boolean hurt(DamageSource source, float amount);

    @Shadow public abstract boolean isBlocking();

    @Shadow public abstract boolean isAlive();

    @Unique
    int shieldDashCoolDown = 0;


    @Override
    public boolean canBeCollidedWith() {
        return ColliableItem.isColliable();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        boolean isShieldDashable = Config.isShieldDashable();
        if (isShieldDashable){
            if (level().isClientSide && this.isBlocking()
                    && ShieldDashMixinHelper.isAttackKeyPressed() && shieldDashCoolDown <= 0) {//盾牌猛击冲刺部分
                // 获取玩家当前面朝的方向
                Vec3 playerLookDirection = this.getLookAngle().normalize();

                if (playerLookDirection.y < 0) {// 如果玩家面朝的是 y 轴负方向，则将 y 分量设为零
                    playerLookDirection = new Vec3(playerLookDirection.x, 0.0, playerLookDirection.z).normalize();
                } else {// 如果玩家面朝的是 y 轴正方向，则将 y 分量减小
                    playerLookDirection = new Vec3(playerLookDirection.x, playerLookDirection.y * 0.3, playerLookDirection.z).normalize();
                }
                // 设置速度大小，例如 0.1 表示速度的大小为 0.1 个单位
                double speed = 2;
                // 乘以速度系数
                playerLookDirection = playerLookDirection.scale(speed);
                // 给玩家应用速度
                this.addDeltaMovement(playerLookDirection);
                level().playSound(this, this.blockPosition(),
                        SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1f, 1f);
                shieldDashCoolDown = 20;
            }
            if (level().isClientSide && shieldDashCoolDown > 0) {//盾牌猛击内置冷却部分，传递数据包到服务端
                shieldDashCoolDown--;
//            System.out.println(shieldDashCoolDown);

                sentC2S();
            }
            if (this.isAlwaysTicking() && isBlocking()) {//盾牌猛击造成伤害和击退部分
                if (checkPlayerCollisions((Player) (Object) this) != null) {
                    Entity entity = checkPlayerCollisions((Player) (Object) this);
//                System.out.println(ShieldDashMixinHelper.getHitCoolDown(this.getId()));
                    if (ShieldDashMixinHelper.getHitCoolDown(this.getId()) >= 15) {//盾牌冲刺中
                        Vec3 playerLookDirection = this.getLookAngle().normalize();
                        playerLookDirection = new Vec3(playerLookDirection.x, 0, playerLookDirection.z).normalize();
                        double speed = 0.5;
                        // 乘以速度系数
                        Vec3 upVector = new Vec3(0, 0.1, 0);
                        playerLookDirection = playerLookDirection.scale(speed).add(upVector);
                        entity.hurt(damageSources().playerAttack((Player) (Object) this), 5f);
                        entity.addDeltaMovement(playerLookDirection);
                    }
                }
            }
        }
    }

    @Unique
    @OnlyIn(Dist.CLIENT)
    private void sentC2S(){
//        PacketByteBuf buf = PacketByteBufs.create();//传输到服务端
//        buf.writeInt(shieldDashCoolDown);
//        ClientPlayNetworking.send(ModMessages.Shield_Dash_ID, buf);
        PacketDistributor.sendToServer(new ShieldDashC2SPacket(shieldDashCoolDown));
    }
    @Unique
    private Entity checkPlayerCollisions(Player player) {

        Level world = player.level();
        AABB collisionBox = player.getBoundingBox().inflate(2.0); // 检测范围为玩家周围 2 格的立方体

        // 获取与玩家碰撞的所有实体
        for (Entity entity : world.getEntities(player, collisionBox)) {
            // 确保碰撞到的实体不是玩家自身，并且是生物实体
            if (entity != player && entity instanceof LivingEntity) {
                return entity;
//                System.out.println("储存生物id");
//                System.out.println("HitCoolDown"+ ShieldDashMixinHelper.getHitCoolDown(entity.getId()));
//                System.out.println("isHit"+ ShieldDashMixinHelper.getEntityValue(entity.getId()));
            }
        }
        return null;
    }
}
