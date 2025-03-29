package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.windcharge;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantmentHelper;
import com.mafuyu33.neomafishmod.entity.custom.CustomWindChargeEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WindChargeItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge.EXPLOSION_DAMAGE_CALCULATOR;

@Mixin(WindChargeItem.class)
public abstract class WindChargeItemMixin {
	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;)V"))
	private void init(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
		if(player.getItemInHand(hand).is(Items.WIND_CHARGE) //是风弹并且有快速装填，取消延迟
				&& ModEnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE, player.getItemInHand(hand)) > 0){
			// 获取风弹物品的注册ID
			ResourceLocation windChargeId = BuiltInRegistries.ITEM.getKey(Items.WIND_CHARGE);
			// 使用正确的方法移除冷却
			player.getCooldowns().removeCooldown(windChargeId);
		}
	}

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"))
	private void init1(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
		if(player.getItemInHand(hand).is(Items.WIND_CHARGE) //是风弹并且有无限
				&& ModEnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, player.getItemInHand(hand)) > 0){
			if(!player.isCreative()) { //不是创造
				int count = player.getItemInHand(hand).getCount();
				player.getItemInHand(hand).setCount(count + 1);
			}
		}
	}

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectileFromRotation(Lnet/minecraft/world/entity/projectile/Projectile$ProjectileFactory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;FFF)Lnet/minecraft/world/entity/projectile/Projectile;"))
	private void init2(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
		int i = ModEnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT, player.getItemInHand(hand));
		if(player.getItemInHand(hand).is(Items.WIND_CHARGE) && i > 0){ //是风弹并且有多重射击
			for(int k = 0; k < i + 2; k++){ //抛出i+2个风弹
				WindCharge windcharge = new WindCharge(player, level, player.position().x(), player.getEyePosition().y(), player.position().z());
				windcharge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 3.0F);
				level.addFreshEntity(windcharge);
			}
		}
	}

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectileFromRotation(Lnet/minecraft/world/entity/projectile/Projectile$ProjectileFactory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;FFF)Lnet/minecraft/world/entity/projectile/Projectile;"), cancellable = true)
	private void init3(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
		ItemStack itemstack = player.getItemInHand(hand);
		if(itemstack.is(Items.WIND_CHARGE) //风爆，合成大风弹
				&& ModEnchantmentHelper.getEnchantmentLevel(Enchantments.WIND_BURST, itemstack) > 0){

			int i = ModEnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT, itemstack);
			if(itemstack.is(Items.WIND_CHARGE) && i > 0){ //是风弹并且有多重射击
				for(int k = 0; k < i + 3; k++){ //抛出i+3个风弹
					CustomWindChargeEntity customWindCharge = new CustomWindChargeEntity(player, level, player.position().x(), player.getEyePosition().y(), player.position().z());
					customWindCharge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 0.0F);
					level.addFreshEntity(customWindCharge);
				}
				neomafishmod$setCooldown(level, player, itemstack);
				cir.setReturnValue(level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
			} else {
				CustomWindChargeEntity customWindCharge = new CustomWindChargeEntity(player, level, player.position().x(), player.getEyePosition().y(), player.position().z());
				customWindCharge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 0.0F);
				level.addFreshEntity(customWindCharge);
				neomafishmod$setCooldown(level, player, itemstack);
				cir.setReturnValue(level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
			}
		}
	}

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectileFromRotation(Lnet/minecraft/world/entity/projectile/Projectile$ProjectileFactory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;FFF)Lnet/minecraft/world/entity/projectile/Projectile;"), cancellable = true)
	private void init4(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
		ItemStack itemstack = player.getItemInHand(hand);
		if(itemstack.is(Items.WIND_CHARGE) //忠诚附魔，原地爆炸
				&& ModEnchantmentHelper.getEnchantmentLevel(Enchantments.LOYALTY, itemstack) > 0){

			Vec3 lookVec = Vec3.directionFromRotation(player.getRotationVector());
			double distance = 2d;
			// 将朝向向量乘以所需的距离，以确定爆炸生成的位置
			double offsetX = lookVec.x * distance;
			double offsetY = lookVec.y * distance;
			double offsetZ = lookVec.z * distance;
			Vec3 explorePos = new Vec3(player.getX() + offsetX, player.getY() + offsetY + 1.625, player.getZ() + offsetZ);

			int i = ModEnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT, itemstack);
			if(itemstack.is(Items.WIND_CHARGE) && i > 0){ //是风弹并且有多重射击
				for(int k = 0; k < i + 3; k++){ //抛出i+3个风弹
					neomafishmod$explode(explorePos, level, 0.3f);
				}
				neomafishmod$setCooldown(level, player, itemstack);
				cir.setReturnValue(level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
			} else {
				neomafishmod$explode(explorePos, level, 0.3f);
				neomafishmod$setCooldown(level, player, itemstack);
				cir.setReturnValue(level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
			}
		}
	}

	@Unique
	private void neomafishmod$setCooldown(Level level, Player player, ItemStack itemstack) {
		level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.WIND_CHARGE_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
		// 获取当前风弹物品的注册ID
		ResourceLocation windChargeId = BuiltInRegistries.ITEM.getKey((Item)(Object)this);
		// 使用正确的方法添加冷却
		player.getCooldowns().addCooldown(windChargeId, 10);
		player.awardStat(Stats.ITEM_USED.get((WindChargeItem)(Object)this));
		itemstack.consume(1, player);
	}

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectileFromRotation(Lnet/minecraft/world/entity/projectile/Projectile$ProjectileFactory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;FFF)Lnet/minecraft/world/entity/projectile/Projectile;"), cancellable = true)
	private void init5(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
		ItemStack itemstack = player.getItemInHand(hand);
		if(itemstack.is(Items.WIND_CHARGE) //绑定诅咒
				&& ModEnchantmentHelper.getEnchantmentLevel(Enchantments.BINDING_CURSE, itemstack) > 0){

			int i = ModEnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT, itemstack);
			if(itemstack.is(Items.WIND_CHARGE) && i > 0){ //是风弹并且有多重射击
				for(int k = 0; k < i + 3; k++){ //抛出i+3个风弹
					WindCharge windcharge = new WindCharge(player, level, player.position().x(), player.getEyePosition().y(), player.position().z());
					windcharge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0F, 0.0F);
					level.addFreshEntity(windcharge);
				}

				neomafishmod$setCooldown(level, player, itemstack);
				cir.setReturnValue(level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
			} else {
				WindCharge windcharge = new WindCharge(player, level, player.position().x(), player.getEyePosition().y(), player.position().z());
				windcharge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0F, 0.0F);
				level.addFreshEntity(windcharge);

				neomafishmod$setCooldown(level, player, itemstack);
				cir.setReturnValue(level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
			}
		}
	}

	@Unique
	protected void neomafishmod$explode(Vec3 pos, Level level, float radius) {
		level.explode(null, (DamageSource)null, EXPLOSION_DAMAGE_CALCULATOR, pos.x(), pos.y(), pos.z(), radius, false, Level.ExplosionInteraction.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_SMALL, SoundEvents.WIND_CHARGE_BURST);
	}
}