package com.mafuyu33.neomafishmod.item.custom;

import com.mafuyu33.neomafishmod.entity.CustomLlamaSpitEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LlamaItem extends Item {
    public LlamaItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        level.playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.LLAMA_SPIT, SoundSource.NEUTRAL,0.5f,0.4f/(level.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!level.isClientSide){
            CustomLlamaSpitEntity llamaSpitEntity = new CustomLlamaSpitEntity(level,player);
            llamaSpitEntity.shootFromRotation(player,player.getXRot(),player.getYRot(),0f,1.5f,1.0f);
            level.addFreshEntity(llamaSpitEntity);
        }
        return super.use(level, player, usedHand);
    }
}
