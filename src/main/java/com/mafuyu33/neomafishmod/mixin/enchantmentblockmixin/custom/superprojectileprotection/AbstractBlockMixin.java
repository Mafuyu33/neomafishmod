package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.superprojectileprotection;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.class)
public abstract class AbstractBlockMixin implements FeatureElement {
    @Inject(at = @At("HEAD"), method = "onProjectileHit", cancellable = true)
    private void init(Level level, BlockState state, BlockHitResult hit, Projectile projectile, CallbackInfo ci) {
        int k = BlockEnchantmentStorage.getLevel(ModEnchantments.SUPER_PROJECTILE_PROTECTION,hit.getBlockPos());
        if(k>0 && !level.isClientSide){
            // 获取命中表面的法向量
            Vec3i normalVec3i = hit.getDirection().getNormal();
            Vec3 normal = new Vec3(normalVec3i.getX(), normalVec3i.getY(), normalVec3i.getZ());

            // 获取投射物的入射方向
            Vec3 incoming = projectile.getDeltaMovement();

            // 计算反射向量
            double dotProduct = incoming.dot(normal);
            Vec3 reflection = incoming.subtract(normal.scale(2 * dotProduct));

            try {
                // 使用反射创建新的投射物实例
                Projectile newProjectile = (Projectile) projectile.getType().create(level);
                if (newProjectile != null) {
                    newProjectile.moveTo(projectile.getX(), projectile.getY(), projectile.getZ(), projectile.getYRot(), projectile.getXRot());
                    newProjectile.setDeltaMovement(reflection.scale(k * 0.1+0.9));

                    // 将新的投射物实体添加到世界中
                    level.addFreshEntity(newProjectile);

                    // 删除原投射物
                    projectile.discard();

                    // 取消默认行为
                    ci.cancel();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
