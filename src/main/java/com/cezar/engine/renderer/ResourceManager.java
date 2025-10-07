package com.cezar.engine.renderer;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

/**
 * Manages the lifecycle of OpenGL resources.
 * Tracks resource IDs and provides centralized cleanup to prevent GPU memory leaks.
 *
 * This class is responsible ONLY for resource management, not loading.
 */
public class ResourceManager {
    private final List<Integer> textureIds = new ArrayList<>();
    private final List<Integer> vaoIds = new ArrayList<>();
    private final List<Integer> vboIds = new ArrayList<>();
    private final List<Integer> eboIds = new ArrayList<>();

    /**
     * Registers a texture ID for tracking and cleanup
     * @param textureId OpenGL texture ID
     */
    public void registerTexture(int textureId) {
        textureIds.add(textureId);
    }

    /**
     * Registers a VAO ID for tracking and cleanup
     * @param vaoId OpenGL VAO ID
     */
    public void registerVAO(int vaoId) {
        vaoIds.add(vaoId);
    }

    /**
     * Registers a VBO ID for tracking and cleanup
     * @param vboId OpenGL VBO ID
     */
    public void registerVBO(int vboId) {
        vboIds.add(vboId);
    }

    /**
     * Registers an EBO ID for tracking and cleanup
     * @param eboId OpenGL EBO ID
     */
    public void registerEBO(int eboId) {
        eboIds.add(eboId);
    }

    /**
     * Cleans up all tracked GPU resources.
     * Call this on application shutdown to prevent GPU memory leaks.
     */
    public void dispose() {
        // Clean up all textures
        for (int textureId : textureIds) {
            glDeleteTextures(textureId);
        }
        textureIds.clear();

        // Clean up all VAOs
        for (int vaoId : vaoIds) {
            glDeleteVertexArrays(vaoId);
        }
        vaoIds.clear();

        // Clean up all VBOs
        for (int vboId : vboIds) {
            glDeleteBuffers(vboId);
        }
        vboIds.clear();

        // Clean up all EBOs
        for (int eboId : eboIds) {
            glDeleteBuffers(eboId);
        }
        eboIds.clear();
    }
}