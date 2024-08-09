package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.throwableaxe;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements TraceableEntity {

    @Shadow public abstract ItemStack getItem();

    @Shadow @Nullable public abstract Entity getOwner();

    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("TAIL") , method = "tick")
    private void init1(CallbackInfo info){
        int i = mafishmod$GetLoyaltyFromItem(this.getItem());
        ItemStack itemStack = this.getItem();

        if(itemStack.getItem() instanceof AxeItem && i > 0 )
        {
            if (this.getOwner()!=null ){
                var fuMaster = this.getOwner();
                if (fuMaster!=null && fuMaster.isAlive()){
                    Vec3 fuMasterPosition = fuMaster.position();
                    if(this.onGround()){
                        var direciton = fuMasterPosition.subtract(this.position()).normalize();
                        double horizontalSpeed = 0.5f;
                        if (direciton!=null){
                            var lastVelocity = new Vec3(direciton.x * horizontalSpeed,0.4,direciton.z * horizontalSpeed);
                            this.setDeltaMovement(lastVelocity);
                        }
                    }
                }
            }

        }
    }


    @Unique
    private byte mafishmod$GetLoyaltyFromItem(ItemStack stack) {
        return this.level() instanceof ServerLevel serverlevel
                ? (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverlevel, stack, this), 0, 127)
                : 0;
    }
}