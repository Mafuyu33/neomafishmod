package com.mafuyu33.neomafishmod.mixin.itemmixin.lead;//package com.example.examplemod.mixin.itemmixin.lead;
//
//import com.example.examplemod.Config;
//import net.minecraft.world.entity.*;
//import net.minecraft.world.entity.ai.goal.Goal;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.Vec3;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.Unique;
// TODO
//@Mixin(PathfinderMob.class)
//public abstract class LeadItemMovementMixin extends Mob {
//	protected LeadItemMovementMixin(EntityType<? extends Mob> entityType, Level level) {
//		super(entityType, level);
//	}
//
////	@Shadow
////	protected abstract void updateForLeashLength(float leashLength);
//	@Shadow protected boolean shouldStayCloseToLeashHolder() {
//		return true;
//	};
//	@Shadow public abstract boolean isPanicking();
//	@Shadow protected double followLeashSpeed() {
//		return 1.0;
//	};
//	@Unique
//	Float breakForce = Config.breakDistance();
//	@Unique
//	private static Vec3 lastPosMainController= new Vec3(0, 0, 0);
//	@Unique
//	private static Vec3 lastPos= new Vec3(0, 0, 0);
//	/**
//	 * @author Mafish
//	 * @reason Make lead stronger && VR lead
//	 */
//	@Overwrite
//	public void updateLeash() 	{
//
//		super.updateLeash();
//		Entity entity = this.getHoldingEntity();
//
//		if (entity != null && entity.level() == this.level()) {
//			this.restrictTo(entity.blockPosition(), 5);
//			float f = this.distanceTo(entity);
//			if ((PathfinderMob) (Object) this instanceof TamableAnimal) {
//				if(((TamableAnimal)(Object)this).isInSittingPose()){
//					if (f > breakForce) {
//						this.dropLeash(true, true);
//					}
//					return;
//				}
//			}
//
//			this.updateForLeashLength(f);
//			if(entity instanceof Player user) {
////				if(VRPlugin.canRetrieveData(user)) {//VR状态下的拴绳
////					forceSimulate(f, entity, 15.0F);
////				} else {//非vr
////					forceSimulate(f, entity, 6.0F);
////				}
//			}else {
//				forceSimulate(f, entity, 6.0F);
//			}
//
//		}
//
//		// TODO VR
//
//		//VR lead modification section. VR拴绳改造部分
//		if(entity != null && entity.level() == this.level()
//				&& entity instanceof Player user && !level().isClientSide) { //The pulling effect of leading in VR state.VR状态下的拴绳拉扯效果，
////			if (VRPlugin.canRetrieveData(user)) {
////				Vec3d currentPosMainController = VRDataHandler.getControllerPosition(user, 0);
////				Vec3d currentPos = VRDataHandler.getHMDPosition(user);
////				if (currentPosMainController != null) {
////					double leashHandDistance = currentPosMainController.distanceTo(lastPosMainController); // Calculate the distance between the current position and the previous position of the leaded hand,计算拴绳手的当前位置和上一个位置之间的距离
////					double pullThreshold = 0.11; // 可以调整这个值来设置敏感度
////					if (!Objects.equals(lastPosMainController, new Vec3d(0, 0, 0))
////							&& !Objects.equals(lastPos, new Vec3d(0, 0, 0))) {
////						double distanceA = currentPosMainController.distanceTo(this.getPos()); // Distance A. A距离
////						double distanceB = lastPosMainController.distanceTo(this.getPos()); // Distance A. B距离
////
////
////						double currentControllerToPlayerDistance = currentPosMainController.distanceTo(currentPos); //The distance from the controller to the player. 手柄到玩家的距离
////						double lastControllerToPlayerDistance = lastPosMainController.distanceTo(lastPos); //The distance from the controller to the player. 手柄到玩家的距离
////						double differenceOfControllerToPlayerDistance=currentControllerToPlayerDistance-lastControllerToPlayerDistance;// Difference.差值
////
////						double stationaryThreshold = 0.05; //Set a threshold for relative stillness between the controller and the player positions, which can be adjusted according to actual circumstances. 设定手柄和玩家位置相对静止的阈值，可以根据实际情况调整
//////						System.out.println("Power"+ leashHandDistance);
//////						System.out.println("currentPos"+ currentPos);
//////						System.out.println("lastPos"+ lastPos);
//////						System.out.println("A"+ distanceA);
//////						System.out.println("B"+ distanceB);
//////						System.out.println("A-B"+ (distanceA - distanceB));
//////						System.out.println("C"+ currentControllerToPlayerDistance);
//////						System.out.println("D"+ lastControllerToPlayerDistance);
//////						System.out.println("C-D"+ (currentControllerToPlayerDistance-lastControllerToPlayerDistance));
////
////						if (leashHandDistance > pullThreshold && (distanceA-distanceB>0)
////								&& (differenceOfControllerToPlayerDistance > stationaryThreshold)) { // If A-B > 0 && the distance between the controller and the player is greater than the stillness threshold. 如果 A-B>0 && 手柄和玩家的距离大于静止阈值
////							Vec3d entityToPlayerVector = currentPosMainController.subtract(this.getPos()).normalize(); // The direction vector from the entity to the player. 实体到玩家的方向向量
////							double forceMagnitude = (distanceA - distanceB) * 5.0; // The magnitude of force is directly proportional to A-B. 力的大小与A-B正相关
////							Vec3d force = entityToPlayerVector.multiply(forceMagnitude);
////
////							// Apply force to the entity. 给实体施加力
////							this.addVelocity(force.x, force.y, force.z);
////						}
////					}
////
////					// Update the previous controller position. 更新上一次的手柄位置
////					lastPosMainController = currentPosMainController;
////					lastPos = currentPos;
////				}
////			}
//		}
//	}
//
//	@Unique
//	private void forceSimulate(float f, Entity entity, float pullDistance) {
//		if (f > breakForce) {
//			this.dropLeash(true, true);
//			this.goalSelector.disableControlFlag(Goal.Flag.MOVE);
//		} else if (f > pullDistance) {
//			double d = (entity.getX() - this.getX()) / (double) f;
//			double e = (entity.getY() - this.getY()) / (double) f;
//			double g = (entity.getZ() - this.getZ()) / (double) f;
//			this.setDeltaMovement(this.getDeltaMovement().add(Math.copySign(d * d * 0.4, d), Math.copySign(e * e * 0.4, e), Math.copySign(g * g * 0.4, g)));
//			this.checkSlowFallDistance();
//		} else if (this.shouldStayCloseToLeashHolder() && !this.isPanicking()) {
//			this.goalSelector.enableControlFlag(Goal.Flag.MOVE);
//			float h = 2.0F;
//			Vec3 vec3d = (new Vec3(entity.getX() - this.getX(), entity.getY() - this.getY(), entity.getZ() - this.getZ())).normalize().scale((double) Math.max(f - 2.0F, 0.0F));
//			this.getNavigation().moveTo(this.getX() + vec3d.x, this.getY() + vec3d.y, this.getZ() + vec3d.z, this.followLeashSpeed());
//		}
//	}
//}