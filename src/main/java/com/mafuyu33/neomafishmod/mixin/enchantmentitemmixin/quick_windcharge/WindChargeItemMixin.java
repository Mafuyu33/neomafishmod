package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.quick_windcharge;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantmentHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WindChargeItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WindChargeItem.class)
public abstract class WindChargeItemMixin {
	@Inject(method = "use",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;)V"))
	private void init(Level p_326306_, Player p_326042_, InteractionHand p_326470_, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
		if(p_326042_.getItemInHand(p_326470_).is(Items.WIND_CHARGE) //是风弹并且有快速装填，取消延迟
				&& ModEnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE,p_326042_.getItemInHand(p_326470_))>0){
			p_326042_.getCooldowns().removeCooldown((Item) (Object)this);
		}
	}
	@Inject(method = "use",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"))
	private void init1(Level p_326306_, Player p_326042_, InteractionHand p_326470_, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
		if(p_326042_.getItemInHand(p_326470_).is(Items.WIND_CHARGE) //是风弹并且有无限
				&& ModEnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY,p_326042_.getItemInHand(p_326470_))>0){
			if(!p_326042_.isCreative()) {//不是创造
				int count = p_326042_.getItemInHand(p_326470_).getCount();
				p_326042_.getItemInHand(p_326470_).setCount(count + 1);
			}
		}
	}
	@Inject(method = "use",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
	private void init2(Level p_326306_, Player p_326042_, InteractionHand p_326470_, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
		int i = ModEnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT,p_326042_.getItemInHand(p_326470_));
		if(p_326042_.getItemInHand(p_326470_).is(Items.WIND_CHARGE) && i>0){ //是风弹并且有多重射击
			for(int k = 0; k < i + 2; k++){//抛出i+2个风弹
				WindCharge windcharge = new WindCharge(p_326042_, p_326306_, p_326042_.position().x(), p_326042_.getEyePosition().y(), p_326042_.position().z());
				windcharge.shootFromRotation(p_326042_, p_326042_.getXRot(), p_326042_.getYRot(), 0.0F, 1.5F, 3.0F);
				p_326306_.addFreshEntity(windcharge);
			}
		}
	}
	@Inject(method = "use",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/windcharge/WindCharge;shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"),cancellable = true)
	private void init3(Level p_326306_, Player p_326042_, InteractionHand p_326470_, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
		ItemStack itemstack = p_326042_.getItemInHand(p_326470_);
		if(itemstack.is(Items.WIND_CHARGE) //绑定诅咒
				&& ModEnchantmentHelper.getEnchantmentLevel(Enchantments.BINDING_CURSE,p_326042_.getItemInHand(p_326470_))>0){

			int i = ModEnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT,p_326042_.getItemInHand(p_326470_));
			if(p_326042_.getItemInHand(p_326470_).is(Items.WIND_CHARGE) && i>0){ //是风弹并且有多重射击
				for(int k = 0; k < i + 3; k++){//抛出i+3个风弹
					WindCharge windcharge = new WindCharge(p_326042_, p_326306_, p_326042_.position().x(), p_326042_.getEyePosition().y(), p_326042_.position().z());
					windcharge.shootFromRotation(p_326042_, p_326042_.getXRot(), p_326042_.getYRot(), 0.0F, 0F, 0.0F);
					p_326306_.addFreshEntity(windcharge);
				}
				cir.setReturnValue(InteractionResultHolder.sidedSuccess(itemstack, p_326306_.isClientSide()));
			}else {
				WindCharge windcharge = new WindCharge(p_326042_, p_326306_, p_326042_.position().x(), p_326042_.getEyePosition().y(), p_326042_.position().z());
				windcharge.shootFromRotation(p_326042_, p_326042_.getXRot(), p_326042_.getYRot(), 0.0F, 0F, 0.0F);
				p_326306_.addFreshEntity(windcharge);
				cir.setReturnValue(InteractionResultHolder.sidedSuccess(itemstack, p_326306_.isClientSide()));
			}
		}
	}
}