package com.mafuyu33.neomafishmod.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class ZhuGeItem extends CrossbowItem {

//    private boolean charged = false;
//    private boolean loaded = false;
    public ZhuGeItem(Properties properties) {
        super(properties);
    }

    private static final ChargingSounds DEFAULT_SOUNDS = new ChargingSounds(
            Optional.of(SoundEvents.CROSSBOW_LOADING_START), Optional.of(SoundEvents.CROSSBOW_LOADING_MIDDLE), Optional.of(SoundEvents.CROSSBOW_LOADING_END)
    );

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if ( !isCharged(stack) && tryLoadProjectiles(entityLiving, stack)) {
            ChargingSounds crossbowitem$chargingsounds = this.getChargingSounds(stack);
            crossbowitem$chargingsounds.end()
                    .ifPresent(
                            p_352852_ -> level.playSound(
                                    null,
                                    entityLiving.getX(),
                                    entityLiving.getY(),
                                    entityLiving.getZ(),
                                    p_352852_.value(),
                                    entityLiving.getSoundSource(),
                                    1.0F,
                                    1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F
                            )
                    );
        }
    }

    private static boolean tryLoadProjectiles(LivingEntity shooter, ItemStack crossbowStack) {
        List<ItemStack> list = draw(crossbowStack, shooter.getProjectile(crossbowStack), shooter);
        if (!list.isEmpty()) {
            crossbowStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(list));
            return true;
        } else {
            return false;
        }
    }

    ChargingSounds getChargingSounds(ItemStack stack) {
        return EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.CROSSBOW_CHARGING_SOUNDS).orElse(DEFAULT_SOUNDS);
    }
    //    @Override
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//        var handItemStack = player.getItemInHand(hand);
//        ChargedProjectiles chargedprojectiles = handItemStack.get(DataComponents.CHARGED_PROJECTILES);
//
//        if (chargedprojectiles != null && !chargedprojectiles.isEmpty()) {
//            performShooting(level, player, hand, handItemStack, getShootingPower(chargedprojectiles), 1.0F, null);
//
//            return InteractionResultHolder.consume(handItemStack);
//        }else if(!player.getProjectile(handItemStack).isEmpty()){
//            if (!isCharged(handItemStack)){
//                this.charged = false;
//                this.loaded = false;
//                player.startUsingItem(hand);
//            }
//        }else{
//            return InteractionResultHolder.fail(handItemStack);
//        }
//        return super.use(level, player, hand);
//    }
//
//    public  void performShooting(Level level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, float velocity, float inaccuracy, @Nullable LivingEntity target)
//    {
//
//        if (level instanceof ServerLevel serverlevel) {
//            if (shooter instanceof Player player && net.neoforged.neoforge.event.EventHooks.onArrowLoose(weapon, shooter.level(), player, 1, true) < 0) return;
//            ChargedProjectiles chargedprojectiles = weapon.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
//            if (chargedprojectiles != null && !chargedprojectiles.isEmpty()) {
//                this.shoot(serverlevel, shooter, hand, weapon, chargedprojectiles.getItems(), velocity, inaccuracy, shooter instanceof Player, target);
//                if (shooter instanceof ServerPlayer serverplayer) {
//                    CriteriaTriggers.SHOT_CROSSBOW.trigger(serverplayer, weapon);
//                    serverplayer.awardStat(Stats.ITEM_USED.get(weapon.getItem()));
//                }
//            }
//        }
//    }
//
//    private static float getShootingPower(ChargedProjectiles projectile) {
//        return projectile.contains(Items.FIREWORK_ROCKET) ? 1.6F : 3.15F;
//    }
//
//
//    protected void shoot(
//            ServerLevel level,
//            LivingEntity shooter,
//            InteractionHand hand,
//            ItemStack weapon,
//            List<ItemStack> projectileItems,
//            float velocity,
//            float inaccuracy,
//            boolean isCrit,
//            @Nullable LivingEntity target
//    ) {
//        float f = EnchantmentHelper.processProjectileSpread(level, weapon, shooter, 0.0F);
//        float f1 = projectileItems.size() == 1 ? 0.0F : 2.0F * f / (float)(projectileItems.size() - 1);
//        float f2 = (float)((projectileItems.size() - 1) % 2) * f1 / 2.0F;
//        float f3 = 1.0F;
//
//        for (int i = 0; i < projectileItems.size(); i++) {
//            ItemStack itemstack = projectileItems.get(i);
//            if (!itemstack.isEmpty()) {
//                float f4 = f2 + f3 * (float)((i + 1) / 2) * f1;
//                f3 = -f3;
//                Projectile projectile = this.createProjectile(level, shooter, weapon, itemstack, isCrit);
//                this.shootProjectile(shooter, projectile, i, velocity, inaccuracy, f4, target);
//                level.addFreshEntity(projectile);
//                weapon.hurtAndBreak(this.getDurabilityUse(itemstack), shooter, LivingEntity.getSlotForHand(hand));
//                if (weapon.isEmpty()) {
//                    break;
//                }
//            }
//        }
//    }

}
