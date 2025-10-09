package com.cezar.engine.lighting;

import com.cezar.engine.maths.Camera;
import com.cezar.engine.renderer.Shader;
import com.cezar.engine.utils.MathUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static java.lang.Math.sin;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
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

    // Cube vertices with normals: Position(3) + Normal(3) = 6 floats per vertex
    private static final float[] VERTICES = {
            -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
             0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
             0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
             0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
            -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,

            -0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,
             0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,
             0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,
             0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,
            -0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,

            -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,
            -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
            -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
            -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
            -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,
            -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,

             0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,
             0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
             0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
             0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
             0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,
             0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,

            -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,
             0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,
             0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
             0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,

            -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,
             0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,
             0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
             0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
            -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
            -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f
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

        // Configure cube VAO (with normals for lighting)
        cubeVao = glGenVertexArrays();
        glBindVertexArray(cubeVao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        // Position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        // Normal attribute
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // Configure light cube VAO (shares same VBO, only uses position)
        lightCubeVao = glGenVertexArrays();
        glBindVertexArray(lightCubeVao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        // Note: stride is 6 floats to skip over normal data in the buffer
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Unbind
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    /**
     * Renders a lit object using the lighting shader.
     */
    public void renderLitObject(Vector3f objectColor, Vector3f lightColor, Vector3f lightPos,
                                Matrix4f model, Matrix4f view, Matrix4f projection, Camera camera) {
        lightingShader.useProgram();
        lightingShader.setVector3f("objectColor", objectColor);
        lightingShader.setVector3f("lightColor", lightColor);
        lightingShader.setVector3f("lightPos", lightPos);
        lightingShader.setMatrix4f("model", model);
        lightingShader.setMatrix4f("view", view);
        lightingShader.setMatrix4f("projection", projection);
        lightingShader.setMatrix3f("normalMatrix", MathUtils.computeNormalMat3f(model));
        lightingShader.setVector3f("viewPos", camera.getPosition());
        lightingShader.setVector3f("material.ambient", 1.0f, 0.5f, 0.31f);
        lightingShader.setVector3f("material.diffuse", 1.0f, 0.5f, 0.31f);
        lightingShader.setVector3f("material.specular", 0.5f, 0.5f, 0.5f);
        lightingShader.setFloat("material.shininess", 32.0f);
        lightingShader.setVector3f("light.ambient",  0.2f, 0.2f, 0.2f);
        lightingShader.setVector3f("light.diffuse",  0.5f, 0.5f, 0.5f); // darken diffuse light a bit
        lightingShader.setVector3f("light.specular", 1.0f, 1.0f, 1.0f);

        lightColor.x = (float)sin(glfwGetTime() * 2.0f);
        lightColor.y = (float)sin(glfwGetTime() * 0.7f);
        lightColor.z = (float)sin(glfwGetTime() * 0.5f);

        Vector3f diffuse = new Vector3f(lightColor).mul(new Vector3f(0.5f));
        Vector3f ambient = new Vector3f(diffuse).mul(new Vector3f(0.2f));

        lightingShader.setVector3f("light.ambient", ambient);
        lightingShader.setVector3f("light.diffuse", diffuse);

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
