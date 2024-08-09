package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.enderpearl;

import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEnderpearl.class)
public class EnderPearlEntityMixin extends ThrowableItemProjectile {

	public EnderPearlEntityMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
		super(entityType, level);
	}

	public EnderPearlEntityMixin(EntityType<? extends ThrowableItemProjectile> entityType, double x, double y, double z, Level level) {
		super(entityType, x, y, z, level);
	}

	public EnderPearlEntityMixin(EntityType<? extends ThrowableItemProjectile> entityType, LivingEntity shooter, Level level) {
		super(entityType, shooter, level);
	}

	@Override
	public Item getDefaultItem() {
		return Items.ENDER_PEARL;
	}

	@Inject(at = @At("HEAD"), method = "onHit")
	private void init(HitResult result, CallbackInfo ci){
		Player playerEntity = ((Player) this.getOwner());
		if (playerEntity != null) {
			InteractionHand hand = playerEntity.getUsedItemHand();
			if (hand != null) {
				ItemStack itemStack = playerEntity.getItemInHand(hand);
				if (itemStack.getItem() == Items.ENDER_PEARL) {
					int k = InjectHelper.getEnchantmentLevel(itemStack, Enchantments.CHANNELING);
					if (k > 0) {
						BlockPos blockPos = this.blockPosition();
						LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(this.level());
						if (lightningEntity != null) {
							lightningEntity.moveTo(Vec3.atBottomCenterOf(blockPos));
							lightningEntity.setCause(playerEntity instanceof ServerPlayer ? (ServerPlayer) playerEntity : null);
							this.level().addFreshEntity(lightningEntity);
							Holder<SoundEvent> tridentThunder = SoundEvents.TRIDENT_THUNDER;
							this.playSound(tridentThunder.value(), 5, 1.0F);
						}
					}
				}
			}
		}
	}
}