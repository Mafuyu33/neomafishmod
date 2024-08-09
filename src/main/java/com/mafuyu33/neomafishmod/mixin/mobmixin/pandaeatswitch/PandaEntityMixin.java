package com.mafuyu33.neomafishmod.mixin.mobmixin.pandaeatswitch;

import com.mafuyu33.neomafishmod.item.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Panda.class)
public abstract class PandaEntityMixin extends Animal {

	protected PandaEntityMixin(EntityType<? extends Animal> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract boolean isEating();

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo ci) {
		if(this.isEating() && this.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.SWITCH)){
			this.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));         // 中毒
			this.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));       // 虚弱
			this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));       // 缓慢
			this.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0)); // 挖掘疲劳
			this.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));      // 失明
			this.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, 0));         // 饥饿
			this.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0));         // 反胃
			this.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0));         // 凋零
			this.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 100, 0));     // 漂浮
			this.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 100, 0));         // 不幸
			this.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0));
		}
	}
}