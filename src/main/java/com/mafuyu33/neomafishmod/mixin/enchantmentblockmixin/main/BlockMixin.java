package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.main;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(Block.class)
public abstract class BlockMixin extends BlockBehaviour implements ItemLike,  net.neoforged.neoforge.common.extensions.IBlockExtension {

    public BlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(at = @At("HEAD"), method = "setPlacedBy")//存储方块的附魔
    private void init1(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack, CallbackInfo ci) {
        InjectHelper.onPlacedInject(level,stack,pos);
    }

    @Inject(at = @At("HEAD"), method = "destroy")//删除方块的附魔
    private void init2(LevelAccessor level, BlockPos pos, BlockState state, CallbackInfo ci){
        if (!level.isClientSide()) {
            if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(pos), new ListTag())) {
                BlockEnchantmentStorage.removeBlockEnchantment(pos.immutable());//删除信息

                //给掉落的方块附魔
//                BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
//                Block.dropStacks(state, (World) world, pos, blockEntity, null, ItemStack.EMPTY);
            }
        }
    }
    @Inject(at = @At("TAIL"), method = "wasExploded")//删除方块的附魔
    private void init4(Level level, BlockPos pos, Explosion explosion, CallbackInfo ci){
        if (!level.isClientSide) {
            if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(pos), new ListTag())) {
                BlockEnchantmentStorage.removeBlockEnchantment(pos.immutable());//删除信息
            }
        }
    }
    @Inject(at = @At("TAIL"), method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V")//删除方块的附魔
    private static void init5(BlockState state, Level level, BlockPos pos, CallbackInfo ci){
        if (!level.isClientSide()) {
            if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(pos), new ListTag())) {
                BlockEnchantmentStorage.removeBlockEnchantment(pos.immutable());//删除信息
            }
        }
    }
}
