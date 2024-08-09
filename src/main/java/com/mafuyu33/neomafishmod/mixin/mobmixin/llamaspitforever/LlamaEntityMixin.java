package com.mafuyu33.neomafishmod.mixin.mobmixin.llamaspitforever;

import com.mafuyu33.neomafishmod.Config;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.animal.horse.Llama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Llama.class)
public abstract class LlamaEntityMixin{
	@Inject(at = @At(value = "HEAD"), method = "registerGoals")
	private void init(CallbackInfo ci) {
		boolean isLlamaSpitForever = Config.isLlamaSpitForever();
		Llama self = (Llama) (Object) this;
        if(isLlamaSpitForever) {
            self.goalSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof RangedAttackGoal);
			self.goalSelector.addGoal(3, new RangedAttackGoal(self, 1.25, 1, 20.0F));
		}else {
            self.goalSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof RangedAttackGoal);
			self.goalSelector.addGoal(3, new RangedAttackGoal(self, 1.25, 40, 20.0F));
		}
	}
}