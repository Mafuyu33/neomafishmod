package com.mafuyu33.neomafishmod.block.custome;

import com.mafuyu33.neomafishmod.entity.custom.TNTProjectileEntity;
import com.mafuyu33.neomafishmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiConsumer;

public class PotatoTNTBlock extends SlabBlock {
    public PotatoTNTBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
        if (entity instanceof LivingEntity){
            TNTProjectileEntity tntProjectileEntity = new TNTProjectileEntity(((LivingEntity) entity), level);
            tntProjectileEntity.setItem(ModItems.TNT_BALL.get().getDefaultInstance());
            tntProjectileEntity.shootFromRotation(entity,90,entity.getYRot(), 0.0f, 5f, 0f);
            level.addFreshEntity(tntProjectileEntity);
        }
    }

    @Override
    protected void onExplosionHit(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
        super.onExplosionHit(state, level, pos, explosion, dropConsumer);
    }
}
