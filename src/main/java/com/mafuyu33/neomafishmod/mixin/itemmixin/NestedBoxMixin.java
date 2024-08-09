package com.mafuyu33.neomafishmod.mixin.itemmixin;

import com.mafuyu33.neomafishmod.Config;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockItem.class)
public abstract class NestedBoxMixin extends Item {


    @Shadow
    @Final
    @Deprecated private Block block;

    public NestedBoxMixin(Properties properties) {
        super(properties);
    }

    @Shadow public abstract Block getBlock();


    /**
     * @author
     * Mafuyu33
     * @reason
     * 让潜影盒能放进潜影盒里
     */
    @Overwrite
    public boolean canFitInsideContainerItems() {
        boolean isNestedBoxInfinite = Config.isNestedBoxInfinite();
        if(isNestedBoxInfinite){
            return true;
        }else{
            return !(this.block instanceof ShulkerBoxBlock);
        }
    }

}

