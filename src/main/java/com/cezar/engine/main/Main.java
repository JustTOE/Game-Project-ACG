package com.cezar.engine.main;

import com.cezar.engine.maths.Camera;
import com.cezar.engine.maths.Transform;
import com.cezar.engine.renderer.RenderManager;
import com.cezar.engine.renderer.Shader;
import com.cezar.engine.renderer.model.Model;
import com.cezar.engine.renderer.model.ModelLoader;
import com.cezar.engine.renderer.texture.Texture;
import com.cezar.engine.renderer.texture.TextureLoader;
import com.cezar.engine.window.InputManager;
import com.cezar.engine.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import java.util.Vector;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    public static float[] vertices = new float[] {
            // Position(X,Y,Z)      Color(R,G,B)        Texture coords
            // Back face
            -0.5f, -0.5f, -0.5f,    0.0f, 0.0f, 1.0f,   0.0f, 0.0f,
            0.5f, -0.5f, -0.5f,     0.0f, 0.0f, 1.0f,   1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,     0.0f, 0.0f, 1.0f,   1.0f, 1.0f,
            0.5f,  0.5f, -0.5f,     0.0f, 0.0f, 1.0f,   1.0f, 1.0f,
            -0.5f,  0.5f, -0.5f,    0.0f, 0.0f, 1.0f,   0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,    0.0f, 0.0f, 1.0f,   0.0f, 0.0f,

            // Front face
            -0.5f, -0.5f,  0.5f,    1.0f, 0.0f, 0.0f,   0.0f, 0.0f,
            0.5f, -0.5f,  0.5f,     1.0f, 0.0f, 0.0f,   1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,     1.0f, 0.0f, 0.0f,   1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,     1.0f, 0.0f, 0.0f,   1.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,    1.0f, 0.0f, 0.0f,   0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,    1.0f, 0.0f, 0.0f,   0.0f, 0.0f,

            // Left face
            -0.5f,  0.5f,  0.5f,    0.0f, 1.0f, 0.0f,   1.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,    0.0f, 1.0f, 0.0f,   1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,    0.0f, 1.0f, 0.0f,   0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,    0.0f, 1.0f, 0.0f,   0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,    0.0f, 1.0f, 0.0f,   0.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,    0.0f, 1.0f, 0.0f,   1.0f, 0.0f,

            // Right face
            0.5f,  0.5f,  0.5f,     1.0f, 1.0f, 0.0f,   1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,     1.0f, 1.0f, 0.0f,   1.0f, 1.0f,
            0.5f, -0.5f, -0.5f,     1.0f, 1.0f, 0.0f,   0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,     1.0f, 1.0f, 0.0f,   0.0f, 1.0f,
            0.5f, -0.5f,  0.5f,     1.0f, 1.0f, 0.0f,   0.0f, 0.0f,
            0.5f,  0.5f,  0.5f,     1.0f, 1.0f, 0.0f,   1.0f, 0.0f,

            // Bottom face
            -0.5f, -0.5f, -0.5f,    1.0f, 0.0f, 1.0f,   0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,     1.0f, 0.0f, 1.0f,   1.0f, 1.0f,
            0.5f, -0.5f,  0.5f,     1.0f, 0.0f, 1.0f,   1.0f, 0.0f,
            0.5f, -0.5f,  0.5f,     1.0f, 0.0f, 1.0f,   1.0f, 0.0f,
            -0.5f, -0.5f,  0.5f,    1.0f, 0.0f, 1.0f,   0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f,    1.0f, 0.0f, 1.0f,   0.0f, 1.0f,

            // Top face
            -0.5f,  0.5f, -0.5f,    0.0f, 1.0f, 1.0f,   0.0f, 1.0f,
            0.5f,  0.5f, -0.5f,     0.0f, 1.0f, 1.0f,   1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,     0.0f, 1.0f, 1.0f,   1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,     0.0f, 1.0f, 1.0f,   1.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,    0.0f, 1.0f, 1.0f,   0.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,    0.0f, 1.0f, 1.0f,   0.0f, 1.0f
    };

    public static int[] indices = new int[] {};

    public static Vector3f cubePositions[] = new Vector3f[] {
    new Vector3f( 0.0f,  0.0f,  0.0f),
    new Vector3f( 2.0f,  5.0f, -15.0f),
    new Vector3f(-1.5f, -2.2f, -2.5f),
    new Vector3f(-3.8f, -2.0f, -12.3f),
    new Vector3f( 2.4f, -0.4f, -3.5f),
    new Vector3f(-1.7f,  3.0f, -7.5f),
    new Vector3f( 1.3f, -2.0f, -2.5f),
    new Vector3f( 1.5f,  2.0f, -2.5f),
    new Vector3f( 1.5f,  0.2f, -1.5f),
    new Vector3f(-1.3f,  1.0f, -1.5f)

};


    public static void main(String[] args) {
        init();
    }

    private static void init() {
        if(!glfwInit()) {
            System.out.println("Unable to initialize GLFW!");
            glfwTerminate();
        }

        Window window = new Window(800, 600, "Shadow Labyrinth", NULL, NULL);

        GL.createCapabilities();
        glViewport(0, 0, 800, 600);

        glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        glEnable(GL_DEPTH_TEST);

        Shader shader = new Shader();
        shader.attach("src/main/resources/shaders/vertex.glsl", GL_VERTEX_SHADER);
        shader.attach("src/main/resources/shaders/fragment.glsl", GL_FRAGMENT_SHADER);
        shader.link();

        RenderManager renderManager = new RenderManager(shader);

        TextureLoader textureLoader = new TextureLoader();
        Texture texture = textureLoader.loadTexture("src/main/resources/textures/texture.jpg");
        int textureId = textureLoader.generateTexture(texture);
        texture.setTextureId(textureId);

        ModelLoader modelLoader = new ModelLoader();
        Model model = modelLoader.loadModel(vertices, indices);

        // Set transform
        Transform modelTransform = new Transform();
        modelTransform.setRotation(new Vector3f((float) Math.toRadians(-55.0f), 0.0f, 0.0f));
        model.setTransform(modelTransform);

        // Camera object
        Camera camera = new Camera();

        // Projection matrix (will be updated each frame)
        Matrix4f projection = new Matrix4f();

        // InputManager for handling keyboard/mouse input
        InputManager inputManager = new InputManager(camera);
        inputManager.setupCallbacks(window);

        // Delta time tracking for frame-rate independent movement
        float deltaTime = 0.0f;
        float lastFrame = 0.0f;

        while(!glfwWindowShouldClose(window.getHandle())) {
            // Calculate delta time
            float currentFrame = (float) glfwGetTime();
            deltaTime = currentFrame - lastFrame;
            lastFrame = currentFrame;

            // Process input
            inputManager.processInput(window, deltaTime);

            // Update projection matrix with current FOV
            projection.identity();
            projection.perspective((float) Math.toRadians(camera.getFov()), 800.0f / 600.0f, 0.1f, 100.0f);

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            shader.useProgram();

            // Set view and projection matrices (constant for all cubes)
            int viewLoc = glGetUniformLocation(shader.getProgramId(), "view");
            int projectionLoc = glGetUniformLocation(shader.getProgramId(), "projection");
            glUniformMatrix4fv(viewLoc, false, camera.getLookAt().get(new float[16]));
            glUniformMatrix4fv(projectionLoc, false, projection.get(new float[16]));

            shader.setInt("customTexture", 0);

            // Render multiple cubes
            for(int i = 0; i < 10; i++) {
                Matrix4f modelMatrix = new Matrix4f();
                modelMatrix.translate(cubePositions[i]);
                float angle = (float) Math.toRadians(20.0f * i);
                modelMatrix.rotate(angle, new Vector3f(1.0f, 0.3f, 0.5f));

                int modelLoc = glGetUniformLocation(shader.getProgramId(), "model");
                glUniformMatrix4fv(modelLoc, false, modelMatrix.get(new float[16]));

                renderManager.renderModel(model, texture);
            }

            glfwSwapBuffers(window.getHandle());
            glfwPollEvents();
        }

        shader.freeProgram();
        renderManager.dispose();
        modelLoader.cleanup();
        inputManager.dispose();
        window.destroy();
        glfwTerminate();
    }

}
