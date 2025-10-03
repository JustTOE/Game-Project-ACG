package com.cezar.main;

import com.cezar.renderer.RenderManager;
import com.cezar.renderer.Shader;
import com.cezar.renderer.model.Model;
import com.cezar.renderer.model.ModelLoader;
import com.cezar.window.Window;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    public static float[] vertices = new float[] {
            0.5f,  0.5f, 0.0f,      1.0f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.0f,      0.0f, 1.0f, 0.0f,
            -0.5f, -0.5f, 0.0f,     0.0f, 0.0f, 1.0f,
            -0.5f,  0.5f, 0.0f,      1.0f, 1.0f, 1.0f
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
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glViewport(0, 0, 800, 600);

        Shader shader = new Shader("src/main/resources/shaders/vertex.glsl", "src/main/resources/shaders/fragment.glsl");
        RenderManager renderManager = new RenderManager(window, shader);
        ModelLoader modelLoader = new ModelLoader();
        Model model = modelLoader.loadModel(vertices, indices);

        while(!glfwWindowShouldClose(window.getHandle())) {
            //shader.updatePulsatingColor(); // just some testing, ignore this
            renderManager.renderModel(model);

            glfwSwapBuffers(window.getHandle());
            glfwPollEvents();
        }

        renderManager.dispose();
        modelLoader.cleanup();
        window.destroy();
        glfwTerminate();
    }


}
