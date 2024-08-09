package com.mafuyu33.neomafishmod.item.custom;

import com.mafuyu33.neomafishmod.item.ModFoods;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

public class BreadSwordVeryHotItem extends SwordItem {
    public BreadSwordVeryHotItem() {
        super(Tiers.STONE,new Properties().food(ModFoods.BREAD_SWORD_VERY_HOT));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        ItemEnchantments itemenchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        Set<Object2IntMap.Entry<Holder<Enchantment>>> enchantments = itemenchantments.entrySet();
        if (enchantments.isEmpty()){
            RegistryAccess registryAccess = level.registryAccess();
            Optional<HolderLookup.RegistryLookup<Enchantment>> enchantmentGetter = registryAccess.lookup(Registries.ENCHANTMENT);
            Optional<Holder.Reference<Enchantment>> enchantmentReference = enchantmentGetter.get().get(Enchantments.FIRE_ASPECT);
            stack.enchant(enchantmentReference.get(),2);
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration % 20==0){
            livingEntity.setRemainingFireTicks(100);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.neomafishmod.fire_happy"));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

}
