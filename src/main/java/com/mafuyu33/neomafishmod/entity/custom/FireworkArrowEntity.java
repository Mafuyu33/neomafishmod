package com.mafuyu33.neomafishmod.entity.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class FireworkArrowEntity extends AbstractArrow {
    int duration=200;

    protected FireworkArrowEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        super.doPostHurtEffects(target);
        MobEffectInstance instance = new MobEffectInstance(MobEffects.GLOWING,this.duration,0);
        target.addEffect(instance);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        entity.addDeltaMovement(new Vec3(0,5,0));
        for(int i = 1;i<20;i++){
            if (i>16){
                explode();
            }
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return null;
    }

    private void explode(){
        float f = 4.0f;
        this.level().explode(this,this.getX(),this.getY(0.0625),this.getZ(),f,Level.ExplosionInteraction.TNT);
    }

    @Override
    public boolean alwaysAccepts() {
        return super.alwaysAccepts();
    }
}
