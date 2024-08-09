package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin;

import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorse.class)
public abstract class HorseSaddleEnchantmentMixin extends Animal implements ContainerListener, HasCustomInventoryScreen, OwnableEntity, PlayerRideableJumping, Saddleable {
    @Shadow
    protected SimpleContainer inventory;


    protected HorseSaddleEnchantmentMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void init1(CallbackInfo ci) {

        LivingEntity controllingPassenger = this.getControllingPassenger();//骑乘者
        ItemStack saddledItem = this.inventory.getItem(0);
        int k = InjectHelper.getEnchantmentLevel(saddledItem, Enchantments.FIRE_ASPECT);
        int j = InjectHelper.getEnchantmentLevel(saddledItem, Enchantments.CHANNELING);

        if(k>0 && controllingPassenger!=null) {//火焰附加的鞍
            controllingPassenger.igniteForSeconds(1);
        }
        if(j>0 && controllingPassenger!=null){//引雷的鞍
            BlockPos blockPos = controllingPassenger.blockPosition();
            LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(this.level());
            if (lightningEntity != null) {
                lightningEntity.moveTo(Vec3.atBottomCenterOf(blockPos));
                this.level().addFreshEntity(lightningEntity);
                SoundEvent soundEvent = SoundEvents.TRIDENT_THUNDER.value();
                this.playSound(soundEvent, 5, 1.0F);
            }
        }
    }
}
