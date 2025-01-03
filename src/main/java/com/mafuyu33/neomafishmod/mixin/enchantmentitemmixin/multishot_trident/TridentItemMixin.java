package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.multishot_trident;

import com.llamalad7.mixinextras.sugar.Local;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantmentHelper;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentItem.class)
public abstract class TridentItemMixin{
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"), method = "releaseUsing")
    private void init(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft, CallbackInfo ci , @Local ThrownTrident throwntrident) {
        int k = InjectHelper.getEnchantmentLevel(stack, Enchantments.MULTISHOT);
        int o = ModEnchantmentHelper.getEnchantmentLevel(ModEnchantments.REDIRECT_PROJECTILE,stack);
        if(k>0){
            if(o>0){
                //抛出m+2个，存放在1至m中
                for (int i = 0; i < k + 1; i++) {
                    Player playerEntity = ((Player) entityLiving);
                    // 玩家视线起点（玩家眼睛位置）
                    Vec3 eyePosition = playerEntity.getEyePosition();
                    // 玩家视线方向（单位向量）
                    Vec3 lookDirection = playerEntity.getLookAngle();
                    // 螺旋线参数
                    // 初始半径
                    double radius = 1.0;
                    // 半径逐渐增大
                    double r = radius + 0.05 * i;
                    // 每点的高度增量
                    double heightStep = -0.01;
                    // 轴向偏移
                    double axialOffset = i * heightStep;
                    // 每点的角度增量
                    double angleStep = Math.PI / (4 + 0.5 * r);
                    // 当前点的角度
                    double angle = i * angleStep;
                    // 中轴线起点
                    Vec3 basePoint = eyePosition.add(lookDirection.scale(-1));
                    // 计算与中轴线正交的两个基向量（U 和 V）
                    // 初始向上向量
                    Vec3 up = new Vec3(0, 1, 0);
                    if (Math.abs(lookDirection.dot(up)) > 0.999) {
                        // 如果视线接近垂直，改用水平向量
                        up = new Vec3(1, 0, 0);
                    }
                    // 与视线垂直的第一个基向量
                    Vec3 U = lookDirection.cross(up).normalize();
                    // 与 U 和视线垂直的第二个基向量
                    Vec3 V = lookDirection.cross(U).normalize();
                    // 根据编号 i 计算螺旋点的坐标
                    // 螺旋偏移量
                    Vec3 spiralOffset = U.scale(r * Math.cos(angle)).add(V.scale(r * Math.sin(angle)));

                    // 最终点位置
                    Vec3 finalPosition = basePoint.add(lookDirection.scale(axialOffset)).add(spiralOffset);


                    ThrownTrident tridentEntity = new ThrownTrident(level,finalPosition.x,finalPosition.y,finalPosition.z, stack);

                    tridentEntity.setOwner(playerEntity);
                    tridentEntity.setDeltaMovement(0,0,0);
                    tridentEntity.shootFromRotation(entityLiving, entityLiving.getXRot(), entityLiving.getYRot(), 0.0F, 0.03F, 1.0F);
                    tridentEntity.setNoGravity(true);

                    tridentEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    level.addFreshEntity(tridentEntity);
                }
            }else {
                //抛出m+2个，存放在1至m中
                for (int i = 0; i < k + 1; i++) {
                    // 生成随机偏移速度
                    double offsetX = level.random.nextGaussian() * 0.1;
                    double offsetY = level.random.nextGaussian() * 0.1;
                    double offsetZ = level.random.nextGaussian() * 0.1;
                    // 创建带有随机偏移速度的实体
                    int j = InjectHelper.getEnchantmentLevel(stack, Enchantments.RIPTIDE);
                    Player playerEntity = ((Player) entityLiving);
                    ThrownTrident tridentEntity = new ThrownTrident(level, playerEntity, stack);
                    tridentEntity.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), 0.0F, 2.5F + (float) j * 0.5F, 1.0F);
                    tridentEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    tridentEntity.push(offsetX, offsetY, offsetZ);
                    level.addFreshEntity(tridentEntity);
                }
            }
        }
    }
}