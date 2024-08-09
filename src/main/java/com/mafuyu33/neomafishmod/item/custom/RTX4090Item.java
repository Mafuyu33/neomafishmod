package com.mafuyu33.neomafishmod.item.custom;

import com.mafuyu33.neomafishmod.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class RTX4090Item extends PickaxeItem {


    public RTX4090Item(Tier p_42961_, Properties p_42964_) {
        super(p_42961_, p_42964_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        level.playSound(null,player.getX(),player.getY(),player.getZ(),
                ModSounds.PIN.value(), SoundSource.PLAYERS,0.5f,0.4f/(level.getRandom().nextFloat() * 0.4f + 0.8f));
        return super.use(level, player, usedHand);
    }

}
