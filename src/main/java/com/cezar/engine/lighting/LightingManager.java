package com.cezar.engine.lighting;

import com.cezar.engine.renderer.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Manages lighting VAOs and provides rendering methods for lit objects and light sources.
 */
public class LightingManager {
    private int cubeVao;
    private int lightCubeVao;
    private int vbo;

    private final Shader lightingShader;
    private final Shader lightCubeShader;

    // Cube vertices for a simple 3D cube
    private static final float[] VERTICES = {
            -0.5f, -0.5f, -0.5f,
             0.5f, -0.5f, -0.5f,
             0.5f,  0.5f, -0.5f,
             0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,

            -0.5f, -0.5f,  0.5f,
             0.5f, -0.5f,  0.5f,
             0.5f,  0.5f,  0.5f,
             0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f, -0.5f,  0.5f,

            -0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,

             0.5f,  0.5f,  0.5f,
             0.5f,  0.5f, -0.5f,
             0.5f, -0.5f, -0.5f,
             0.5f, -0.5f, -0.5f,
             0.5f, -0.5f,  0.5f,
             0.5f,  0.5f,  0.5f,

            -0.5f, -0.5f, -0.5f,
             0.5f, -0.5f, -0.5f,
             0.5f, -0.5f,  0.5f,
             0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f, -0.5f,

            -0.5f,  0.5f, -0.5f,
             0.5f,  0.5f, -0.5f,
             0.5f,  0.5f,  0.5f,
             0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f, -0.5f
    };

    public LightingManager(Shader lightingShader, Shader lightCubeShader) {
        this.lightingShader = lightingShader;
        this.lightCubeShader = lightCubeShader;
    }

    public void init() {
        // Create VBO
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, VERTICES, GL_STATIC_DRAW);

        // Configure cube VAO
        cubeVao = glGenVertexArrays();
        glBindVertexArray(cubeVao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Configure light cube VAO (shares same VBO)
        lightCubeVao = glGenVertexArrays();
        glBindVertexArray(lightCubeVao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Unbind
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    /**
     * Renders a lit object using the lighting shader.
     */
    public void renderLitObject(Vector3f objectColor, Vector3f lightColor,
                                Matrix4f model, Matrix4f view, Matrix4f projection) {
        lightingShader.useProgram();
        lightingShader.setVector3f("objectColor", objectColor);
        lightingShader.setVector3f("lightColor", lightColor);
        lightingShader.setMatrix4f("model", model);
        lightingShader.setMatrix4f("view", view);
        lightingShader.setMatrix4f("projection", projection);

        glBindVertexArray(cubeVao);
        glDrawArrays(GL_TRIANGLES, 0, 36);
        glBindVertexArray(0);
    }

    /**
     * Renders a light source cube.
     */
    public void renderLightCube(Light light, Matrix4f view, Matrix4f projection) {
        lightCubeShader.useProgram();
        lightCubeShader.setMatrix4f("view", view);
        lightCubeShader.setMatrix4f("projection", projection);

        Matrix4f model = new Matrix4f().identity()
                .translate(light.getPosition())
                .scale(0.2f);
        lightCubeShader.setMatrix4f("model", model);

        glBindVertexArray(lightCubeVao);
        glDrawArrays(GL_TRIANGLES, 0, 36);
        glBindVertexArray(0);
    }

    public void dispose() {
        glDeleteVertexArrays(cubeVao);
        glDeleteVertexArrays(lightCubeVao);
        glDeleteBuffers(vbo);
    }
}
