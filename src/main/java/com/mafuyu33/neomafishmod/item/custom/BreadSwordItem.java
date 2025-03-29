package com.mafuyu33.neomafishmod.item.custom;

import com.mafuyu33.neomafishmod.item.ModFoods;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;

public class BreadSwordItem extends Item {

    public BreadSwordItem(Properties breadSword) {
        super(breadSword);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration % 20 == 0){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 10, 0));
        }
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
    }
}