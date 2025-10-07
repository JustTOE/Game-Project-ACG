package com.cezar.engine.renderer.model;

import com.cezar.engine.renderer.ResourceManager;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

/**
 * Factory for creating OpenGL models from vertex data.
 * Manages VBO, VAO, and EBO creation and configuration.
 * Integrates with ResourceManager for centralized cleanup.
 */
public class ModelLoader {
    private final ResourceManager resourceManager;

    /**
     * Creates a ModelLoader that registers resources with the given ResourceManager
     * @param resourceManager The resource manager to track created resources
     */
    public ModelLoader(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public Model loadModel(float[] vertices, int[] indices) {
        int vaoId = createVAO();
        glBindVertexArray(vaoId);

        prepareForRender(vertices, indices);

        // Wireframe mode
        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        glBindVertexArray(0);

        // If no indices, vertex count is the number of vertices; otherwise it's the number of indices
        boolean useIndexedRendering = indices.length > 0;
        int vertexCount = useIndexedRendering ? indices.length : vertices.length / 8;
        return new Model(vaoId, vertexCount, useIndexedRendering);
    }

    private void prepareForRender(float[] vertices, int[] indices) {
        int vbo = createVBO();

        // Upload vertex data to VBO
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices);
        vertexBuffer.flip();

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Configure vertex attribute pointers (while VAO is bound)
        // Position attribute (location = 0)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Color attribute (location = 1)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // Texture attribute (location = 2)
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);

        // Upload indices to EBO only if provided
        if (indices.length > 0) {
            int ebo = createEBO();
            IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
            indexBuffer.put(indices);
            indexBuffer.flip();

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
        }
    }

    private int createVAO() {
        int vao = glGenVertexArrays();
        resourceManager.registerVAO(vao);
        return vao;
    }

    private int createVBO() {
        int vbo = glGenBuffers();
        resourceManager.registerVBO(vbo);
        return vbo;
    }

    private int createEBO() {
        int ebo = glGenBuffers();
        resourceManager.registerEBO(ebo);
        return ebo;
    }
}
