package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.bowloyalty;

import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BowItem.class)
public abstract class BowItemMixin extends ProjectileWeaponItem {
	public BowItemMixin(Properties properties) {
		super(properties);
	}

//	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"), method = "onStoppedUsing")
//	private void init(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci, @Local(ordinal = 0) PersistentProjectileEntity persistentProjectileEntity) {
//		int o = EnchantmentHelper.getLevel(Enchantments.LOYALTY, stack);
//		if(o>0) {
//			// Set loyalty level
//
//			persistentProjectileEntity.getDataTracker().set(LOYALTY, (byte)EnchantmentHelper.getLoyalty(stack));
//		}
}