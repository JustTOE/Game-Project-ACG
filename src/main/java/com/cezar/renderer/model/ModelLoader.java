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
        int vaoId = createVAO();
        glBindVertexArray(vaoId);

        prepareForRender(3, vertices, indices);

        // Wireframe mode
        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        glBindVertexArray(0);

        return new Model(vaoId, indices.length);
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

    private void prepareForRender(int vertexCount, float[] vertices, int[] indices) {
        int vbo = createVBO();
        int ebo = createEBO();

        // Upload vertex data to VBO
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices);
        vertexBuffer.flip();

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Configure vertex attribute pointers (while VAO is bound)
        // Position attribute (location = 0)
        glVertexAttribPointer(0, vertexCount, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Color attribute (location = 1)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // Texture attribute (location = 2)
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);

        // Upload indices to EBO (must be bound while VAO is active)
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices);
        indexBuffer.flip();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
    }

    public void cleanup() {
        for(int vao : VAOs) {
            glDeleteVertexArrays(vao);
        }

        for(int vbo : VBOs) {
            glDeleteBuffers(vbo);
        }

        for(int ebo : EBOs) {
            glDeleteBuffers(ebo);
        }
    }

}
