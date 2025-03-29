package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.throwableaxe;

import com.mafuyu33.neomafishmod.Config;
import com.mafuyu33.neomafishmod.entity.custom.FuProjectileEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class AxeItemMixin {

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    public void use(Level level, Player player, InteractionHand usedHand, CallbackInfoReturnable<InteractionResult> cir) {
        // 获取物品栈
        ItemStack itemStack = player.getItemInHand(usedHand);

        // 检查是否是斧头（通过物品名称或其他方式）
        if (itemStack.getItem().toString().contains("axe") && Config.isFuThrowable()) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f/(level.getRandom().nextFloat() * 0.4f + 0.8f));
            player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));

            if (!level.isClientSide) {
                if (!player.getAbilities().instabuild) {
                    EquipmentSlot slot = usedHand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                    itemStack.hurtAndBreak(1, player, slot);
                    // 不需要手动移除物品，hurtAndBreak已经处理了耐久和物品摧毁
                }

                FuProjectileEntity fuProjectileEntity = new FuProjectileEntity(EntityType.SNOWBALL, level);
                fuProjectileEntity.setItem(itemStack);
                fuProjectileEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.5f, 1.0f);
                level.addFreshEntity(fuProjectileEntity);
            }

            itemStack.consume(1, player);

            // 使用新的InteractionResult返回类型
            cir.setReturnValue(level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
        }
    }
}