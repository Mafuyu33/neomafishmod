//package com.mafuyu33.neomafishmod.mixin.effectMixin.invert_screen;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.GameRenderer;
//import org.joml.Matrix4f;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.Unique;
//
///**
// * @author Mafuyu33
// */
//@Mixin(GameRenderer.class)
//public abstract class GameRendererMixin {
//	@Shadow @Final
//    Minecraft minecraft;
//	@Shadow public abstract float getDepthFar();
////	@Inject(method = "checkEntityPostEffect",at = @At("HEAD"))
////	private void init(Level level, Player player, InteractionHand usedHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
////		// This code is injected into the start of MinecraftServer.loadWorld()V
////	}
//
//
//
////
////	@ModifyVariable(method = "renderLevel", at = @At(value = "STORE", ordinal = 0))
////	private Matrix4f init(Matrix4f matrix4f, @Local(ordinal = 0) double d0) {
////		if(KeyInputHandler.plusKey.isDown()){
////			//对matrix4f进行一些变换
////			return neomafishmod$createReverseProjection(d0);
////		}else {
////			return matrix4f;
////		}
////	}
//
//	@Unique
//	private Matrix4f neomafishmod$createReverseProjection(double fov) {
//		Matrix4f matrix = new Matrix4f();
//
//		float near = 0.05f;  // 近平面
//		float far = this.getDepthFar();  // 远平面
//		float aspectRatio = (float) this.minecraft.getWindow().getWidth() / (float) this.minecraft.getWindow().getHeight();
//
//		float theta = (float) Math.toRadians(fov) / 2.0f;
//		float d = (float) (1.0 / Math.tan(theta));
//
//		// 反透视投影矩阵
//		matrix.m00(d / aspectRatio);  // X 方向缩放
//		matrix.m11(d);                // Y 方向缩放
//		matrix.m22((near + far) / (near - far));  // **修改远近缩放方式**
//		matrix.m23((2 * near * far) / (near - far));
//		matrix.m32(1.0f);   // **关键：远处变大**
//		matrix.m33(0.0f);   // 保持 w' 依赖 z
//
//		return matrix;
//	}
//}