package com.mafuyu33.neomafishmod.mixin.itemmixin;

import com.mafuyu33.neomafishmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class KaoFishMixin extends Entity implements TraceableEntity {

    @Unique
    private int counter=0;

    public KaoFishMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract ItemStack getItem();

    @Inject(at = @At("HEAD"), method = "tick")
    private void init(CallbackInfo info) {
        var world = this.level();
        BlockPos blockPos = this.blockPosition();
        FluidState fluidState = world.getFluidState(blockPos);
        if (this.getItem().is(ModItems.RUBY)  && fluidState.is(FluidTags.LAVA)) {
            counter++;
            if(counter>=20) {
                System.out.println(123);
                spawnAtLocation(ModItems.RAW_RUBY);
                this.discard();
                counter=0;
            }
        }
    }


}
