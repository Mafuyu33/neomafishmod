package com.mafuyu33.neomafishmod.mixin;

import com.mafuyu33.neomafishmod.util.EvictingLinkedHashSetQueue;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Queue;
import java.util.function.Function;

@Mixin(ParticleEngine.class)
public abstract class ParticleManagerMixin {//增加粒子数量上限

//	@ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"), index = 1)
//	private EvictingQueue<Particle> injected(EvictingQueue<Particle> originalQueue) {
//		int maxSize = 10; // 设置新的最大大小值
//		return EvictingQueue.create(maxSize);
//	}

	@ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"), index = 1)
	private Function<ParticleRenderType, Queue<Particle>> madparticleUseEvictingLinkedHashSetQueueInsteadOfEvictingQueue(Function<ParticleRenderType, Queue<Particle>> mappingFunction) {
		return t -> new EvictingLinkedHashSetQueue<>(16384, 999999999);
	}

}