package com.mafuyu33.neomafishmod.enchantmentblock;

import com.mafuyu33.neomafishmod.NeoMafishMod;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

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
                    .withVertexShader(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID, "core/enchanted_block_pre"))
                    .withFragmentShader(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID, "core/enchanted_block_pre"))
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withUniform("CamPos", UniformType.VEC3)
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

    private static MeshData preMesh = null;
    private static volatile boolean dirty = true;
    private static final RenderSystem.AutoStorageIndexBuffer INDEX_BUFFER = new RenderSystem.AutoStorageIndexBuffer(4, 6, (p_403829_, p_403830_) -> {
        p_403829_.accept(p_403830_);
        p_403829_.accept(p_403830_ + 1);
        p_403829_.accept(p_403830_ + 2);
        p_403829_.accept(p_403830_ + 2);
        p_403829_.accept(p_403830_ + 3);
        p_403829_.accept(p_403830_);
    });

    public static void markDirty() {
        dirty = true;
    }

    private static void updateMesh(RenderLevelStageEvent event) {
        Minecraft mc = Minecraft.getInstance();
        BufferBuilder preBufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
        for (var pos : BlockEnchantmentStorage.getAllEnchantedBlocks()) {

            var state = event.getLevel().getBlockState(pos);
            BlockStateModel blockstatemodel = mc.getBlockRenderer().getBlockModel(state);
            event.getPoseStack().pushPose();
            for (BlockModelPart blockmodelpart : blockstatemodel.collectParts(event.getLevel(), pos, state, RandomSource.create(42L))) {
                event.getPoseStack().pushPose();
                event.getPoseStack().translate(pos.getX(), pos.getY(), pos.getZ());
                for (var direction : Direction.values()) {
                    ModelBlockRenderer.renderQuadList(event.getPoseStack().last(), preBufferBuilder, 1, 1, 1, blockmodelpart.getQuads(direction), 0xf0, 0);
                }
                ModelBlockRenderer.renderQuadList(event.getPoseStack().last(), preBufferBuilder, 1, 1, 1, blockmodelpart.getQuads(null), 0xf0, 0);
                event.getPoseStack().popPose();
            }
            event.getPoseStack().popPose();
        }

        if (preMesh != null) {
            preMesh.close();
        }
        preMesh = preBufferBuilder.build();
        if (preMesh == null) {
            return;
        }
        var iboBuffer = preMesh.indexBuffer();
        if (iboBuffer != null) {
            ibo = ENCHANTED_BLOCK_PRE.getVertexFormat().uploadImmediateIndexBuffer(iboBuffer);
            preIndexType = preMesh.drawState().indexType();
        } else {
            ibo = INDEX_BUFFER.getBuffer(preMesh.drawState().indexCount());
            preIndexType = INDEX_BUFFER.type();
        }
        vbo = ENCHANTED_BLOCK_PRE.getVertexFormat().uploadImmediateVertexBuffer(preMesh.vertexBuffer());
    }

    static GpuBuffer ibo, vbo;
    static VertexFormat.IndexType preIndexType;

    @SubscribeEvent
    public static void onRenderLevelLast(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (dirty) {
            updateMesh(event);
            dirty = false;
        }
        if (preMesh == null) {
            return;
        }
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
        var camPos = mc.gameRenderer.getMainCamera().getPosition();
        prePass.setUniform("CamPos", (float) camPos.x, (float) camPos.y, (float) camPos.z);
        prePass.drawIndexed(0, preMesh.drawState().indexCount());
        prePass.close();

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
        if (postMesh.indexBuffer() == null) {
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

    private static boolean firstJoinDone = false;
    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        // 只在客户端执行 & 只在第一次进入世界时执行
        if (!event.getLevel().isClientSide() || firstJoinDone) return;
        firstJoinDone = true;
        markDirty();
    }

}

