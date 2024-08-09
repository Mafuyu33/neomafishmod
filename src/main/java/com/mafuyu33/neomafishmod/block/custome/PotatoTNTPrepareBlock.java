package com.mafuyu33.neomafishmod.block.custome;

import com.mafuyu33.neomafishmod.block.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class PotatoTNTPrepareBlock extends FlowerBlock {
    public PotatoTNTPrepareBlock(Holder<MobEffect> effect, float seconds, Properties properties) {
        super(effect, seconds, properties);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        startDelayedOperation(level,pos,state,placer,stack);
    }

    public void startDelayedOperation(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                level.removeBlock(pos,false);
                level.setBlock(pos,(ModBlock.POTATO_TNT.get().defaultBlockState()),3);
            }
        }, 5000);
    }
}
