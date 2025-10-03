package com.cezar.renderer.model;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;


public class ModelLoader {

    private final List<Integer> VBOs = new ArrayList<>();
    private final List<Integer> VAOs = new ArrayList<>();

    public Model loadModel(float[] vertices) {
        int id = createVAO();
        storeDataIntoBufferObject(0, 3, vertices);

        // We are already unbinding the buffers in the storeDataIntoBufferObject method.
        glBindVertexArray(0);

        // Return the new model with vertices.length / 3 because of X, Y, Z coordinates
        return new Model(id, vertices.length / 3);
    }

    public int createVAO() {
        int vao = glGenVertexArrays();
        VAOs.add(vao);
        glBindVertexArray(vao);
        return vao;
    }

    public int createVBO() {
        int vbo = glGenBuffers();
        VBOs.add(vbo);
        return vbo;
    }

    public void storeDataIntoBufferObject(int attrib_no, int vertex_count,float[] data) {
        int vbo = createVBO();

        // Buffering the data
        FloatBuffer vertices_data_buffer = BufferUtils.createFloatBuffer(data.length);
        vertices_data_buffer.put(data);
        vertices_data_buffer.flip();

        // Moving the data from the FloatBuffer to the openGL buffers
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices_data_buffer, GL_STATIC_DRAW);

        // I suggest you do some research about what the arguments of these methods do
        //
        glVertexAttribPointer(attrib_no, vertex_count, GL_FLOAT, false, 0, 0);

        // Unbind buffer when we are done
        glBindBuffer(GL_ARRAY_BUFFER, 0);
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
