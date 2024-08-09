package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.tnt;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(TntBlock.class)
public abstract class TntBlockMixin extends Block {

    @Shadow
    @Deprecated
    protected static void explode(Level level, BlockPos pos, @Nullable LivingEntity entity) {

    }

    public TntBlockMixin(Properties properties) {
		super(properties);
	}


	/**
	 * @author
	 * Mafuyu33
	 * @reason
	 * infinite explosion
	 */
	@Overwrite
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		int k = BlockEnchantmentStorage.getLevel(Enchantments.INFINITY,pos);
		ItemStack itemStack = player.getItemInHand(hand);
		if (!itemStack.is(Items.FLINT_AND_STEEL) && !itemStack.is(Items.FIRE_CHARGE)) {
			return super.useItemOn(stack,state, level, pos, player, hand, hitResult);
		} else {
			explode(level, pos, player);
			if(k==0) {
				level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);//删除TNT
			}
			Item item = itemStack.getItem();
			if (!player.isCreative()) {
				if (itemStack.is(Items.FLINT_AND_STEEL)) {
//					itemStack.hurtAndBreak(1, player, (playerx) -> {
//						playerx.sendToolBreakStatus(hand);
//					});
					itemStack.hurtAndBreak(1,player, Objects.requireNonNull(itemStack.getEquipmentSlot()));
				} else {
					itemStack.shrink(1);
				}
			}

			player.awardStat(Stats.ITEM_USED.get(item));
			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	/**
	 * @author
	 * Mafuyu33
	 * @reason
	 * infinite explosion
	 */

	@Overwrite
	public void wasExploded(Level world, BlockPos pos, Explosion explosion) {
		if (!world.isClientSide) {
			int k = BlockEnchantmentStorage.getLevel(Enchantments.INFINITY,pos);
			if(k>0){
				ListTag enchantments = BlockEnchantmentStorage.getEnchantmentsAtPosition(pos); // 获取物品栈上的附魔信息列表
				BlockEnchantmentStorage.addBlockEnchantment(pos,enchantments);// 将附魔信息列表存储
				world.setBlock(pos, Blocks.TNT.defaultBlockState(), 16);//添加TNT
			}
			PrimedTnt tntEntity = new PrimedTnt(world, (double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5, explosion.getIndirectSourceEntity());
			int i = tntEntity.getFuse();
			tntEntity.setFuse((short)(world.random.nextInt(i / 4) + i / 8));
			world.addFreshEntity(tntEntity);
		}
	}

	/**
	 * @author
	 * Mafuyu33
	 * @reason
	 * infinite explosion
	 */
	@Overwrite
	public void onProjectileHit(Level world, BlockState state, BlockHitResult hit, Projectile projectile) {
		if (!world.isClientSide) {
			BlockPos blockPos = hit.getBlockPos();
			int k = BlockEnchantmentStorage.getLevel(Enchantments.INFINITY,blockPos);
			Entity entity = projectile.getOwner();
			if (projectile.isOnFire() && projectile.mayInteract(world, blockPos)) {
				explode(world, blockPos, entity instanceof LivingEntity ? (LivingEntity)entity : null);
				if(k==0) {
					world.removeBlock(blockPos, false);
				}
			}
		}

	}
}