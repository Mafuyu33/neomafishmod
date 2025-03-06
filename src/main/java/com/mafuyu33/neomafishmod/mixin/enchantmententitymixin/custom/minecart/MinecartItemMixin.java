package com.mafuyu33.neomafishmod.mixin.enchantmententitymixin.custom.minecart;


import com.llamalad7.mixinextras.sugar.Local;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantmentHelper;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Mafuyu33
 */
@Mixin(MinecartItem.class)
public abstract class MinecartItemMixin extends Item {
    public MinecartItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "useOn",at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private void init(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir, @Local(ordinal = 0) AbstractMinecart abstractminecart) {
        // 获取正在使用的物品
        ItemStack stack = context.getItemInHand();
        if (ModEnchantmentHelper.getEnchantmentLevel(ModEnchantments.BAD_LUCK_OF_SEA, stack) > 0) {
            abstractminecart.addTag("bad_luck_of_sea");
        }
    }
}