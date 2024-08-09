package com.mafuyu33.neomafishmod.item.custom;

import com.mafuyu33.neomafishmod.entity.FuProjectileEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;

public class FuItem extends AxeItem {
    public FuItem() {
        super(Tiers.IRON,new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        level.playSound(null,player.getX(),player.getY(),player.getZ(),
                SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL,0.5f,0.4f/(level.getRandom().nextFloat() * 0.4f + 0.8f));

        if (!level.isClientSide)
        {
            FuProjectileEntity fuProjectileEntity = new FuProjectileEntity(player,level);
            fuProjectileEntity.setItem(itemStack);
            fuProjectileEntity.shootFromRotation(player,player.getXRot(),player.getYRot(),0.0f,1.5f,1.0f);
            level.addFreshEntity(fuProjectileEntity);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        itemStack.consume(1, player);
        return InteractionResultHolder.success(itemStack);
    }
}
