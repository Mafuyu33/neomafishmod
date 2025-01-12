package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.superprojectileprotection;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
                    // 复制所有字段
                    neomafishmod$copyFields(projectile, newProjectile);
                    newProjectile.moveTo(projectile.getX(), projectile.getY(), projectile.getZ(), projectile.getYRot(), projectile.getXRot());
                    newProjectile.setDeltaMovement(reflection.scale(k * 0.1+0.9));
                    //设置速度上限，如果速度大于一个特定的数值，就会被限制在这个数值上
                    double maxSpeed = 50;
                    Vec3 velocity = newProjectile.getDeltaMovement();
                    if (velocity.length() > maxSpeed) {
                        velocity = velocity.normalize().scale(maxSpeed);
                        newProjectile.setDeltaMovement(velocity);
                    }
                    //输出投射物的速度
//                    System.out.println(newProjectile.getDeltaMovement().length());
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

    // 使用反射复制所有字段
    @Unique
    private static void neomafishmod$copyFields(Object source, Object target) {
        Class<?> clazz = source.getClass();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    // 忽略静态字段和 final 字段
                    if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                        continue;
                    }

                    // 忽略特定字段
                    if (field.getName().equals("id") || field.getName().equals("uuid") || field.getName().equals("isAddedToWorld")) {
                        continue;
                    }

                    // 获取源字段的值
                    Object value = field.get(source);

                    // 深拷贝处理
                    if (value instanceof ItemStack) {
                        value = ((ItemStack) value).copy();
                    } else if (value instanceof Vec3) {
                        value = new Vec3(((Vec3) value).x, ((Vec3) value).y, ((Vec3) value).z);
                    }

                    // 设置目标字段的值
                    field.set(target, value);

                } catch (IllegalAccessException e) {
                    System.out.println("Failed to access field: " + field.getName() + " - " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Failed to set field: " + field.getName() + " - " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Unexpected error copying field: " + field.getName() + " - " + e.getMessage());
                }
            }
            clazz = clazz.getSuperclass(); // 继续处理父类字段
        }
    }


}
