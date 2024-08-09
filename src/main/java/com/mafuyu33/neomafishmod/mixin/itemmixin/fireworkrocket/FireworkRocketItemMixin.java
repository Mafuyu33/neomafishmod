package com.mafuyu33.neomafishmod.mixin.itemmixin.fireworkrocket;

import com.mafuyu33.neomafishmod.Config;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FireworkRocketItem.class)
public abstract class FireworkRocketItemMixin extends Item {
    public FireworkRocketItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        boolean isFireworkCanUseOnEntity = Config.isFireworkCanUseOnEntity();
        if (isFireworkCanUseOnEntity)
        {
            float f= 1.0f;
            interactionTarget.level().explode(interactionTarget,interactionTarget.getX(),interactionTarget.getY(0.0625),interactionTarget.getZ(),
                    f, Level.ExplosionInteraction.TNT);
            interactionTarget.setDeltaMovement(0,10,0);
            stack.shrink(1);
        }
        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }

    @Inject(method = "useOn",at = @At("RETURN"),locals = LocalCapture.CAPTURE_FAILSOFT)
    private void init(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir){
        boolean fireworkCanUseOnEntity = Config.isFireworkCanUseOnEntity();
        if (fireworkCanUseOnEntity)
        {
            Player player = context.getPlayer();
            ItemStack breastplate = player.getInventory().getArmor(2);
            if (breastplate.getItem()== Items.ELYTRA)
            {
                player.setDeltaMovement(0,10,0);
            }
        }
    }
}
