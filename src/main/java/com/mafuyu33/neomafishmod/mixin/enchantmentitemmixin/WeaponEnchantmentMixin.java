package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin;

import com.mafuyu33.neomafishmod.effect.ModEffects;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.mixinhelper.FearMixinHelper;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import com.mafuyu33.neomafishmod.mixinhelper.WeaponEnchantmentMixinHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class WeaponEnchantmentMixin extends Entity implements Attackable,net.neoforged.neoforge.common.extensions.ILivingEntityExtension {
    @Unique
    float Times = 0F;

    public WeaponEnchantmentMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract InteractionHand getUsedItemHand();

    @Shadow public abstract ItemStack getItemInHand(InteractionHand hand);

    @Shadow public abstract boolean isAlive();

    @Shadow @Final
    protected static EntityDataAccessor<Byte> DATA_LIVING_ENTITY_FLAGS;

    @Shadow public abstract void setItemInHand(InteractionHand hand, ItemStack stack);

    @Shadow public abstract boolean isDeadOrDying();



    @Inject(at = @At("RETURN"), method = "onEffectAdded")
    private void init1(MobEffectInstance effectInstance, Entity entity, CallbackInfo ci) {
        if(entity != null && effectInstance.is(ModEffects.TELEPORT_EFFECT) && this != entity){
            // 获取攻击者（当前实体）和目标实体的位置
            Vec3 attackerPos = entity.position();
            Vec3 targetPos = this.position();

            // 交换两个实体的位置
            entity.teleportTo(targetPos.x, targetPos.y, targetPos.z);
            this.teleportTo(attackerPos.x, attackerPos.y, attackerPos.z);
        }
    }
    @Inject(at = @At("RETURN"), method = "setLastHurtMob")
    private void init(Entity target, CallbackInfo info) {

        InteractionHand hand = this.getUsedItemHand();
        ItemStack itemStack = this.getItemInHand(hand);
        int k = InjectHelper.getEnchantmentLevel(itemStack, ModEnchantments.KILL_CHICKEN_GET_EGG);
        int j = InjectHelper.getEnchantmentLevel(itemStack, Enchantments.LOOTING);
        int m = InjectHelper.getEnchantmentLevel(itemStack, ModEnchantments.GONG_XI_FA_CAI);
        int n = InjectHelper.getEnchantmentLevel(itemStack, ModEnchantments.MERCY);
        int o = InjectHelper.getEnchantmentLevel(itemStack, ModEnchantments.HOT_POTATO);
        int p = InjectHelper.getEnchantmentLevel(itemStack, ModEnchantments.REVERSE);
        int q = InjectHelper.getEnchantmentLevel(itemStack, ModEnchantments.PAY_TO_PLAY);
        int r = InjectHelper.getEnchantmentLevel(itemStack, ModEnchantments.FEAR);

        if(r>0 && this.isAlwaysTicking() &&
                target instanceof LivingEntity livingEntity && livingEntity.isAlive() && !level().isClientSide){//恐惧
            FearMixinHelper.storeEntityValue(target.getUUID(),20*r);
            FearMixinHelper.storeIsAttacked(target.getUUID(),true);
            FearMixinHelper.setIsFirstTime(target.getUUID(),true);
        }

        if(q > 0 && this.isAlwaysTicking() &&
                target instanceof LivingEntity livingEntity && livingEntity.isAlive() && !level().isClientSide){//镀金
            Iterable<ItemStack> inventory = ((Player) (Object) this).getInventory().items;

            // 遍历背包中的物品栏
            boolean foundGoldenIngot = false;
            int count=0;
            for (ItemStack stack : inventory) {
                if (stack.getItem() == Items.GOLD_INGOT) {
                    // 如果找到金锭，标记为已找到，并从物品栏中减少一个金锭
                    foundGoldenIngot = true;
                    count = stack.getCount();
                    stack.shrink(q); // 减少q个金锭
                    break;
                }
            }
            // 如果找到金锭，则对目标造成额外的伤害
            if (foundGoldenIngot) {
                level().playSound(this,this.blockPosition(),
                        SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS,1f,1f);//播放声音
                float extraDamage = count*3.125F; // 设置额外伤害值
                target.hurt(damageSources().playerAttack(((Player) (Object) this)),extraDamage); // 对目标生物实体造成额外伤害
            }
        }

        if(p>0 && target instanceof LivingEntity livingEntity && livingEntity.isAlive() && !level().isClientSide){//反转了
            if(!target.hasCustomName()) {
                target.setCustomName(Component.literal("Grumm"));
                target.setCustomNameVisible(false);
                WeaponEnchantmentMixinHelper.storeReverse(target.getUUID(),1);
            }else {
                target.setCustomName(null);
                WeaponEnchantmentMixinHelper.storeReverse(target.getUUID(),0);
            }
        }

        if (o>0 && target instanceof LivingEntity livingEntity && livingEntity.isAlive()){//烫手山芋
//            ItemStack targetItemStack = ((LivingEntity) target).getStackInHand(targetHand);
//            itemStack.damage(1,random, Objects.requireNonNull(getServer()).getCommandSource().getPlayer());
            InteractionHand targetHand = ((LivingEntity) target).getUsedItemHand();
            if (!livingEntity.getMainHandItem().isEmpty()) {
                spawnAtLocation(livingEntity.getMainHandItem());
            }
            livingEntity.setItemInHand(targetHand, itemStack.copy());
            this.setItemInHand(hand, ItemStack.EMPTY);
        }
        
        if (k > 0 && target instanceof Chicken) {//杀鸡取卵
            target.spawnAtLocation(Items.EGG);
        }
        if(k > 0 && j>0 && target instanceof Chicken){
            target.spawnAtLocation(Items.EGG);
            target.spawnAtLocation(Items.DRAGON_EGG);
            target.spawnAtLocation(Items.TURTLE_EGG);
            target.spawnAtLocation(Items.SNIFFER_EGG);
            target.spawnAtLocation(Items.FROGSPAWN);
        }


        if (m > 0 && target.getType() == EntityType.VILLAGER) {//恭喜发财
            WeaponEnchantmentMixinHelper.storeEntityValue(target.getId(),m) ;
        }


        if(n > 0 && target instanceof LivingEntity){
            Times=Times+n;
            ((LivingEntity) target).addEffect(
                    new MobEffectInstance(MobEffects.HEALTH_BOOST,900,(int) (Times-1F)));
            ((LivingEntity) target).addEffect(
                    new MobEffectInstance(MobEffects.HEAL,900,(int) (Times-1F)));
        }
    }
    @Unique
    boolean isDrop=false;
    @Inject(at = @At("HEAD"), method = "tick")
    private void init1(CallbackInfo ci) {
        InteractionHand hand = this.getUsedItemHand();
        ItemStack itemStack = this.getItemInHand(hand);
        int o = InjectHelper.getEnchantmentLevel(itemStack, ModEnchantments.HOT_POTATO);
        if(o>0 && this.isDeadOrDying() && !isDrop){
            spawnAtLocation(itemStack);
            isDrop=true;
        }
    }
}