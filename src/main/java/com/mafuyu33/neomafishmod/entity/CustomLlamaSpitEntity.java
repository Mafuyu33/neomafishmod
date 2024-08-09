package com.mafuyu33.neomafishmod.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class CustomLlamaSpitEntity extends Projectile {

    protected CustomLlamaSpitEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public CustomLlamaSpitEntity(Level world, Player owner) {
        this(EntityType.LLAMA_SPIT, world);  // 这里你需要替换成你自己的 EntityType
        this.setOwner(owner);
        this.setPos(owner.getX() - (double)(owner.getBbWidth() + 1.0F) * 0.5 * (double)Math.sin(owner.yBodyRot * 0.017453292F), owner.getEyeY() - 0.10000000149011612, owner.getZ() + (double)(owner.getBbWidth() + 1.0F) * 0.5 * (double)Math.cos(owner.yBodyRot * 0.017453292F));
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 vec3d = this.getDeltaMovement();
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this,this::canHitEntity);
        this.onHit(hitResult);
        double d = this.getX() + vec3d.x;
        double e = this.getY() + vec3d.y;
        double f = this.getZ() + vec3d.z;
        this.updateRotation();
        float g = 0.99F;
        float h = 0.06F;
        if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
            this.discard();
        } else if (this.isInWaterOrBubble()) {
            this.discard();
        } else {
            this.setDeltaMovement(vec3d.scale(0.9900000095367432));
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.05999999865889549, 0.0));
            }

            this.setPos(d, e, f);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity owner = this.getOwner();
        if (owner instanceof LivingEntity livingEntity){
            result.getEntity().hurt(this.damageSources().mobProjectile(this,livingEntity),1f);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide){
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);

        double d = packet.getX();
        double e = packet.getY();
        double f = packet.getZ();

        for(int i =0;i<7;i++){
            double g = 0.4 + 0.1 * (double)i;
            this.level().addParticle(ParticleTypes.SPIT,this.getX(),this.getY(),this.getZ(),d*g,e,f*g);
        }

        this.setPos(d,e,f);
    }
}
