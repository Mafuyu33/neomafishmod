package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.badluckofsea;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockEntity.class)
public interface EntityBlockAccessor {

    @Invoker("saveAdditional")
    void saveAdditional(CompoundTag tag, HolderLookup.Provider registries);

}
