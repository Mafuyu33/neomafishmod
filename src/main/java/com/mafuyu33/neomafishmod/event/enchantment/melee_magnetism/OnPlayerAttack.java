package com.mafuyu33.neomafishmod.event.enchantment.melee_magnetism;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantmentHelper;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.network.packet.C2S.PlayerActionC2SPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

@EventBusSubscriber(modid = NeoMafishMod.MODID)
public class OnPlayerAttack {
    public static Vec3 targetPosition;
    public static long startTime;
    public static final long DURATION = 100;
    @SubscribeEvent
    public static void onPlayerAttackBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack weapon = player.getWeaponItem();

        //输出检测到的手中的武器的附魔等级
        //System.out.println("level: " + ModEnchantmentHelper.getEnchantmentLevel(ModEnchantments.ONE_WITH_SHADOWS, weapon));

        int i = ModEnchantmentHelper.getEnchantmentLevel(ModEnchantments.MELEE_MAGNETISM, weapon);
        if (i > 0) {
            // 获取玩家的视角方向
            Vec3 lookVec = player.getLookAngle();
            // 圆锥的高度由i决定
            double coneHeight = i * 3.0;
            double coneAngle = 5.0;
            // 获取玩家的位置
            Vec3 playerPos = player.position();
            // 计算圆锥底面圆的半径
            double coneRadius = coneHeight * Math.tan(Math.toRadians(coneAngle));
            // 计算圆锥顶点和底面圆的中心
            Vec3 coneApex = playerPos;
            Vec3 coneBaseCenter = playerPos.add(lookVec.scale(coneHeight));
            // 创建一个近似的AABB来表示圆锥的碰撞箱
            AABB coneBoundingBox = new AABB(
                    Math.min(coneApex.x, coneBaseCenter.x) - coneRadius,
                    Math.min(coneApex.y, coneBaseCenter.y) - coneRadius,
                    Math.min(coneApex.z, coneBaseCenter.z) - coneRadius,
                    Math.max(coneApex.x, coneBaseCenter.x) + coneRadius,
                    Math.max(coneApex.y, coneBaseCenter.y) + coneRadius,
                    Math.max(coneApex.z, coneBaseCenter.z) + coneRadius
            );


            // 获取所有生物实体
            List<Entity> entities = player.level().getEntities(player, coneBoundingBox, e -> e instanceof LivingEntity && e != player);

            // 初始化最小垂直距离和最近的实体
            double minPerpendicularDistance = Double.MAX_VALUE;
            LivingEntity nearestEntity = null;

            for (Entity entity : entities) {
                LivingEntity livingEntity = (LivingEntity) entity;
                Vec3 entityPos = livingEntity.position();

                // 计算从玩家到实体的向量
                Vec3 toEntity = entityPos.subtract(playerPos);

                // 计算实体到玩家视线的垂直距离
                Vec3 crossProduct = toEntity.cross(lookVec);
                double perpendicularDistance = crossProduct.length() / lookVec.length();

                // 计算沿视线方向的距离
                double distanceAlongLookVec = toEntity.dot(lookVec);

                // 检查实体是否在玩家前方并且在圆锥高度内
                if (distanceAlongLookVec > 0 && distanceAlongLookVec < coneHeight && perpendicularDistance < minPerpendicularDistance) {
                    //实体未死亡
                    if (!livingEntity.isDeadOrDying()) {
                        minPerpendicularDistance = perpendicularDistance;
                        nearestEntity = livingEntity;
                    }
                }
            }

            // 输出最近实体的名字
            if (nearestEntity != null) {
//                System.out.println("Nearest entity: " + nearestEntity.getName().getString());
                targetPosition = nearestEntity.position().add(0,-0.5,0);
                startTime = System.currentTimeMillis();
                PacketDistributor.sendToServer(new PlayerActionC2SPacket(nearestEntity.getId()));
            }
//            else {
//                System.out.println("No entity found");
//            }
        }
    }
    @SubscribeEvent
    public static void onPlayerAttack(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        ItemStack weapon = player.getWeaponItem();

        //输出检测到的手中的武器的附魔等级
//        System.out.println("level: " + ModEnchantmentHelper.getEnchantmentLevel(ModEnchantments.ONE_WITH_SHADOWS, weapon));

        int i = ModEnchantmentHelper.getEnchantmentLevel(ModEnchantments.MELEE_MAGNETISM, weapon);
        if (i > 0) {
            // 获取玩家的视角方向
            Vec3 lookVec = player.getLookAngle();
            // 圆锥的高度由i决定
            double coneHeight = i * 3.0;
            double coneAngle = 5.0;
            // 获取玩家的位置
            Vec3 playerPos = player.position();
            // 计算圆锥底面圆的半径
            double coneRadius = coneHeight * Math.tan(Math.toRadians(coneAngle));
            // 计算圆锥顶点和底面圆的中心
            Vec3 coneApex = playerPos;
            Vec3 coneBaseCenter = playerPos.add(lookVec.scale(coneHeight));
            // 创建一个近似的AABB来表示圆锥的碰撞箱
            AABB coneBoundingBox = new AABB(
                    Math.min(coneApex.x, coneBaseCenter.x) - coneRadius,
                    Math.min(coneApex.y, coneBaseCenter.y) - coneRadius,
                    Math.min(coneApex.z, coneBaseCenter.z) - coneRadius,
                    Math.max(coneApex.x, coneBaseCenter.x) + coneRadius,
                    Math.max(coneApex.y, coneBaseCenter.y) + coneRadius,
                    Math.max(coneApex.z, coneBaseCenter.z) + coneRadius
            );


            // 获取所有生物实体
            List<Entity> entities = player.level().getEntities(player, coneBoundingBox, e -> e instanceof LivingEntity && e != player);

            // 初始化最小垂直距离和最近的实体
            double minPerpendicularDistance = Double.MAX_VALUE;
            LivingEntity nearestEntity = null;

            for (Entity entity : entities) {
                LivingEntity livingEntity = (LivingEntity) entity;
                Vec3 entityPos = livingEntity.position();

                // 计算从玩家到实体的向量
                Vec3 toEntity = entityPos.subtract(playerPos);

                // 计算实体到玩家视线的垂直距离
                Vec3 crossProduct = toEntity.cross(lookVec);
                double perpendicularDistance = crossProduct.length() / lookVec.length();

                // 计算沿视线方向的距离
                double distanceAlongLookVec = toEntity.dot(lookVec);

                // 检查实体是否在玩家前方并且在圆锥高度内
                if (distanceAlongLookVec > 0 && distanceAlongLookVec < coneHeight && perpendicularDistance < minPerpendicularDistance) {
                    //实体未死亡
                    if (!livingEntity.isDeadOrDying()) {
                        minPerpendicularDistance = perpendicularDistance;
                        nearestEntity = livingEntity;
                    }
                }
            }

            // 输出最近实体的名字
            if (nearestEntity != null) {
//                System.out.println("Nearest entity: " + nearestEntity.getName().getString());
                targetPosition = nearestEntity.position().add(0,-0.5,0);
                startTime = System.currentTimeMillis();
                PacketDistributor.sendToServer(new PlayerActionC2SPacket(nearestEntity.getId()));
            }
//            else {
//                System.out.println("No entity found");
//            }
        }
    }
}
