package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.targetblock;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TargetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TargetBlock.class)
public abstract class TargetBlockMixin {
	@Inject(at = @At("HEAD"), method = "onProjectileHit",cancellable = true)
	private void init(Level level, BlockState state, BlockHitResult hit, Projectile projectile, CallbackInfo ci) {

		int k = BlockEnchantmentStorage.getLevel(Enchantments.PROJECTILE_PROTECTION,hit.getBlockPos());
		if(k>0 && !level.isClientSide){
			Entity entity = projectile.getOwner();
			if(entity instanceof LivingEntity livingEntity) {
				double d = livingEntity.getX() - projectile.getX();
				double e = livingEntity.getY() - projectile.getY();
				double f = livingEntity.getZ() - projectile.getZ();
			// 创建物品实体并设置位置
			Projectile newProjectileEntity = new Arrow(level,  projectile.getX(), projectile.getY(), projectile.getZ() ,new ItemStack(Items.ARROW),null);
			newProjectileEntity.setDeltaMovement(d * 0.1*1.3, e * 0.1 + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08*1.3, f * 0.1*1.3);

			// 将物品实体添加到世界中
			projectile.discard();
			level.addFreshEntity(newProjectileEntity);
		}else {
			Projectile newProjectileEntity = new Arrow(level, projectile.getX(), projectile.getY(), projectile.getZ(),new ItemStack(Items.ARROW),null);
			newProjectileEntity.setPos(projectile.getX(), projectile.getY(), projectile.getZ());
			newProjectileEntity.setDeltaMovement(0,0,0);
			// 将物品实体添加到世界中
			projectile.discard();
			level.addFreshEntity(newProjectileEntity);
		}
			ci.cancel();
		}
	}
}