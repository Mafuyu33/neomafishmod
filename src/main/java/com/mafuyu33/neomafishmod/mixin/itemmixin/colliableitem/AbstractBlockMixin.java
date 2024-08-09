package com.mafuyu33.neomafishmod.mixin.itemmixin.colliableitem;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.Properties.class)
public abstract class AbstractBlockMixin {
    @Mutable
    @Shadow
    boolean hasCollision;
    @Mutable
    @Shadow boolean canOcclude;
    @Mutable
    @Shadow boolean dynamicShape;
    @Inject(method = "noCollission", at = @At("HEAD"),cancellable = true)
    private void init(CallbackInfoReturnable<BlockBehaviour.Properties> cir) {
//        if(!dynamicBounds) {
        hasCollision = true;
        canOcclude = true;
        dynamicShape = false;
        cir.setReturnValue((BlockBehaviour.Properties)(Object)this);
//        }
    }
}
