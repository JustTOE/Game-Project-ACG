package com.cezar.engine.main;

import com.cezar.engine.maths.Transform;
import com.cezar.engine.renderer.RenderManager;
import com.cezar.engine.renderer.Shader;
import com.cezar.engine.renderer.model.Model;
import com.cezar.engine.renderer.model.ModelLoader;
import com.cezar.engine.renderer.texture.Texture;
import com.cezar.engine.renderer.texture.TextureLoader;
import com.cezar.engine.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    public static float[] vertices = new float[] {
//           Position(X,Y,Z)          Color(R,G,B)         Texture coords(relative to model)
            0.5f,  0.5f, 0.0f,      1.0f, 0.0f, 0.0f,       1.0f, 1.0f,
            0.5f, -0.5f, 0.0f,      0.0f, 1.0f, 0.0f,       1.0f, 0.0f,
            -0.5f, -0.5f, 0.0f,     0.0f, 0.0f, 1.0f,       0.0f, 0.0f,
            -0.5f,  0.5f, 0.0f,      1.0f, 1.0f, 1.0f,      0.0f, 1.0f
    };

    public static int[] indices = new int[] {
            0, 1, 3,
            1, 2, 3
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

        // View matrix
        Matrix4f view = new Matrix4f();
        view.translate(new Vector3f(0.0f, 0.0f, -3.0f), view);

        // Projection matrix
        Matrix4f projection = new Matrix4f();
        projection.perspective((float) Math.toRadians(45.0f), 800.0f / 600.0f, 0.1f, 100.0f);

        while(!glfwWindowShouldClose(window.getHandle())) {

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            shader.useProgram();

            // Set matrices
            int modelLoc = glGetUniformLocation(shader.getProgramId(), "model");
            int viewLoc = glGetUniformLocation(shader.getProgramId(), "view");
            int projectionLoc = glGetUniformLocation(shader.getProgramId(), "projection");

            modelTransform.setRotation((new Vector3f((float) (glfwGetTime() * Math.toRadians(50.0f)))));

            glUniformMatrix4fv(modelLoc, false, modelTransform.getModelMatrix().get(new float[16]));
            glUniformMatrix4fv(viewLoc, false, view.get(new float[16]));
            glUniformMatrix4fv(projectionLoc, false, projection.get(new float[16]));

            shader.setInt("customTexture", 0);

            renderManager.renderModel(model, texture);

            glfwSwapBuffers(window.getHandle());
            glfwPollEvents();
        }

        shader.freeProgram();
        renderManager.dispose();
        modelLoader.cleanup();
        window.destroy();
        glfwTerminate();
    }


}
