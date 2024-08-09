package com.mafuyu33.neomafishmod.mixin.mobmixin.llamaspitforever;

import com.mafuyu33.neomafishmod.Config;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.horse.Llama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Llama.LlamaHurtByTargetGoal.class)
public abstract class LlamaEntity_SpitRevengeGoalMixin extends HurtByTargetGoal {

	public LlamaEntity_SpitRevengeGoalMixin(PathfinderMob mob, Class<?>... toIgnoreDamage) {
		super(mob, toIgnoreDamage);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/Llama;setDidSpit(Z)V"), method = "canContinueToUse",cancellable = true)
	private void init(CallbackInfoReturnable<Boolean> cir) {
		boolean isLlamaSpitForever = Config.isLlamaSpitForever();
		if(isLlamaSpitForever) {
			if(targetMob!=null && targetMob.isAlive()) {
				cir.setReturnValue(true);
			}
		}
	}
}