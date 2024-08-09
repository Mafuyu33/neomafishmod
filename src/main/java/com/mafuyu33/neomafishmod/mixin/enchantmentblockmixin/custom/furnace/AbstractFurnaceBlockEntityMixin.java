package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.furnace;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends BaseContainerBlockEntity implements WorldlyContainer, RecipeCraftingHolder, StackedContentsCompatible {
	protected AbstractFurnaceBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, Boolean isFortune) {
		super(blockEntityType, blockPos, blockState);
	}
	@Inject(at = @At("HEAD"), method = "getBurnDuration",cancellable = true)
	private void init1(ItemStack fuel, CallbackInfoReturnable<Integer> cir) {
		int k = BlockEnchantmentStorage.getLevel(Enchantments.FIRE_ASPECT,getBlockPos());
		if(k>0){//火焰附加
			BlockPos firePos =getBlockPos();
			firePos = firePos.offset(0,1,0);
			if(level!=null && !level.isClientSide) {
				level.setBlock(firePos, Blocks.FIRE.defaultBlockState(), 3);
			}
			cir.setReturnValue(-1);
		}
	}



	@Inject(method = "burn",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;grow(I)V"))//时运烧矿
	private static void init2(RegistryAccess registryAccess, RecipeHolder<?> recipe, NonNullList<ItemStack> inventory, int maxStackSize, AbstractFurnaceBlockEntity furnace, CallbackInfoReturnable<Boolean> cir) {
		ItemStack itemStack = (ItemStack)inventory.get(0);
		int k = InjectHelper.getEnchantmentLevel(itemStack,Enchantments.FORTUNE);
		if (k > 0) {
			ItemStack itemStack3 = (ItemStack)inventory.get(2);
			itemStack3.grow(k);
		}
	}
	@Inject(method = "burn",at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;",ordinal = 0))
	private static void init3(RegistryAccess registryAccess, RecipeHolder<?> recipe, NonNullList<ItemStack> inventory, int maxStackSize, AbstractFurnaceBlockEntity furnace, CallbackInfoReturnable<Boolean> cir) {
		ItemStack itemStack = (ItemStack)inventory.get(0);
		int k = InjectHelper.getEnchantmentLevel(itemStack, Enchantments.FORTUNE);
		if (k > 0) {
			ItemStack itemStack3 = (ItemStack)inventory.get(2);
			itemStack3.grow(k);
		}
	}

}