package com.cezar.renderer.model;

public class Model {
    int vao;
    int vertex_count;

    public Model(int vao, int vertex_count) {
        this.vao = vao;
        this.vertex_count = vertex_count;
    }

    public int getVAO() {
        return vao;
    }

    public int getVertex_count() {
        return vertex_count;
    }
}
