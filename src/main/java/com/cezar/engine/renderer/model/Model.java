package com.cezar.engine.renderer.model;

import com.cezar.engine.maths.Transform;
import com.cezar.engine.renderer.texture.Texture;

public class Model {
    private final int vao;
    private final int vertexCount;
    private final boolean useIndexedRendering;

    private Transform transform;

    public Model(int vao, int vertexCount) {
        this(vao, vertexCount, true);
    }

    public Model(int vao, int vertexCount, boolean useIndexedRendering) {
        this.vao = vao;
        this.vertexCount = vertexCount;
        this.useIndexedRendering = useIndexedRendering;
    }

    public Model(int vao, int vertexCount, Transform transform) {
        this(vao, vertexCount, true, transform);
    }

    public Model(int vao, int vertexCount, boolean useIndexedRendering, Transform transform) {
        this.vao = vao;
        this.vertexCount = vertexCount;
        this.useIndexedRendering = useIndexedRendering;
        this.transform = transform;
    }

    public int getVAO() {
        return vao;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public boolean useIndexedRendering() {
        return useIndexedRendering;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }
}
