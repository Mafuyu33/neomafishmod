package com.mafuyu33.neomafishmod.mixin.itemmixin.swimmingtripwire;

import com.mafuyu33.neomafishmod.Config;
import com.mafuyu33.neomafishmod.mixinhelper.TripwireBlockMixinHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TripWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TripWireBlock.class)
public abstract class TripwireBlockMixin extends Block {

	public TripwireBlockMixin(Properties properties) {
		super(properties);
	}

	@Inject(at = @At("HEAD"), method = "entityInside")
	private void init(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
		boolean isSwimTripwire = Config.isSwimTripwire();
		if (isSwimTripwire){
			if (!level.isClientSide && entity.getPose() != Pose.DYING && !entity.isAlwaysTicking()) {//绊倒生物
				level.broadcastEntityEvent(entity, (byte) 3);
				entity.setPose(Pose.DYING);
			}

			if (entity.getPose() != Pose.SWIMMING && entity.isAlwaysTicking()
					&& TripwireBlockMixinHelper.getEntityValue(entity.getId()) <= 0) {//绊倒玩家
				if (!level.isClientSide) {
					Vec3 velocity = entity.getDeltaMovement(); // 获取实体的速度向量
					System.out.println(velocity);
					if (Math.abs(velocity.y) > 0.07) {
//					world.sendEntityStatus(entity, (byte) 3);
						TripwireBlockMixinHelper.storeEntityValue(entity.getId(), 50);
					}
				} else {
					entity.push(0, 0.3, 0);
				}
			}
		}
	}
}