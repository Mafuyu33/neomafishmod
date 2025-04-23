package com.mafuyu33.neomafishmod.render;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Math;

/**
 * 在 RenderLevelLastEvent 中应用 "enchanted_block_outline.json" 后处理，
 * 并对目标方块进行额外渲染以获得边缘发光效果.
 */
//@EventBusSubscriber(modid = NeoMafishMod.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ColorfulBlockOutline {

    //@SubscribeEvent
    public static void onRenderLevelLast(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.debugLine(1));
        var camera = event.getCamera();
        var camPos = camera.getPosition();
        for (var pos : BlockEnchantmentStorage.getAllEnchantedBlocks()) {
            ShapeRenderer.renderShape(
                    event.getPoseStack(),
                    buffer,
                    event.getLevel().getBlockState(pos).getShape(event.getLevel(), pos, CollisionContext.empty()),
                    pos.getX() - camPos.x,
                    pos.getY() - camPos.y,
                    pos.getZ() - camPos.z,
                    0x77000000 + hslToRgb((int) ((Mth.sin(Util.getMillis() / 3000f) + 1) * 127), 200, 127)
            );
        }
    }

    public static int hslToRgb(int h, int s, int l) {
        float hf = (h & 0xFF) / 255f;
        float sf = (s & 0xFF) / 255f;
        float lf = (l & 0xFF) / 255f;

        float r, g, b;

        if (sf == 0f) {
            r = g = b = lf;
        } else {
            float q = lf < 0.5f
                    ? lf * (1f + sf)
                    : lf + sf - lf * sf;
            float p = 2f * lf - q;
            r = hue2rgb(p, q, hf + 1f / 3f);
            g = hue2rgb(p, q, hf);
            b = hue2rgb(p, q, hf - 1f / 3f);
        }
        int ri = Math.round(r * 255);
        int gi = Math.round(g * 255);
        int bi = Math.round(b * 255);
        return (ri << 16) | (gi << 8) | bi;
    }

    private static float hue2rgb(float p, float q, float t) {
        if (t < 0f) t += 1f;
        if (t > 1f) t -= 1f;
        if (t < 1f / 6f) return p + (q - p) * 6f * t;
        if (t < 1f / 2f) return q;
        if (t < 2f / 3f) return p + (q - p) * (2f / 3f - t) * 6f;
        return p;
    }
}
