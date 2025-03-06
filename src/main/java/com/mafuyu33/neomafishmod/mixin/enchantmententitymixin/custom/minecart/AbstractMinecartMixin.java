package com.mafuyu33.neomafishmod.mixin.enchantmententitymixin.custom.minecart;


import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.extensions.IAbstractMinecartExtension;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Mafuyu33
 */
@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends VehicleEntity implements IAbstractMinecartExtension {
    public AbstractMinecartMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;applyGravity()V"))
    private void init(CallbackInfo ci) {
        if(this.getTags().contains("bad_luck_of_sea")){
            this.addDeltaMovement(new Vec3(0,1,0));
        }
    }
}