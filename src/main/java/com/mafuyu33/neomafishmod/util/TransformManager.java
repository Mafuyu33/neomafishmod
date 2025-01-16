package com.mafuyu33.neomafishmod.util;

import org.joml.Matrix4f;

/**
 * @author Mafuyu33
 */
public class TransformManager {
    public static final Matrix4f TRANSFORM_MATRIX = new Matrix4f();

    static {
        // 初始化一个便于调试的变换矩阵
        TRANSFORM_MATRIX.identity() // 设置为单位矩阵
//                // 添加平移：x=1, y=2, z=3
//                .translate(1.0f, 2.0f, 3.0f)
//                // 添加旋转：绕X=30°, Y=45°, Z=60°
//                .rotateXYZ((float) Math.toRadians(30), (float) Math.toRadians(45), (float) Math.toRadians(60))
                // 添加缩放：x轴放大1.5倍，y轴缩小到0.8倍，z轴放大1.2倍
                .scale(1f, 5f, 1f);
    }

    public static void applyTransform(Matrix4f matrix) {
        matrix.mul(TRANSFORM_MATRIX);
    }

    public static void setTransform(Matrix4f matrix) {
        TRANSFORM_MATRIX.set(matrix);
    }
}
