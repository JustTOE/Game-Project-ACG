package com.cezar.renderer.model;

public class Model {
    private final int vao;
    private final int vertexCount;

    public Model(int vao, int vertexCount) {
        this.vao = vao;
        this.vertexCount = vertexCount;
    }

    public int getVAO() {
        return vao;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
