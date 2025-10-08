package com.cezar.engine.utils;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class MathUtils {

    public static Matrix3f computeNormalMat3f(Matrix4f modelMatrix) {
        Matrix3f mat3 = new Matrix3f(modelMatrix);
        return mat3.invert().transpose();
    }

}
