package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin;

import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PickaxeItem.class)
public abstract class InfinitePickaxeItemMixin extends Item {

    public InfinitePickaxeItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = world.getBlockState(blockPos);
        ItemStack itemStack = context.getItemInHand();
        Player playerEntity = context.getPlayer();
        LivingEntity user = ((LivingEntity) context.getPlayer());

        int k = InjectHelper.getEnchantmentLevel(itemStack, Enchantments.INFINITY);
        if (k > 0 && !world.isClientSide) {
            world.destroyBlock(blockPos, true);
            world.setBlock(blockPos, (blockState.getBlock()).defaultBlockState(), 3);
            EquipmentSlot equipmentSlot = itemStack.equals(playerEntity.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
            itemStack.hurtAndBreak(1, user, equipmentSlot);
        }
        return super.useOn(context);
    }

}
