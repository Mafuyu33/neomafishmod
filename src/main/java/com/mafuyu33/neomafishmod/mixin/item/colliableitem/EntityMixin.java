package com.mafuyu33.neomafishmod.mixin.item.colliableitem;

import com.mafuyu33.neomafishmod.item.custom.ColliableItem;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Inject(method = "canBeCollidedWith",at = @At("HEAD"),cancellable = true)
	private void init(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(ColliableItem.isColliable());
	}
}