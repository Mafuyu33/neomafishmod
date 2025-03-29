package com.mafuyu33.neomafishmod.effect.custom;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static java.lang.Math.min;

public class AllAttributesBoostEffect extends MobEffect {

    public AllAttributesBoostEffect() {
        // 蓝色调
        super(MobEffectCategory.BENEFICIAL, 0xF0F8FF);

        // 在1.21.1版本中，MobEffect已经有内置的机制支持药水等级对属性的影响
        // 当添加属性修饰符时，数值会自动根据效果等级进行缩放：amount * (level + 1)

        // 1. 战斗相关属性
        this.addAttributeModifier(Attributes.MAX_HEALTH, ResourceLocation.parse("attributemod:max_health_boost"), 4.0D, AttributeModifier.Operation.ADD_VALUE);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.parse("attributemod:attack_damage_boost"), 3.0D, AttributeModifier.Operation.ADD_VALUE);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.parse("attributemod:attack_speed_boost"), 0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_KNOCKBACK, ResourceLocation.parse("attributemod:attack_knockback_boost"), 0.2D, AttributeModifier.Operation.ADD_VALUE);
        this.addAttributeModifier(Attributes.ARMOR, ResourceLocation.parse("attributemod:armor_boost"), 2.0D, AttributeModifier.Operation.ADD_VALUE);
        this.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, ResourceLocation.parse("attributemod:armor_toughness_boost"), 2.0D, AttributeModifier.Operation.ADD_VALUE);
        this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, ResourceLocation.parse("attributemod:knockback_resistance_boost"), 0.1D, AttributeModifier.Operation.ADD_VALUE);

        // 2. 移动相关属性
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.parse("attributemod:movement_speed_boost"), 0.2D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.FLYING_SPEED, ResourceLocation.parse("attributemod:flying_speed_boost"), 0.2D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.JUMP_STRENGTH, ResourceLocation.parse("attributemod:jump_strength_boost"), 0.2D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.STEP_HEIGHT, ResourceLocation.parse("attributemod:step_height_boost"), 0.5D, AttributeModifier.Operation.ADD_VALUE);

        // 3. 环境生存相关属性
        this.addAttributeModifier(Attributes.LUCK, ResourceLocation.parse("attributemod:luck_boost"), 1.0D, AttributeModifier.Operation.ADD_VALUE);
        this.addAttributeModifier(Attributes.MAX_ABSORPTION, ResourceLocation.parse("attributemod:max_absorption_boost"), 4.0D, AttributeModifier.Operation.ADD_VALUE);
        this.addAttributeModifier(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, ResourceLocation.parse("attributemod:explosion_resistance_boost"), 0.2D, AttributeModifier.Operation.ADD_VALUE);
        this.addAttributeModifier(Attributes.FALL_DAMAGE_MULTIPLIER, ResourceLocation.parse("attributemod:fall_damage_reduction"), -0.2D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.SAFE_FALL_DISTANCE, ResourceLocation.parse("attributemod:safe_fall_boost"), 2.0D, AttributeModifier.Operation.ADD_VALUE);
        this.addAttributeModifier(Attributes.OXYGEN_BONUS, ResourceLocation.parse("attributemod:oxygen_boost"), 30.0D, AttributeModifier.Operation.ADD_VALUE);

        // 4. 玩家特有属性
        this.addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, ResourceLocation.parse("attributemod:block_break_boost"), 0.2D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.BLOCK_INTERACTION_RANGE, ResourceLocation.parse("attributemod:block_range_boost"), 1.0D, AttributeModifier.Operation.ADD_VALUE);
        this.addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, ResourceLocation.parse("attributemod:entity_range_boost"), 1.0D, AttributeModifier.Operation.ADD_VALUE);
        this.addAttributeModifier(Attributes.MINING_EFFICIENCY, ResourceLocation.parse("attributemod:mining_boost"), 0.2D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.SNEAKING_SPEED, ResourceLocation.parse("attributemod:sneaking_boost"), 0.3D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.SWEEPING_DAMAGE_RATIO, ResourceLocation.parse("attributemod:sweeping_boost"), 0.2D, AttributeModifier.Operation.ADD_VALUE);

        // 5. 水中相关属性
        this.addAttributeModifier(Attributes.WATER_MOVEMENT_EFFICIENCY, ResourceLocation.parse("attributemod:water_movement_boost"), 0.3D, AttributeModifier.Operation.ADD_VALUE);
        this.addAttributeModifier(Attributes.SUBMERGED_MINING_SPEED, ResourceLocation.parse("attributemod:water_mining_boost"), 0.3D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        // 6. 其他
        this.addAttributeModifier(Attributes.SCALE, ResourceLocation.parse("attributemod:scale_boost"), 0.3D, AttributeModifier.Operation.ADD_VALUE);
        this.addAttributeModifier(Attributes.GRAVITY, ResourceLocation.parse("attributemod:gravity_increase"), 0.01D, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        // 由于不在tick中执行破坏逻辑，可以设置为false以提高性能
        return false;
    }
}

