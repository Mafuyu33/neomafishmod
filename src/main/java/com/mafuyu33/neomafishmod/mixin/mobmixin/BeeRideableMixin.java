package com.mafuyu33.neomafishmod.mixin.mobmixin;

import com.mafuyu33.neomafishmod.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;



/*
为蜜蜂添加一个骑乘动作
 */
@Mixin(Bee.class)
public abstract class BeeRideableMixin extends Animal {

   @Unique
   private static final Logger LOGGER = LoggerFactory.getLogger("SIL YONI");

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand){
      boolean isBeeRideable = Config.isBeeRideable();
      if(isBeeRideable) {
         var itemStack = player.getItemInHand(hand);
         if (!itemStack.isEmpty()) {
            return super.mobInteract(player, hand);
         }
         if (this.getFirstPassenger() != null) {
            return super.mobInteract(player, hand);
         }

         Bee entity = (Bee) (Object) this;

         var isServerSide = !entity.level().isClientSide();
         if (isServerSide) {
            LOGGER.info("some one just interacting a bee");
            player.displayClientMessage(Component.literal("riding bee!"), false);
            player.setYRot(entity.getYRot());
            player.setXRot(entity.getXRot());
            player.startRiding(entity);
         }
         return InteractionResult.sidedSuccess(isServerSide);
      }else {
         return InteractionResult.PASS;
      }
   }

   @Override
   public LivingEntity getControllingPassenger(){
      var passenger = this.getFirstPassenger();
      if (passenger instanceof LivingEntity){
         return (LivingEntity)passenger;
      }else {
         return null;
      }
   }
   @Override
   protected void tickRidden(Player controllingPlayer, Vec3 movementInput) {
      this.setRot(controllingPlayer.getYRot(), controllingPlayer.getXRot() * 0.5f);
      this.yBodyRot = this.yHeadRot = this.getYRot();
      this.yHeadRotO = this.yHeadRot;
      // 获取玩家的视角方向
      Vec3 lookDirection = controllingPlayer.getLookAngle();
      // 将玩家的视角方向作为速度向量的一部分
      Vec3 velocity = new Vec3(lookDirection.x, lookDirection.y, lookDirection.z).normalize();
      // 设置实体的速度
      addDeltaMovement(velocity.scale(0.05));
      LOGGER.info("" + this.getSpeed());
   }

   @Override
   protected Vec3 getRiddenInput(Player controllingPlayer, Vec3 movementInput) {
      return super.getRiddenInput(controllingPlayer, movementInput);
   }

   public BeeRideableMixin(EntityType<? extends Animal> e, Level w){
      super(e, w);
      throw new RuntimeException("no construct for mixin class");
   }
}
