package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.slippery;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;

import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Mafuyu33
 */
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractRecipeBookScreen<InventoryMenu> {

	public InventoryScreenMixin(InventoryMenu menu, Inventory playerInventory, Component title) {
		super(menu, null, playerInventory, title);
		// 传null作为RecipeBookComponent，实际初始化会在InventoryScreen中处理
	}

	@Inject(at = @At(value = "HEAD"), method = "mouseReleased", cancellable = true)
	private void init(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
		// 使用父类的isHovering方法检查槽位
		for (Slot slot : this.menu.slots) {
			if (this.isHovering(slot.x, slot.y, 16, 16, mouseX, mouseY)) {
				ItemStack itemStack = slot.getItem();
				if (!itemStack.isEmpty() && InjectHelper.getEnchantmentLevel(itemStack, ModEnchantments.SLIPPERY) > 0) {
					if (placeItemInPlayerInventory(this.minecraft.player, itemStack)) {
						cir.setReturnValue(true);
						cir.cancel();
						break;
					}
				}
			}
		}
	}

//	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/screen/ScreenHandler;canInsertIntoSlot(Lnet/minecraft/item/ItemStack;Lnet/minecraft/screen/slot/Slot;)Z"), method = "mouseReleased",cancellable = true)
//	private void init1(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir){
//		Slot slot = this.getSlotAt(mouseX, mouseY);
//		if(slot!=null) {
//			ItemStack itemStack = slot.getStack();
//			if (EnchantmentHelper.getLevel(ModEnchantments.SLIPPERY, itemStack) > 0) {
//				if(placeItemInPlayerInventory(this.client.player, itemStack)){
//					cir.cancel();
//				}
//			}
//		}
//	}

	// 在玩家背包的随机位置放置物品
	@Unique
	private static boolean placeItemInPlayerInventory(Player player, ItemStack itemStack) {
		// 获取玩家背包
		Inventory playerInventory = player.getInventory();

		// 创建一个随机数生成器
		Random random = new Random();

		// 获取物品总数
		int count = itemStack.getCount();

		// 创建空槽位列表
		List<Integer> emptySlots = new ArrayList<>();

		// 查找所有空槽位
		for (int i = 0; i < playerInventory.getContainerSize(); i++) {
			// 只检查主物品栏的36个槽位(0-35)，不包括装备槽
			if (i < 36 && playerInventory.getItem(i).isEmpty()) {
				emptySlots.add(i);
			}
		}

		// 如果没有空槽位，返回失败
		if (emptySlots.isEmpty()) {
			return false;
		}

		// 随机选择一个空槽位
		int slotIndex = emptySlots.get(random.nextInt(emptySlots.size()));

		// 将物品放入选定的槽位
		playerInventory.setItem(slotIndex, itemStack.copy());

		// 清空原始物品堆
		itemStack.setCount(0);

		System.out.println("已将物品放置到背包中的槽位: " + slotIndex);
		return true;
	}
}