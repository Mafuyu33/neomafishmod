package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.knockback;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin extends BlockBehaviour implements ItemLike, net.neoforged.neoforge.common.extensions.IBlockExtension  {

	public BlockMixin(Properties properties) {
		super(properties);
	}

	@Inject(at = @At("HEAD"), method = "stepOn")
	private void init(Level level, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
		int k = BlockEnchantmentStorage.getLevel(Enchantments.KNOCKBACK,pos);
		if (!level.isClientSide() && k > 0) {//如果有击退附魔
			entity.push(0,k*0.5,0);
		}
		if (level.isClientSide() && k > 0 && entity instanceof Player player) {//如果有击退附魔
			player.push(0,k*0.5,0);
		}
	}
}