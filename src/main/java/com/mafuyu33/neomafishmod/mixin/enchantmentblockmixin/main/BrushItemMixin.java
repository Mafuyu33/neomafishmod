package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.main;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(BrushItem.class)
public abstract class BrushItemMixin extends Item {

	public BrushItemMixin(Properties properties) {
		super(properties);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		return super.hurtEnemy(stack, target, attacker);
	}

	@Inject(at = @At("HEAD"), method = "useOn")
	private void init(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
		//只在服务端运行
		if (!context.getLevel().isClientSide) {
			//如果刷子有附魔
			if (context.getItemInHand().isEnchanted()) {
				//如果Pos位置方块没有附魔
				if(Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(context.getClickedPos()), new ListTag())){
//				ListTag enchantments = context.getItemInHand().getEnchantments();//获取刷子上的附魔信息列表
				InjectHelper.addToList(context.getItemInHand(), context.getClickedPos());
//				BlockEnchantmentStorage.addBlockEnchantment(context.getClickedPos().immutable(), enchantments);//储存信息
				EquipmentSlot equipmentSlot = context.getItemInHand().equals(context.getPlayer().getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
//				context.getItemInHand().hurtAndBreak(1, context.getPlayer(), (userx) -> {
//					userx.broadcastBreakEvent(equipmentSlot);
//				});
					context.getItemInHand().hurtAndBreak(1,context.getPlayer(),equipmentSlot);
					//位置方块有附魔
				}else {
					ListTag oldEnchantments = BlockEnchantmentStorage.getEnchantmentsAtPosition(context.getClickedPos());
					//先删除信息
					BlockEnchantmentStorage.removeBlockEnchantment(context.getClickedPos().immutable());
//					ListTag enchantments = context.getItemInHand().getEnchantments();//获取刷子上的附魔信息列表
					ListTag enchantments = InjectHelper.enchantmentsToNbtList(context.getItemInHand());
					// 合并附魔列表
					ListTag newEnchantments =mergeNbtLists(oldEnchantments, enchantments);
					//储存信息
					BlockEnchantmentStorage.addBlockEnchantment(context.getClickedPos().immutable(), newEnchantments);
				}
			}else {//如果刷子没有附魔
				//删除信息
				BlockEnchantmentStorage.removeBlockEnchantment(context.getClickedPos().immutable());
			}
		}
	}
	// 合并两个 NBT 列表
	@Unique
	private static ListTag mergeNbtLists(ListTag list1, ListTag list2) {
		ListTag mergedList = new ListTag();
		mergedList.addAll(list1);
		mergedList.addAll(list2);
		return mergedList;
	}
}