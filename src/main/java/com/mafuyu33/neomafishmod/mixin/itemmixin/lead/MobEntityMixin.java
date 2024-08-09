package com.mafuyu33.neomafishmod.mixin.itemmixin.lead;

import com.mafuyu33.neomafishmod.Config;
import com.mafuyu33.neomafishmod.mixinhelper.FearMixinHelper;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(Mob.class)
public abstract class MobEntityMixin extends LivingEntity implements Targeting,Leashable {
    @Shadow
    @Final
    protected GoalSelector goalSelector;
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }


    @Shadow public abstract void dropLeash(boolean sendPacket, boolean dropItem);


    /**
     * @author
     * Mafish
     * @reason
     * 所有生物都能被拴绳拴住
     * Make every mob can be leashed
     */
    @Overwrite
    public boolean canBeLeashed(){
        boolean isLeadCanLinkEveryMob = Config.isLeadCanLinkEveryMob();
        if(isLeadCanLinkEveryMob){
            return !this.isLeashed();
        }else {
            return !this.isLeashed() && !(this instanceof Enemy);
        }
    }



    @Inject(at = @At("HEAD"), method = "checkAndHandleImportantInteractions", cancellable = true)
    private void init(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {//拴绳增强
        boolean isLeadCanLinkTogether = Config.isLeadCanLinkTogether();
        if(isLeadCanLinkTogether){
            ItemStack itemStack = player.getItemInHand(hand);
            // 检测范围为玩家周围 50 格的立方体
            AABB collisionBox = player.getBoundingBox().inflate(50.0);
            for (Entity entity : level().getEntities(player, collisionBox)) {
                if (entity instanceof LivingEntity livingEntity) {
                    if (!itemStack.is(Items.LEAD) && ((Mob) livingEntity).getLeashHolder() != null
                            && ((Mob) livingEntity).getLeashHolder() == player) {
                        // 玩家手中牵着实体并且点击一个没有被拴着的实体，就让两个实体互相连接.
                        if (!level().isClientSide) {
                            ((Mob) entity).setLeashedTo(this, true);
                        }
//                    if(!getWorld().isClient) {
//                        this.detachLeashWithoutClearNbt(true, false);
//                    }
                        cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
                    }
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void init(CallbackInfo ci) {
        boolean containsFleeGoal = false;
        for (WrappedGoal goal : this.goalSelector.getAvailableGoals()) {// 检查生物的 goalSelector 列表中是否包含 fleeFromPlayerGoal
            if (goal.getGoal() == fleeFromPlayerGoal) {
                containsFleeGoal = true;
                break;
            }
        }

        if(this.getLastHurtByMob() instanceof Player){//让生物恐惧玩家的效果
            UUID uuid1 = this.getUUID();
            int times = FearMixinHelper.getEntityValue(uuid1);
            if (FearMixinHelper.getIsAttacked(uuid1)) {//计时器
                FearMixinHelper.storeEntityValue(uuid1,times-1);
                System.out.println(times);
//                // 输出生物的 goalSelector 列表
//                System.out.println("All goals in goalSelector:");
//                for (PrioritizedGoal goal : this.goalSelector.getGoals()) {
//                    System.out.println(goal.getGoal());
//                }
            }
            if(times > 0 && FearMixinHelper.getIsFirstTime(uuid1) && !containsFleeGoal){//第一次，设置目标
                addFleeFromGoal();
                FearMixinHelper.setIsFirstTime(uuid1,false);
            }

            if(FearMixinHelper.getIsAttacked(uuid1) && times == 0){//停止计时的时候，清除Goal
                removeFleeFromGoal();
                FearMixinHelper.storeIsAttacked(uuid1,false);

                // 输出生物的 goalSelector 列表
                System.out.println("All goals in goalSelector:");
                for (WrappedGoal goal : this.goalSelector.getAvailableGoals()) {
                    System.out.println(goal.getGoal());
                }
            }
        }
    }
//    @Unique
//    public void attachLeashWithoutClearNbt(Entity entity, boolean sendPacket) {//不清除nbt的拴住方法
//        this.holdingEntity = entity;
//        if (!this.level().isClientSide && sendPacket && this.level() instanceof ServerLevel) {
//            ((ServerLevel)this.level()).getChunkSource().broadcast(this, new ClientboundSetEntityLinkPacket(this, this.holdingEntity));
//        }
//
//        if (this.isPassenger()) {
//            this.stopRiding();
//        }
//
//    }
//
//    @Unique
//    public void detachLeashWithoutClearNbt(boolean sendPacket, boolean dropItem) {
//        if (this.holdingEntity != null) {
//            this.holdingEntity = null;
//
//            if (!this.level().isClientSide && dropItem) {
//                this.spawnAtLocation(Items.LEAD);
//            }
//
//            if (!this.level().isClientSide && sendPacket && this.level() instanceof ServerLevel) {
//                ((ServerLevel)this.level()).getChunkSource().broadcast(this, new ClientboundSetEntityLinkPacket(this, (Entity)null));
//            }
//        }
//
//    }



    @Unique
    private Goal fleeFromPlayerGoal;
    // 添加逃离玩家的行为目标
    @Unique
    private void addFleeFromGoal() {
//        System.out.println("add");
//        this.goalSelector.add(1, new FleeEntityGoal<>((PathAwareEntity) (Object) this, PlayerEntity.class, 6.0F, 1.0, 1.2));
        System.out.println("add");
        fleeFromPlayerGoal = new AvoidEntityGoal<>((PathfinderMob) (Object) this, Player.class, 6.0F, 1.0, 1.2);
        this.goalSelector.addGoal(1, fleeFromPlayerGoal);
    }

    // 移除逃离猫的行为目标
    @Unique
    private void removeFleeFromGoal() {
        // 遍历并移除逃离猫的行为目标
        System.out.println("remove");
        this.goalSelector.removeGoal(fleeFromPlayerGoal);
    }
}