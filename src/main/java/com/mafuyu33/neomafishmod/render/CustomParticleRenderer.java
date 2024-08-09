package com.mafuyu33.neomafishmod.render;


import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

public class CustomParticleRenderer {
    public static void spawnFlameParticles(Vec3 pos) {
        Minecraft client = Minecraft.getInstance();
        ClientLevel world = client.getConnection().getLevel();
        if (world != null) {
            SimpleParticleType flameParticle = ParticleTypes.FLAME;
            BlockPos blockPos = new BlockPos((int) pos.x, (int)pos.y, (int)pos.z);
            // 添加火焰粒子到世界
            world.addParticle(flameParticle, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0.0, 0.0, 0.0);
            System.out.println(123);
        }
    }
}

