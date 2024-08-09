package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.bowloyalty;

import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class PersistentProjectileEntityMixin extends Projectile {

	protected PersistentProjectileEntityMixin(EntityType<? extends Projectile> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow public abstract ItemStack getPickupItemStackOrigin();
	@Shadow
	protected boolean inGround;

	@Shadow public abstract void setNoPhysics(boolean noClip);

	@Shadow public abstract boolean isNoPhysics();

	@Shadow public AbstractArrow.Pickup pickup;

	@Shadow protected abstract ItemStack getPickupItem();

	@Unique
	private static EntityDataAccessor<Byte> LOYALTY;
	@Unique
	public int returnTimer;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;isNoPhysics()Z"), method = "tick")
	private void init(CallbackInfo ci) {

		Entity entity = this.getOwner();
		int loyaltyLevel = getLoyaltyFromBow(entity);
		if (loyaltyLevel > 0 && this.entityData.get(LOYALTY)==0) {
			this.entityData.set(LOYALTY, (byte) loyaltyLevel);
		}
		loyaltyLevel = this.entityData.get(LOYALTY);
		if (loyaltyLevel > 0 && (this.inGround || this.isNoPhysics()) && entity != null) {
			if (!this.isOwnerAlive()) {
				if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
					this.spawnAtLocation(this.getPickupItem(), 0.1F);
				}
				this.discard();
			} else {
				this.setNoPhysics(true);
				Vec3 vec3d = entity.getEyePosition().subtract(this.position());
				this.setPos(this.getX(), this.getY() + vec3d.y * 0.015 * (double)loyaltyLevel, this.getZ());
				if (this.level().isClientSide) {
					this.yOld = this.getY();
				}

				double d = 0.05 * (double)loyaltyLevel;
				this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec3d.normalize().scale(d)));
				if (this.returnTimer == 0) {
					this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
				}

				++this.returnTimer;
			}
		}
	}

	@Inject(at = @At("TAIL"), method = "<clinit>")
	private static void init1(CallbackInfo ci) {
		LOYALTY = SynchedEntityData.defineId(PersistentProjectileEntityMixin.class, EntityDataSerializers.BYTE);
	}

	@Inject(at = @At("TAIL"), method = "defineSynchedData")
	private void init2(SynchedEntityData.Builder builder,CallbackInfo ci) {
//		this.entityData.defineLOYALTY, (byte)0);
		builder.define(LOYALTY,(byte) 0 );
	}
	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V")
	private void init3(EntityType entityType, LivingEntity owner, Level level, ItemStack pickupItemStack, ItemStack firedFromWeapon, CallbackInfo ci) {
		this.entityData.set(LOYALTY, (byte) InjectHelper.getEnchantmentLevel(firedFromWeapon, Enchantments.LOYALTY));
	}
	@Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
	private void init4(CompoundTag compound, CallbackInfo ci) {
		this.entityData.set(LOYALTY, (byte)InjectHelper.getEnchantmentLevel(getPickupItem(),Enchantments.LOYALTY));
	}
	@Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
	private void init5(CompoundTag compound, CallbackInfo ci) {
		compound.putByte("Loyalty", this.entityData.get(LOYALTY));
	}

	@Unique
	private int getLoyaltyFromBow(Entity entity) {
		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			ItemStack mainHandItem = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);
			ItemStack offHandItem = livingEntity.getItemInHand(InteractionHand.OFF_HAND);

			if (mainHandItem.getItem() == Items.BOW) {
				return InjectHelper.getEnchantmentLevel(mainHandItem,Enchantments.LOYALTY);
			}

			if (offHandItem.getItem() == Items.BOW) {
				return InjectHelper.getEnchantmentLevel(mainHandItem,Enchantments.LOYALTY);
			}
		}
		return 0;
	}
	@Unique
	private boolean isOwnerAlive() {
		Entity entity = this.getOwner();
		if (entity != null && entity.isAlive()) {
			return !(entity instanceof ServerPlayer) || !entity.isSpectator();
		} else {
			return false;
		}
	}
}