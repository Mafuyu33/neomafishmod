package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.badluckofsea;

import com.llamalad7.mixinextras.sugar.Local;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import com.mafuyu33.neomafishmod.network.packet.S2C.EntityVelocityUpdateS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Mafuyu33
 */
@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {

    @Shadow private int fallDamageMax;

    public FallingBlockEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void init(CallbackInfo info){
        Level level = this.level();
        BlockPos blockPos = this.blockPosition();
        FluidState fluidState = level.getFluidState(blockPos);
        if (fluidState.is(FluidTags.WATER) && this.fallDamageMax==-1){
            BlockPos closestNonLiquidBlockPos = null;
            double closestDistanceSq = Double.MAX_VALUE;

            for (int xOffset=-20;xOffset<=19;xOffset++){
                for(int zOffset=-20;zOffset<=19;zOffset++){
                    BlockPos currentPos = blockPos.offset(xOffset,0,zOffset);
                    FluidState fluidState1 = level.getFluidState(currentPos);

                    if (!fluidState1.is(FluidTags.WATER)){
                        double distanceSq = this.distanceToSqr(Vec3.atCenterOf(currentPos));
                        if (distanceSq < closestDistanceSq){
                            closestDistanceSq = distanceSq;
                            closestNonLiquidBlockPos = currentPos;
                        }
                    }
                }
            }

            if (closestNonLiquidBlockPos != null){
                Vec3 direction = Vec3.atCenterOf(closestNonLiquidBlockPos).subtract(this.position()).normalize();
                double speed = 0.15;

                Vec3 velocity = direction.scale(speed);
                    // 使用叠加后的总速度直接设置
                    Vec3 finalMotion = this.getDeltaMovement()
                            .add(0, 0.3, 0)
                            .add(velocity);
                    this.setDeltaMovement(finalMotion);
                    //发包到客户端
                PacketDistributor.sendToAllPlayers(new EntityVelocityUpdateS2CPacket(this.getId(),finalMotion));
            }else{
                    Vec3 finalMotion = this.getDeltaMovement()
                            .add(0, 0.3, 0);
                    this.setDeltaMovement(finalMotion);
                    //发包到客户端
                PacketDistributor.sendToAllPlayers(new EntityVelocityUpdateS2CPacket(this.getId(),finalMotion));
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    public void init1(CallbackInfo info, @Local BlockPos blockPos){
        if (this.fallDamageMax == -1){
            ResourceKey<Enchantment> enchantment = ModEnchantments.BAD_LUCK_OF_SEA;
            ListTag enchantmentNbtList = new ListTag();

            CompoundTag enchantmentNbt = new CompoundTag();
            enchantmentNbt.putString("id",enchantment.location().toString());
            enchantmentNbt.putInt("lvl",3);

            enchantmentNbtList.add(enchantmentNbt);
            BlockEnchantmentStorage.addBlockEnchantment(blockPos.immutable(),enchantmentNbtList);
        }
    }
}
