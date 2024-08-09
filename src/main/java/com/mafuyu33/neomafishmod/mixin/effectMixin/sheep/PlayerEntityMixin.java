package com.mafuyu33.neomafishmod.mixin.effectMixin.sheep;

import com.mafuyu33.neomafishmod.effect.ModEffects;
import com.mafuyu33.neomafishmod.network.packet.C2S.SheepBreedingC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow public abstract FoodData getFoodData();

    @Shadow public abstract boolean isAffectedByFluids();

    @Shadow protected FoodData foodData;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    boolean nEOFORGE1_21$lastStage = false;
    @Unique
    int nEOFORGE1_21$times = 0;

    @Inject(at = @At("HEAD"),method = "tick")
    private void init(CallbackInfo info){
        if(this.getFoodData().needsFood() && this.level().isClientSide){
            nEOFORGE1_21$times = 0;
            mafishmod$sendC2S();
        }

        if(this.hasEffect(ModEffects.SHEEP_EFFECT) && !this.getFoodData().needsFood()){
            if(this.level().isClientSide){
                boolean isSneakKyePressed = Minecraft.getInstance().options.keyShift.isDown();
                if(isSneakKyePressed != nEOFORGE1_21$lastStage){
                    nEOFORGE1_21$times ++;
                    mafishmod$sendC2S();
                }
                nEOFORGE1_21$lastStage = isSneakKyePressed;
            }else if(SheepBreedingC2SPacket.getTimes()>=10){
                this.foodData.setFoodLevel(this.foodData.getFoodLevel()-3);//减少三点饱食度
                Sheep sheepEntity = mafishmod$findSheepAround();
                if(sheepEntity!=null) {
                    System.out.println("createChild!");
                    sheepEntity.spawnChildFromBreeding((ServerLevel) this.level(),sheepEntity);
                }
            }
        }
    }

    @Unique
    private void mafishmod$sendC2S(){
//        PacketByteBuf buf = PacketByteBufs.create();//C2S
//        buf.writeInt(times);
//        ClientPlayNetworking.send(ModMessages.SHEEP_BREEDING_ID, buf);

    }


    @Unique
    private Sheep mafishmod$findSheepAround() {
        float distance = 0.8F;

        if (this.level() == null) {
            return null;
        }

        // 获取玩家所在的世界
        Level world = this.level();

        // 获取玩家当前位置
        Vec3 playerPos = this.position();

        // 定义搜索范围（玩家周围的范围）
        AABB searchBox = new AABB(
                playerPos.x - distance, playerPos.y - distance, playerPos.z - distance,
                playerPos.x + distance, playerPos.y + distance, playerPos.z + distance
        );

        // 搜索范围内的所有羊实体
        List<Sheep> entities = world.getEntitiesOfClass(Sheep.class, searchBox, entity -> true);

        // 检查是否找到准备繁殖的羊实体
        for (Sheep entity : entities) {
            System.out.println("Found Sheep: " + entity);
            if (entity.isInLove()) { // 如果有准备繁殖的羊
                System.out.println("Sheep ready to breed: " + entity);
                return entity;
            }
        }

        System.out.println("No sheep found or ready to breed");
        return null;
    }


}
