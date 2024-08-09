package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.enderpearl;

import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EnderpearlItem.class)
public class EnderPearlItemMixin extends Item {

	public EnderPearlItemMixin(Properties properties) {
		super(properties);
	}

	/**
	 * @author
	 * Mafuyu33
	 * @reason
	 * 重写
	 */
	@Overwrite
	public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		level.playSound((Player)null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENDER_PEARL_THROW,
				SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

		//快速装填
		int k = InjectHelper.getEnchantmentLevel(itemStack, Enchantments.QUICK_CHARGE);
		if (k <= 0) {
			user.getCooldowns().addCooldown(this, 20);
		}

		if (!level.isClientSide) {
			ThrownEnderpearl enderPearlEntity = new ThrownEnderpearl(level, user);
			enderPearlEntity.setItem(itemStack);
			int n = InjectHelper.getEnchantmentLevel(itemStack, Enchantments.VANISHING_CURSE);
//			int m = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, itemStack);
//			int j = EnchantmentHelper.getLevel(Enchantments., itemStack);

			if( n > 0 ){
				enderPearlEntity.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.5F, 1.0F);
				level.addFreshEntity(enderPearlEntity);

				enderPearlEntity.discard();
			}

//			else if( m > 0 ){
//				int numPearlsToThrow = 5; // 设置要丢出的末影珍珠数量
//				for(int i=0; i<numPearlsToThrow; i++){
//					enderPearlEntity.setVelocity(user, user.getPitch(), user.getYaw(), i, 1.5F, i+1);
//					world.spawnEntity(enderPearlEntity);
//				}
//			}
//			else if( j > 0 ){
//				j = 10;
//				for(int i=0; i<j; i++){
//					enderPearlEntity.setVelocity(user, user.getPitch(), user.getYaw(), i, 1.5F, i+1);
//					world.spawnEntity(enderPearlEntity);
//				}
//			}
			else {
				enderPearlEntity.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.5F, 1.0F);
				level.addFreshEntity(enderPearlEntity);
			}
		}

		user.awardStat(Stats.ITEM_USED.get(this));

		if (!user.getAbilities().invulnerable & InjectHelper.getEnchantmentLevel(itemStack, Enchantments.INFINITY) < 1) {//无限
			itemStack.shrink(1);
		}

		return  InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
	}

}