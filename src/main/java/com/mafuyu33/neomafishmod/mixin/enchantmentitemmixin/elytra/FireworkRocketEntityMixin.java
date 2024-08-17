package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.elytra;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantmentHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin extends Projectile {

	@Shadow
	@Nullable
	private LivingEntity attachedToEntity;

	@Shadow
	private int life;

	@Shadow
	private int lifetime;

    @Shadow
    private void explode() {
    }

    protected FireworkRocketEntityMixin(EntityType<? extends Projectile> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"), cancellable = true)
	private void init(CallbackInfo ci) {
		ItemStack itemstack = this.attachedToEntity.getItemBySlot(EquipmentSlot.CHEST);
		if(ModEnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY,itemstack)>0) {//鞘翅有无限附魔的时候。

			Vec3 vec31 = this.attachedToEntity.getLookAngle();  // 获取实体的视线方向
			double accelerationFactor = 0.1;  // 定义加速度的大小
			Vec3 currentVelocity = this.attachedToEntity.getDeltaMovement();  // 获取当前速度矢量

			// 添加加速度，直接在当前速度矢量上增加一个基于视线方向的固定矢量
			Vec3 newVelocity = currentVelocity.add(
					vec31.x * accelerationFactor,
					vec31.y * accelerationFactor,
					vec31.z * accelerationFactor
			);

			this.attachedToEntity.setDeltaMovement(newVelocity);  // 设置新的速度矢量
			ci.cancel();

			Vec3 vec3 = this.attachedToEntity.getHandHoldingItemAngle(Items.FIREWORK_ROCKET);
			this.setPos(this.attachedToEntity.getX() + vec3.x, this.attachedToEntity.getY() + vec3.y, this.attachedToEntity.getZ() + vec3.z);
			this.setDeltaMovement(this.attachedToEntity.getDeltaMovement());

			HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
			if (!this.noPhysics) {
				this.hitTargetOrDeflectSelf(hitresult);
				this.hasImpulse = true;
			}

			this.updateRotation();
			if (this.life == 0 && !this.isSilent()) {
				this.level().playSound((Player) null, this.getX(), this.getY(), this.getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.AMBIENT, 3.0F, 1.0F);
			}

			++this.life;
			if (this.level().isClientSide && this.life % 2 < 2) {
				this.level().addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, -this.getDeltaMovement().y * 0.5, this.random.nextGaussian() * 0.05);
			}

			if (!this.level().isClientSide && this.life > this.lifetime) {
				this.explode();
			}
		}
	}
}