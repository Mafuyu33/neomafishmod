package com.mafuyu33.neomafishmod.mixin.itemmixin.entityitemrenderer.lightningitemrenderer;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Unique
    private  final Minecraft mc = Minecraft.getInstance();

    @Inject(method = "render",at= @At("HEAD"),cancellable = true)
    public void renderItem(
            ItemStack itemStack,
            ItemDisplayContext displayContext,
            boolean leftHand,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int combinedLight,
            int combinedOverlay,
            BakedModel p_model,
            CallbackInfo ci
    ){
        if (BuiltInRegistries.ITEM.getKey(itemStack.getItem()).equals(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,"lightning_item")))
        {
            ci.cancel();
            LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT,mc.level);
            poseStack.pushPose();

            poseStack.scale(0.5f,0.5f,0.5f);
            mc.getEntityRenderDispatcher().render(lightningBolt,0,0,0,0f,1f,poseStack,bufferSource,combinedLight);
            poseStack.popPose();
        }

    }
}
