package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin;

import com.mafuyu33.neomafishmod.item.custom.ColliableItem;
import com.mafuyu33.neomafishmod.mixinhelper.WeaponEnchantmentMixinHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class ReverseMixin extends Entity implements Attackable {

    public ReverseMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract Iterable<ItemStack> getArmorSlots();

    @Shadow public abstract boolean hurt(DamageSource source, float amount);

    @Shadow public abstract boolean isAlive();

    @Override
    public boolean canBeCollidedWith() {
        return ColliableItem.isColliable();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        if(WeaponEnchantmentMixinHelper.getReverse(this.getUUID())==1){//反转了，提供向上速度
            push(0,0.1,0);
        }
    }
}
