package com.mafuyu33.neomafishmod.mixin.itemmixin.linear_algebra;

import com.mafuyu33.neomafishmod.event.KeyInputHandler;
import com.mafuyu33.neomafishmod.util.TransformManager;
import net.minecraft.core.BlockPos;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mafuyu33
 */
@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {

	@Inject(method = "getVisualShape", at = @At("HEAD"), cancellable = true)
	private void modifyVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if (KeyInputHandler.minusKey.isDown()) {
			// 获取原始的形状
			VoxelShape originalShape = state.getVisualShape(level, pos, context);
			if (originalShape.isEmpty()) {
				System.out.println("Original visual shape is empty.");
				return;
			}

			// 应用变换矩阵
			VoxelShape transformedShape = applyMatrixToShape(originalShape, TransformManager.TRANSFORM_MATRIX);

			// 设置返回值
			if (!transformedShape.isEmpty()) {
				cir.setReturnValue(transformedShape);
			} else {
				System.out.println("Transformed visual shape is empty.");
			}
		}
	}


	@Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
	private void modifyShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
		// 检查键盘输入是否按下对应的键
		if (KeyInputHandler.minusKey.isDown()) {
			// 获取原始形状，默认情况下为 Shapes.block()
			VoxelShape originalShape = Shapes.block();

			// 将变换矩阵应用到形状上
			VoxelShape transformedShape = applyMatrixToShape(originalShape, TransformManager.TRANSFORM_MATRIX);

			// 确保变换后的形状合法
			if (transformedShape == null || transformedShape.isEmpty()) {
				// 如果变换失败，则返回原始形状
				cir.setReturnValue(originalShape);
				return;
			}
			// 返回变换后的形状
			cir.setReturnValue(transformedShape);
		}
	}


	@Inject(method = "getCollisionShape",at = @At("HEAD"), cancellable = true)
	private void init0(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if (KeyInputHandler.minusKey.isDown()) {
			// 获取原始的碰撞箱
			VoxelShape originalShape = state.getShape(level, pos);
			if (originalShape.isEmpty()) {
//				System.out.println("Original shape is empty.");
				return;
			}

			// 应用变换矩阵
			VoxelShape transformedShape = applyMatrixToShape(originalShape, TransformManager.TRANSFORM_MATRIX);

			// 设置返回值
			if (!transformedShape.isEmpty()) {
				cir.setReturnValue(transformedShape);
			}
//			else {
//				System.out.println("Transformed shape is empty.");
//			}
		}
	}

	@Unique
	private VoxelShape applyMatrixToShape(VoxelShape shape, Matrix4f transform) {
		List<AABB> transformedBoxes = new ArrayList<>();
		shape.toAabbs().forEach(box -> {
			Vec3 min = applyMatrixToVector(transform, new Vec3(box.minX, box.minY, box.minZ));
			Vec3 max = applyMatrixToVector(transform, new Vec3(box.maxX, box.maxY, box.maxZ));

			// 修正 AABB 顺序并限制范围
			double minX = Math.max(0, Math.min(1, Math.min(min.x, max.x)));
			double minY = Math.max(0, Math.min(1, Math.min(min.y, max.y)));
			double minZ = Math.max(0, Math.min(1, Math.min(min.z, max.z)));
			double maxX = Math.max(0, Math.min(1, Math.max(min.x, max.x)));
			double maxY = Math.max(0, Math.min(1, Math.max(min.y, max.y)));
			double maxZ = Math.max(0, Math.min(1, Math.max(min.z, max.z)));

			transformedBoxes.add(new AABB(minX, minY, minZ, maxX, maxY, maxZ));
		});

		return combineAABBsToVoxelShape(transformedBoxes);
	}

	@Unique
	private Vec3 applyMatrixToVector(Matrix4f matrix, Vec3 vector) {
		Vector4f vec4 = new Vector4f((float) vector.x, (float) vector.y, (float) vector.z, 1.0f);
		vec4.mul(matrix);
		return new Vec3(vec4.x(), vec4.y(), vec4.z());
	}

	@Unique
	private VoxelShape combineAABBsToVoxelShape(List<AABB> boxes) {
		VoxelShape combinedShape = Shapes.empty();
		for (AABB box : boxes) {
			combinedShape = Shapes.or(combinedShape, Shapes.create(box));
		}
		return combinedShape;
	}
}