package com.mafuyu33.neomafishmod.item.custom;

import com.mafuyu33.neomafishmod.entity.custom.LightningProjectileEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class LightningItem extends SwordItem {
    public LightningItem(Tier p_tier, Properties p_properties) {
        super(p_tier, p_properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemInHand = player.getItemInHand(usedHand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));

        if (!level.isClientSide){
            LightningProjectileEntity lightningProjectileEntity = new LightningProjectileEntity(player,level);
            lightningProjectileEntity.setItem(itemInHand);
            lightningProjectileEntity.shootFromRotation(player,player.getXRot(),player.getYRot(),0f,1.5f,1.0f);
            level.addFreshEntity(lightningProjectileEntity);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
//        itemInHand.consume(1, player);
        return super.use(level, player, usedHand);
    }
}
