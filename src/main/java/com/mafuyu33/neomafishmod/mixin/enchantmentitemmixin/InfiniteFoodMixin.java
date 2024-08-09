package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin;

import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class InfiniteFoodMixin {
    @Mixin(LivingEntity.class)
    public abstract static class InFiniteFoodMixin extends Entity implements Attackable {

        public InFiniteFoodMixin(EntityType<?> entityType, Level level) {
            super(entityType, level);
        }

        @Inject(method = "eat*" , at = @At("HEAD"))
        private void afterEatFood(Level level, ItemStack food, FoodProperties foodProperties,CallbackInfoReturnable<ItemStack> cir) {
            if(foodProperties!=null) {
                int k = InjectHelper.getEnchantmentLevel(food, Enchantments.INFINITY);
                if (k > 0) {
                    food.grow(1);
                }
            }
        }
    }
}