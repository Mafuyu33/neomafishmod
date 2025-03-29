package com.mafuyu33.neomafishmod.item.custom;

import com.mafuyu33.neomafishmod.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RTX4090Item extends Item {

    public RTX4090Item(Properties properties) {
        // 请在注册时调用 properties.pickaxe(...) 为该工具设置挖掘属性
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.PIN.value(), SoundSource.PLAYERS, 0.5f,
                0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));
        return super.use(level, player, usedHand);
    }
}
