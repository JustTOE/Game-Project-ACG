package com.cezar.engine.main;

import com.cezar.engine.lighting.Light;
import com.cezar.engine.lighting.LightingManager;
import com.cezar.engine.maths.Camera;
import com.cezar.engine.maths.Transform;
import com.cezar.engine.renderer.RenderManager;
import com.cezar.engine.renderer.ResourceManager;
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

import static java.lang.Math.sin;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }
}

class Application {

    private final float[] vertices = new float[] {
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

    private final int[] indices = new int[] {};

    private final Vector3f[] cubePositions = new Vector3f[] {
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

    public void run() {
        if(!glfwInit()) {
            System.out.println("Unable to initialize GLFW!");
            glfwTerminate();
        }

        Window window = new Window(800, 600, "Shadow Labyrinth", NULL, NULL);

        GL.createCapabilities();
        glViewport(0, 0, 800, 600);

        glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

        glEnable(GL_DEPTH_TEST);

        // Create ResourceManager for centralized resource cleanup
        ResourceManager resourceManager = new ResourceManager();

        // Shader initialization
        Shader basicShader = new Shader();
        basicShader.attach("src/main/resources/shaders/vertex.glsl", GL_VERTEX_SHADER);
        basicShader.attach("src/main/resources/shaders/fragment.glsl", GL_FRAGMENT_SHADER);
        basicShader.link();

        Shader lightingShader = new Shader();
        lightingShader.attach("src/main/resources/shaders/lighting.vert.glsl", GL_VERTEX_SHADER);
        lightingShader.attach("src/main/resources/shaders/lighting.frag.glsl", GL_FRAGMENT_SHADER);
        lightingShader.link();

        Shader lightCubeShader = new Shader();
        lightCubeShader.attach("src/main/resources/shaders/lightCube.vert.glsl", GL_VERTEX_SHADER);
        lightCubeShader.attach("src/main/resources/shaders/lightCube.frag.glsl", GL_FRAGMENT_SHADER);
        lightCubeShader.link();

        RenderManager renderManager = new RenderManager(basicShader);

        // Initialize lighting system
        LightingManager lightingManager = new LightingManager(lightingShader, lightCubeShader);
        lightingManager.init();

        // Create a light source
        Light light = new Light(new Vector3f(1.2f, 1.0f, 2.0f), new Vector3f(1.0f, 1.0f, 1.0f));

        // Create loaders with dependency injection
        TextureLoader textureLoader = new TextureLoader(resourceManager);
        Texture containerTexture = textureLoader.loadTexture("src/main/resources/textures/container2.png");
        int textureId_1 = textureLoader.generateTexture(containerTexture);
        containerTexture.setTextureId(textureId_1);

        Texture specularContainerTexture = textureLoader.loadTexture("src/main/resources/textures/container2_specular.png");
        int textureId_2 = textureLoader.generateTexture(specularContainerTexture);
        containerTexture.setTextureId(textureId_2);


        ModelLoader modelLoader = new ModelLoader(resourceManager);
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

            glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, textureId_1);

            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, textureId_2);

            // Render lit object
            Matrix4f modelMatrix = new Matrix4f().identity();
            lightingManager.renderLitObject(
                    new Vector3f(1.0f, 0.5f, 0.31f),  // Object color (coral)
                    new Vector3f(1.0f, 1.0f, 1.0f),   // Light color (white)
                    light.getPosition(),              // Light position
                    modelMatrix,
                    camera.getLookAt(),
                    projection,
                    camera
            );



            // Render light source cube
            lightingManager.renderLightCube(light, camera.getLookAt(), projection);

            glfwSwapBuffers(window.getHandle());
            glfwPollEvents();
        }

        // Cleanup resources
        basicShader.freeProgram();
        lightingShader.freeProgram();
        lightCubeShader.freeProgram();
        lightingManager.dispose();
        renderManager.dispose();
        resourceManager.dispose();
        inputManager.dispose();
        window.destroy();
        glfwTerminate();
    }

}
