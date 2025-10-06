package com.cezar.engine.renderer.model;

import com.cezar.engine.maths.Transform;
import com.cezar.engine.renderer.texture.Texture;

public class Model {
    private final int vao;
    private final int vertexCount;

    private Transform transform;

    public Model(int vao, int vertexCount) {
        this.vao = vao;
        this.vertexCount = vertexCount;
    }

    public Model(int vao, int vertexCount, Transform transform) {
        this.vao = vao;
        this.vertexCount = vertexCount;
        this.transform = transform;
    }

    public int getVAO() {
        return vao;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }
}
