package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.redirect_trident;

import com.llamalad7.mixinextras.sugar.Local;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantmentHelper;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.event.enchantment.trident_redirect.OnPlayerLeftClick;
import com.mafuyu33.neomafishmod.network.packet.C2S.RedirectTridentC2SPacket;
import com.mafuyu33.neomafishmod.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

/**
 * @author Mafuyu33
 */
@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow{
    //todo:致密附魔三叉戟，飞行速度变慢，伤害提高。（这个三叉戟好沉阿！）
    @Shadow
    private boolean dealtDamage;
    //重定向的附魔等级
    @Unique
    private static final EntityDataAccessor<Byte> ID_REDIRECT = SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.BYTE);

    protected ThrownTridentMixin(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V",at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V"))
    private void init0(Level level, LivingEntity shooter, ItemStack pickupItemStack, CallbackInfo ci) {
        this.entityData.set(ID_REDIRECT, this.neomafishmod$getRedirectFromItem(pickupItemStack));
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V",at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V"))
    private void init1(Level level, double x, double y, double z, ItemStack pickupItemStack, CallbackInfo ci) {
        this.entityData.set(ID_REDIRECT, this.neomafishmod$getRedirectFromItem(pickupItemStack));
    }

    @Inject(method = "defineSynchedData",at = @At(value = "TAIL"))
    private void init2(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(ID_REDIRECT, (byte)0);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;get(Lnet/minecraft/network/syncher/EntityDataAccessor;)Ljava/lang/Object;"), method = "tick")
    private void init4(CallbackInfo ci) {
        Entity entity = this.getOwner();
        int k = (Byte)this.entityData.get(ID_REDIRECT);
        if (k > 0 && entity != null) {
            if((!this.dealtDamage && !this.isNoPhysics())){
                // 玩家点击左键
                if (OnPlayerLeftClick.onPlayerLeftClicked()) {
                    // 获取玩家视角方向的射线检测到的坐标
                    Vec3 lookVec = entity.getLookAngle();
                    Vec3 startVec = entity.getEyePosition();
                    // 射线长度100
                    Vec3 endVec = startVec.add(lookVec.scale(100));
                    HitResult hitResult = entity.level().clip(new ClipContext(startVec, endVec, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));

                    Vec3 targetPos = null;
                    if (hitResult.getType() == HitResult.Type.BLOCK || hitResult.getType() == HitResult.Type.ENTITY) {
                        targetPos = hitResult.getLocation();
                    } else {
                        // 设置三叉戟实体的速度朝向玩家视线方向
                        targetPos = startVec.add(lookVec.scale(10));
                    }

                    Vec3 direction = targetPos.subtract(this.position()).normalize();
                    Vec3 finalVelocity = direction.scale(k);
                    // 设置速度
                    this.setDeltaMovement(finalVelocity);
                    //C2S同步速度到服务端
                    PacketDistributor.sendToServer(new RedirectTridentC2SPacket(this.getId(), finalVelocity));

                    // 播放音效
//                    Minecraft.getInstance().level.playSound(Minecraft.getInstance().player, this.blockPosition(), SoundEvents.COPPER_BULB_TURN_OFF, SoundSource.PLAYERS, 3.0F, 1.0F);
                    // 生成粒子
//                    //todo:好看的粒子效果
//                    this.level().addParticle(ParticleTypes.DUST_PLUME, this.getX(), this.getY(), this.getZ(), direction.x, direction.y, direction.z);
                }
            }else{
                //重置重力
                this.setNoGravity(false);
                PacketDistributor.sendToServer(new RedirectTridentC2SPacket(this.getId(),Vec3.ZERO));
            }
        }
    }

    @Unique
    private byte neomafishmod$getRedirectFromItem(ItemStack stack) {
        Level var3 = this.level();
        byte var10000;
        if (var3 instanceof ServerLevel) {
            var10000 = (byte) ModEnchantmentHelper.getEnchantmentLevel(ModEnchantments.REDIRECT_PROJECTILE, stack);
        } else {
            var10000 = 0;
        }

        return var10000;
    }
}