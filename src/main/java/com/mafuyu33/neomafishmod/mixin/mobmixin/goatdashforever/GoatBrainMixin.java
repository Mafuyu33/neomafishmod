package com.mafuyu33.neomafishmod.mixin.mobmixin.goatdashforever;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mafuyu33.neomafishmod.Config;
import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Tuple;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.PrepareRamNearestTarget;
import net.minecraft.world.entity.ai.behavior.RamTarget;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.goat.GoatAi;
import net.minecraft.world.entity.schedule.Activity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GoatAi.class)
public abstract class GoatBrainMixin {

	@Mutable
	@Final
	@Shadow
	private static TargetingConditions RAM_TARGET_CONDITIONS;
//	@Mutable
//	@Shadow @Final
//	public static int MIN_RAM_TARGET_DISTANCE;
//	@Mutable
//	@Shadow @Final
//	public static int MAX_RAM_TARGET_DISTANCE;
	@Mutable
	@Shadow @Final
	private static UniformInt TIME_BETWEEN_RAMS;
	@Mutable
	@Shadow @Final
	private static UniformInt TIME_BETWEEN_RAMS_SCREAMER;


	@Inject(at = @At("HEAD"), method = "updateActivity")
	private static void init(Goat brain, CallbackInfo ci) {
		boolean isGoatDashForever = Config.isGoatDashForever();
		boolean isGoatDashTogether = Config.isGoatDashTogether();
		if(isGoatDashTogether){
			RAM_TARGET_CONDITIONS = TargetingConditions.forCombat().selector((entity) -> entity.level().getWorldBorder().isWithinBounds(entity.getBoundingBox()));
		}else {
			RAM_TARGET_CONDITIONS = TargetingConditions.forCombat().selector((entity) -> !entity.getType().equals(EntityType.GOAT) && entity.level().getWorldBorder().isWithinBounds(entity.getBoundingBox()));
		}
		if(isGoatDashForever){
//			MIN_RAM_TARGET_DISTANCE=1;
//			MAX_RAM_TARGET_DISTANCE=20;
			TIME_BETWEEN_RAMS = UniformInt.of(0, 1);
			TIME_BETWEEN_RAMS_SCREAMER = UniformInt.of(0, 1);
		}else {
//			MIN_RAM_TARGET_DISTANCE=4;
//			MAX_RAM_TARGET_DISTANCE=7;
			TIME_BETWEEN_RAMS = UniformInt.of(600, 6000);
			TIME_BETWEEN_RAMS_SCREAMER = UniformInt.of(100, 300);
		}

	}
	/**
	 * @author
	 * Mafuyu33
	 * @reason
	 * For dash Forever
	 */
	@Overwrite
	private static void initRamActivity(Brain<Goat> brain) {
		boolean isGoatDashForever = Config.isGoatDashForever();
		if (isGoatDashForever) {
			brain.addActivityWithConditions(
					Activity.RAM,
					ImmutableList.of(
							Pair.of(
									0,
									new RamTarget(
											goat -> goat.isScreamingGoat() ? TIME_BETWEEN_RAMS_SCREAMER : TIME_BETWEEN_RAMS,
											RAM_TARGET_CONDITIONS,
											3.0F,
											goat -> goat.isBaby() ? 1.0 : 2.5,
											goat -> goat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_RAM_IMPACT : SoundEvents.GOAT_RAM_IMPACT,
											goat -> goat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_HORN_BREAK : SoundEvents.GOAT_HORN_BREAK
									)
							),
							Pair.of(
									1,
									new PrepareRamNearestTarget<>(
											goat -> goat.isScreamingGoat() ? TIME_BETWEEN_RAMS_SCREAMER.getMinValue() : TIME_BETWEEN_RAMS.getMinValue(),
											1,
											20,
											1.25F,
											RAM_TARGET_CONDITIONS,
											0,
											goat -> goat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_PREPARE_RAM : SoundEvents.GOAT_PREPARE_RAM
									)
							)
					),
					ImmutableSet.of(
							Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT),
							Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT),
							Pair.of(MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT)
					)
			);
		}else {
			brain.addActivityWithConditions(Activity.RAM, ImmutableList.of(Pair.of(0, new RamTarget((goat) -> {
				return goat.isScreamingGoat() ? TIME_BETWEEN_RAMS_SCREAMER : TIME_BETWEEN_RAMS;
			}, RAM_TARGET_CONDITIONS, 3.0F, (goat) -> {
				return goat.isBaby() ? 1.0 : 2.5;
			}, (goat) -> {
				return goat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_RAM_IMPACT : SoundEvents.GOAT_RAM_IMPACT;
			}, (goat) -> {
				return goat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_HORN_BREAK : SoundEvents.GOAT_HORN_BREAK;
			})), Pair.of(1, new PrepareRamNearestTarget<>((goat) -> {
				return goat.isScreamingGoat() ? TIME_BETWEEN_RAMS_SCREAMER.getMinValue() : TIME_BETWEEN_RAMS.getMinValue();
			}, 4, 7, 1.25F, RAM_TARGET_CONDITIONS, 20, (goat) -> {
				return goat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_PREPARE_RAM : SoundEvents.GOAT_PREPARE_RAM;
			}))), ImmutableSet.of(Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT), Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), Pair.of(MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT)));
		}
	}
}