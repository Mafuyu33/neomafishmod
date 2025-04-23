package com.mafuyu33.neomafishmod.render.enchantedblock;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.OptionalDouble;
import java.util.OptionalInt;

@EventBusSubscriber(modid = NeoMafishMod.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class EnchantedBlockRenderer {
    public static RenderTarget allWhite = null;

    public static void resize() {
        if (allWhite == null) {
            allWhite = new TextureTarget(null, Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight(), true, false);
        }
        allWhite.resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
    }

    public static final RenderPipeline ENCHANTED_BLOCK_PRE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.TERRAIN_SNIPPET)
                    .withLocation(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID, "enchanted_block_pre"))
                    .withFragmentShader(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID, "core/enchanted_block_pre"))
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .build()
    );

    public static final RenderPipeline ENCHANTED_BLOCK_POST = RenderPipelines.register(
            RenderPipeline.builder()
                    .withUniform("ProjMat", UniformType.MATRIX4X4)
                    .withUniform("oneTexel", UniformType.VEC2)
                    .withLocation(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID, "enchanted_block_post"))
                    .withVertexShader(ResourceLocation.withDefaultNamespace("core/blit_screen"))
                    .withFragmentShader(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID, "core/enchanted_block_post"))
                    .withSampler("AllWhiteSampler")
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
                    .withDepthWrite(false)
                    .build()

    );


    @SubscribeEvent
    public static void onRenderLevelLast(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        BufferBuilder preBufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
        //AABB aabb = new AABB(0, 0, 0, 0, 0, 0);
        var camPos = event.getCamera().getPosition();
        //event.getFrustum().prepare(camPos.x, camPos.y, camPos.z);
        for (var pos : BlockEnchantmentStorage.getAllEnchantedBlocks()) {
            //aabb.setMinX(pos.getX());
            //aabb.setMinY(pos.getY());
            //aabb.setMinZ(pos.getZ());
            //aabb.setMaxX(pos.getX() + 1);
            //aabb.setMaxY(pos.getY() + 1);
            //aabb.setMaxZ(pos.getZ() + 1);
            //if (!event.getFrustum().isVisible(aabb)) {
            //    continue;
            //}

            var state = event.getLevel().getBlockState(pos);
            BlockStateModel blockstatemodel = mc.getBlockRenderer().getBlockModel(state);
            float r = 1;
            float g = 1;
            float b = 1;
            event.getPoseStack().pushPose();
            event.getPoseStack().translate(camPos.multiply(-1, -1, -1));
            for (BlockModelPart blockmodelpart : blockstatemodel.collectParts(event.getLevel(), pos, state, RandomSource.create(42L))) {
                event.getPoseStack().pushPose();
                event.getPoseStack().translate(pos.getX(), pos.getY(), pos.getZ());
                for (var direction : Direction.values()) {
                    ModelBlockRenderer.renderQuadList(event.getPoseStack().last(), preBufferBuilder, r, g, b, blockmodelpart.getQuads(direction), 0xf0, 0);
                }
                ModelBlockRenderer.renderQuadList(event.getPoseStack().last(), preBufferBuilder, r, g, b, blockmodelpart.getQuads(null), 0xf0, 0);
                event.getPoseStack().popPose();
            }
            event.getPoseStack().popPose();
        }
        var preMesh = preBufferBuilder.build();
        if (preMesh == null) {
            return;
        }
        VertexFormat.IndexType preIndexType;
        GpuBuffer ibo;
        if (preMesh.indexBuffer() == null) {
            RenderSystem.AutoStorageIndexBuffer rendersystem$autostorageindexbuffer = RenderSystem.getSequentialBuffer(preMesh.drawState().mode());
            ibo = rendersystem$autostorageindexbuffer.getBuffer(preMesh.drawState().indexCount());
            preIndexType = rendersystem$autostorageindexbuffer.type();
        } else {
            ibo = ENCHANTED_BLOCK_PRE.getVertexFormat().uploadImmediateIndexBuffer(preMesh.indexBuffer());
            preIndexType = preMesh.drawState().indexType();
        }
        var vbo = ENCHANTED_BLOCK_PRE.getVertexFormat().uploadImmediateVertexBuffer(preMesh.vertexBuffer());

        RenderPass prePass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(
                allWhite.getColorTexture(),
                OptionalInt.of(0x0),
                allWhite.getDepthTexture(),
                OptionalDouble.of(1)
        );
        prePass.setPipeline(ENCHANTED_BLOCK_PRE);
        prePass.setIndexBuffer(ibo, preIndexType);
        prePass.setVertexBuffer(0, vbo);
        prePass.bindSampler("Sampler0", mc.gameRenderer.lightTexture().getTarget());
        prePass.bindSampler("Sampler2", mc.gameRenderer.lightTexture().getTarget());
        prePass.drawIndexed(0, preMesh.drawState().indexCount());
        prePass.close();
        preMesh.close();


        BufferBuilder postBufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        postBufferBuilder.addVertex(0, 0, 0).setUv(0, 0);
        postBufferBuilder.addVertex(1, 0, 0).setUv(1, 0);
        postBufferBuilder.addVertex(1, 1, 0).setUv(1, 1);
        postBufferBuilder.addVertex(0, 1, 0).setUv(0, 1);

        // ---------- 把 BufferBuilder 转成 Mesh 并上传 ----------
        var postMesh = postBufferBuilder.build();
        if (postMesh == null) {
            return;
        }

        VertexFormat.IndexType postIndexType;
        GpuBuffer postIbo;
        if (preMesh.indexBuffer() == null) {
            RenderSystem.AutoStorageIndexBuffer rendersystem$autostorageindexbuffer = RenderSystem.getSequentialBuffer(postMesh.drawState().mode());
            postIbo = rendersystem$autostorageindexbuffer.getBuffer(postMesh.drawState().indexCount());
            postIndexType = rendersystem$autostorageindexbuffer.type();
        } else {
            postIbo = ENCHANTED_BLOCK_POST.getVertexFormat().uploadImmediateIndexBuffer(postMesh.indexBuffer());
            postIndexType = postMesh.drawState().indexType();
        }
        var postVbo = ENCHANTED_BLOCK_POST.getVertexFormat().uploadImmediateVertexBuffer(postMesh.vertexBuffer());
        RenderPass postPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(
                mc.getMainRenderTarget().getColorTexture(),
                OptionalInt.empty()
        );
        postPass.setPipeline(ENCHANTED_BLOCK_POST);
        postPass.setIndexBuffer(postIbo, postIndexType);
        postPass.setVertexBuffer(0, postVbo);
        postPass.bindSampler("AllWhiteSampler", allWhite.getColorTexture());
        float[] outSize = {mc.getWindow().getWidth(), mc.getWindow().getHeight()};
        postPass.setUniform("OutSize", outSize);
        postPass.setUniform("oneTexel", 1f / mc.getWindow().getWidth(), 1f / mc.getWindow().getHeight());
        postPass.drawIndexed(0, postMesh.drawState().indexCount());
        //-----clean-----
        postPass.close();
        postMesh.close();
    }
}

