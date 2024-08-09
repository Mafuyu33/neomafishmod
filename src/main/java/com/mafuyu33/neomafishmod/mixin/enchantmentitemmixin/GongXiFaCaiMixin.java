package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin;

import com.mafuyu33.neomafishmod.mixinhelper.WeaponEnchantmentMixinHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class GongXiFaCaiMixin extends Entity implements net.neoforged.neoforge.common.extensions.ILivingEntityExtension {

    public GongXiFaCaiMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract Iterable<ItemStack> getArmorSlots();

    @Shadow public abstract boolean hurt(DamageSource source, float amount);

    @Shadow public abstract boolean isAlive();


    @Inject(method = "tick", at = @At("HEAD"))
    private void init(CallbackInfo ci) {

        if (this.getType() == EntityType.VILLAGER) {//村民恭喜发财
            int entityId = this.getId();// 获取实体的ID
            int times = WeaponEnchantmentMixinHelper.getEntityValue(entityId);
            if (times > 0) {
                WeaponEnchantmentMixinHelper.storeEntityValue(entityId, times - 1);
                this.spawnAtLocation(Items.EMERALD);
            }
        }
    }
}
