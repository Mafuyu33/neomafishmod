package com.mafuyu33.neomafishmod.item.custom;

import com.mafuyu33.neomafishmod.item.ModFoods;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ToolMaterial;  // 正确的导入
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class BreadSwordVeryHotItem extends Item {
    public BreadSwordVeryHotItem(Properties breadSwordVeryHot) {
        super(breadSwordVeryHot);
    }

//    @Override
//    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
//        super.inventoryTick(stack, level, entity, slotId, isSelected);
//        ItemEnchantments itemenchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
//        Set<Object2IntMap.Entry<Holder<Enchantment>>> enchantments = itemenchantments.entrySet();
//        if (enchantments.isEmpty()){
//            RegistryAccess registryAccess = level.registryAccess();
//            Optional<HolderLookup.RegistryLookup<Enchantment>> enchantmentGetter = registryAccess.lookup(Registries.ENCHANTMENT);
//            Optional<Holder.Reference<Enchantment>> enchantmentReference = enchantmentGetter.get().get(Enchantments.FIRE_ASPECT);
//            stack.enchant(enchantmentReference.get(), 2);
//        }
//    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration % 20 == 0){
            livingEntity.setRemainingFireTicks(100);
        }
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.accept(Component.translatable("tooltip.neomafishmod.fire_happy"));
        super.appendHoverText(stack, context, display, tooltipComponents, flag);
    }
}