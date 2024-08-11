package com.mafuyu33.neomafishmod.item.custom;

import com.mafuyu33.neomafishmod.entity.custom.TNTProjectileEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TNTBallItem extends Item {
    public TNTBallItem(Properties properties) {
        super(new Properties());
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getMainHandItem();
        level.playSound(null,player.getX(),player.getY(),player.getZ(),
                SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL,0.5f,0.4f/(level.getRandom().nextFloat() * 0.4f + 0.8f));
        if(!level.isClientSide){
            TNTProjectileEntity projectileEntity = new TNTProjectileEntity(player,level);
            projectileEntity.setItem(itemStack);
            projectileEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(projectileEntity);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        itemStack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());

    }
}
