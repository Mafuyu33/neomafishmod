package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.multishot_trident;

import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentItem.class)
public abstract class TridentItemMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"), method = "releaseUsing")
    private void init(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft, CallbackInfo ci) {
        int k = InjectHelper.getEnchantmentLevel(stack, Enchantments.MULTISHOT);
        if(k>0){
            for(int i = 0; i < k + 1; i++){//抛出m+2个鱼钩，存放在1至m中
                // 生成随机偏移速度
                double offsetX = level.random.nextGaussian() * 0.1;
                double offsetY = level.random.nextGaussian() * 0.1;
                double offsetZ = level.random.nextGaussian() * 0.1;
                // 创建带有随机偏移速度的鱼竿实体
                int j = InjectHelper.getEnchantmentLevel(stack,Enchantments.RIPTIDE);
                Player playerEntity = ((Player) entityLiving);
                ThrownTrident tridentEntity = new ThrownTrident(level, playerEntity, stack);
                tridentEntity.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), 0.0F, 2.5F + (float)j * 0.5F, 1.0F);
                tridentEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                tridentEntity.push(offsetX, offsetY, offsetZ);
                level.addFreshEntity(tridentEntity);
            }

        }
    }
}