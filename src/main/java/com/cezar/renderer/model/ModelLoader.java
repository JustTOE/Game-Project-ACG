package com.cezar.renderer.model;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;


public class ModelLoader {

    private final List<Integer> VBOs = new ArrayList<>();
    private final List<Integer> VAOs = new ArrayList<>();
    private final List<Integer> EBOs = new ArrayList<>();

    public Model loadModel(float[] vertices, int[] indices) {
        int id = createVAO();
        glBindVertexArray(id);

        prepareRender(3, vertices, indices);

        glBindVertexArray(0);

        return new Model(id, indices.length);
    }

    public int createVAO() {
        int vao = glGenVertexArrays();
        VAOs.add(vao);
        return vao;
    }

    public int createVBO() {
        int vbo = glGenBuffers();
        VBOs.add(vbo);
        return vbo;
    }

    public int createEBO() {
        int ebo = glGenBuffers();
        EBOs.add(ebo);
        return ebo;
    }

    public void prepareRender(int vertex_count, float[] vertices, int[] indices) {
        int vbo = createVBO();
        int ebo = createEBO();

        // Buffering the vertex position data
        FloatBuffer vertices_data_buffer = BufferUtils.createFloatBuffer(vertices.length);
        vertices_data_buffer.put(vertices);
        vertices_data_buffer.flip();

        // Moving the data from the FloatBuffer to the openGL buffers
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices_data_buffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, vertex_count, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // Buffering indices data
        IntBuffer indices_data_buffer = BufferUtils.createIntBuffer(indices.length);
        indices_data_buffer.put(indices);
        indices_data_buffer.flip();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices_data_buffer, GL_STATIC_DRAW);
    }

    public void cleanup() {
        for(int vao : VAOs) {
            glDeleteVertexArrays(vao);
        }

        for(int vbo : VBOs) {
            glDeleteBuffers(vbo);
        }
    }

}
