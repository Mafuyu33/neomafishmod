package com.mafuyu33.neomafishmod.mixin.itemmixin;

import com.mafuyu33.neomafishmod.event.ChatMessageHandler;
import com.mafuyu33.neomafishmod.item.custom.ColliableItem;
import com.mafuyu33.neomafishmod.item.custom.MathSwordItem;
import com.mafuyu33.neomafishmod.mixinhelper.MathQuestionMixinHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class MathModeMixin extends Entity implements Attackable {

    public MathModeMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract Iterable<ItemStack> getArmorSlots();

    @Shadow public abstract boolean hurt(DamageSource source, float amount);

    @Shadow public abstract boolean isDeadOrDying();

    @Shadow public abstract InteractionHand getUsedItemHand();

    @Shadow public abstract boolean isAlive();


    @Override
    public boolean canBeCollidedWith() {
        return ColliableItem.isColliable();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void init(CallbackInfo ci) {

        if(MathSwordItem.isMathMode()) {//数学领域
            Player closestPlayer = getCommandSenderWorld().getNearestPlayer(this, 100);
            int level = MathSwordItem.getLevel();
            if (closestPlayer != null && !level().isClientSide && !this.isDeadOrDying()) {//出题
                if (!this.isAlwaysTicking() && isPlayerStaring(closestPlayer) && !this.hasCustomName()) {//算数
                    String[] questionAndAnswer = generateArithmeticQuestionAndAnswer(level);

                    String question = questionAndAnswer[0];
                    String questionColored ="§c§l"+question;
                    int answer = Integer.parseInt(questionAndAnswer[1]);
                    MathQuestionMixinHelper.storeEntityValue(this.getId(), answer);

                    this.setCustomName(Component.literal(questionColored));
                    this.setCustomNameVisible(true);
                }
            }
            if (this.hasCustomName() && closestPlayer != null) {//检测有没有答对
//            System.out.println("answer = "+MathQuestionMixinHelper.getEntityValue(this.getId()));
//            System.out.println("youranswer = "+ChatMessageHandler.getNumber());

                if (MathQuestionMixinHelper.getEntityValue(this.getId()) == ChatMessageHandler.getNumber()) {
                    closestPlayer.swing(getUsedItemHand());
                    level().playSound(closestPlayer,closestPlayer.blockPosition(),
                            SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.PLAYERS,1f,1f);
                    this.hurt(damageSources().playerAttack(closestPlayer), 10f+level*2);
                    this.setCustomName(null);
                    this.setCustomNameVisible(false);
                }
            }
        }else if(MathQuestionMixinHelper.getEntityValue(this.getId()) != 0) {
            this.setCustomName(null);
            this.setCustomNameVisible(false);
        }
    }
    @Unique
    private static String[] generateArithmeticQuestionAndAnswer(int level) {
        Random random = new Random();
        String question;
        int answer = 0;

        do {
            int num1 = random.nextInt(10 * (level + 1)) + 1; // 生成1到10之间的随机数
            int num2 = random.nextInt(10 * (level + 1)) + 1; // 生成1到10之间的随机数
            int operator = random.nextInt(4); // 生成0到3之间的随机数，用于选择运算符

            switch (operator) {
                case 0:
                    question = num1 + " + " + num2 + " = ";
                    answer = num1 + num2;
                    break;
                case 1:
                    question = num1 + " - " + num2 + " = ";
                    answer = num1 - num2;
                    break;
                case 2:
                    question = num1 + " × " + num2 + " = ";
                    answer = num1 * num2;
                    break;
                case 3:
                    // 避免除法时出现除不尽的情况，确保答案是整数
                    num2 = random.nextInt(num1) + 1; // 重新生成一个小于num1的随机数，避免除数过大
                    question = num1 * num2 + " ÷ " + num2 + " = ";
                    answer = num1;
                    break;
                default:
                    question = ""; // 不会发生
            }
        } while (answer == 0);

        return new String[]{question, String.valueOf(answer)};
    }
    @Unique
    boolean isPlayerStaring(Player player) {
        Vec3 vec3d = player.getViewVector(1.0F).normalize();
        Vec3 vec3d2 = new Vec3(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
        double d = vec3d2.length();
        vec3d2 = vec3d2.normalize();
        double e = vec3d.dot(vec3d2);
        return e > 1.0 - 0.025 / d ? player.hasLineOfSight(this) : false;
    }
}
