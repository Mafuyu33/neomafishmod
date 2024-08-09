package com.mafuyu33.neomafishmod.item.custom;

import com.mafuyu33.neomafishmod.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class CheeseBergerItem extends Item{
    public CheeseBergerItem(Properties properties) {
        super(properties);
    }


    private static final int COOLDOWN_TICKS = 200; // 10秒内只播放一次

    private int cooldown = 0;

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if(entity instanceof Cat) {
            if(user.level().isClientSide) {
                user.level().playSound(user, entity.blockPosition(), ModSounds.CHEESE_BERGER_CAT.value(), SoundSource.MASTER);
            }
            entity.push(0,0.3,0);
        }
        return super.interactLivingEntity(stack, user, entity, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (world.isClientSide && entity instanceof Player) {
            if (cooldown > 0) {
                cooldown--;
            } else {
                if (hasNearbyCat(world, entity)) {
                    playCatSound(world, entity);
                    cooldown = COOLDOWN_TICKS;
                }
            }
        }
    }

    private boolean hasNearbyCat(Level world, Entity entity) {
        return !world.getEntitiesOfClass(Cat.class, entity.getBoundingBox().inflate(8.0), cat -> true).isEmpty();
    }

    private void playCatSound(Level world, Entity entity) {
        // 播放声音
        if(entity instanceof Player) {
            world.playSound((Player) entity, entity.getX(), entity.getY(), entity.getZ(), ModSounds.CHEESE_BERGER_MAN, SoundSource.MASTER, 1.0f, 1.0f);
        }
    }
}
