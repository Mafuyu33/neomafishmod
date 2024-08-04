package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.adam_mace;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantmentHelper;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

import static net.minecraft.world.item.MaceItem.canSmashAttack;

@Mixin(MaceItem.class)
public abstract class MaceItemMixin {
	@Inject(method = "hurtEnemy",at = @At("TAIL"))
	private void init1(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
		int i = ModEnchantmentHelper.getEnchantmentLevel(ModEnchantments.ADAM,stack);
		if(i>0){
			// 获取实体的高度比例数据
			if (canSmashAttack(attacker)) {
				ScaleData heightScaleData = ScaleTypes.HEIGHT.getScaleData(target);
				ScaleData widthScaleData = ScaleTypes.WIDTH.getScaleData(target);

				// 获取当前的高度和宽度
				float currentHeight = heightScaleData.getScale();
				float currentWidth = widthScaleData.getScale();

				if (attacker.fallDistance < 10f) {
					// 修改高度和宽度
					float newHeight = currentHeight - 0.1F;
					float newWidth = currentWidth + 0.054F;

					heightScaleData.setScale(newHeight);
					widthScaleData.setScale(newWidth);
				} else if (currentHeight > 0.2F) {
					float newHeight = 0.2F;
					float newWidth = 1.432F;

					heightScaleData.setScale(newHeight);
					widthScaleData.setScale(newWidth);
				}else {
					// 修改高度和宽度
					float newHeight = currentHeight - 0.1F;
					float newWidth = currentWidth + 0.054F;

					heightScaleData.setScale(newHeight);
					widthScaleData.setScale(newWidth);
				}
			}
		}
	}
}