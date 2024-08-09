package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin;

import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

public class InfiniteBucketMixin {
    @Mixin(BucketItem.class)
    public abstract static class InFiniteBucketMixin extends Item implements DispensibleContainerItem {

        public InFiniteBucketMixin(Properties properties) {
            super(properties);
        }

        /**
         * @author
         * Mafuyu33
         * @reason
         * Add infinite bucket
         */
        @Overwrite
        public static ItemStack getEmptySuccessItem(ItemStack stack, Player player) {
            int a = InjectHelper.getEnchantmentLevel(stack, Enchantments.INFINITY);//无限
            if (!player.getAbilities().invulnerable & a != 1) {
                return new ItemStack(Items.BUCKET);
            } else {
                return stack.copy();
            }
        }
    }
}
