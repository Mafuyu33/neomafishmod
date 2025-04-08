package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.main.experience_bottle;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.item.ExperienceBottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Mafuyu33
 */
@Mixin(ExperienceBottleItem.class)
public class ExperienceBottleItemMixin {
    @Inject(method = "use",at = @At(value = "HEAD"),cancellable = true)
    private void init(Level level, Player player, InteractionHand usedHand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        level.playSound((Entity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_BOTTLE_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (level instanceof ServerLevel serverlevel) {
            if (itemstack.isEnchanted()) {
                ListTag enchantmentList = neomafishmod$getTags(itemstack);
                ThrownExperienceBottle bottle = new ThrownExperienceBottle(serverlevel, player, itemstack);
                // 把结构化 enchantmentList 存进瓶子的 NBT
                bottle.getPersistentData().put("block_enchantments", enchantmentList);
                serverlevel.addFreshEntity(bottle);
                bottle.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.7F, 1.0F);
            }else {
                Projectile.spawnProjectileFromRotation(ThrownExperienceBottle::new, serverlevel, itemstack, player, -20.0F, 0.7F, 1.0F);
            }

        }

        player.awardStat(Stats.ITEM_USED.get((ExperienceBottleItem)(Object) this));
        itemstack.consume(1, player);
        cir.setReturnValue(InteractionResult.SUCCESS);
    }

    @Unique
    private static @NotNull ListTag neomafishmod$getTags(ItemStack itemstack) {
        ItemEnchantments enchantments = itemstack.getTagEnchantments();
        ListTag enchantmentList = new ListTag();

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            ResourceKey<Enchantment> enchantmentKey = entry.getKey().getKey();
            int l = entry.getIntValue();

            CompoundTag enchantmentTag = new CompoundTag();
            enchantmentTag.putString("id", enchantmentKey.location().toString());
            enchantmentTag.putInt("lvl", l);
            enchantmentList.add(enchantmentTag);
        }
        return enchantmentList;
    }
}
