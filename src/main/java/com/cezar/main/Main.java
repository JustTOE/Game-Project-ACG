package com.cezar.main;

import com.cezar.renderer.RenderManager;
import com.cezar.renderer.Shader;
import com.cezar.renderer.model.Model;
import com.cezar.renderer.model.ModelLoader;
import com.cezar.renderer.texture.Texture;
import com.cezar.renderer.texture.TextureLoader;
import com.cezar.window.Window;
import org.lwjgl.opengl.GL;
import org.w3c.dom.Text;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
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

        Shader shader = new Shader();
        shader.attach("src/main/resources/shaders/vertex.glsl", GL_VERTEX_SHADER);
        shader.attach("src/main/resources/shaders/fragment.glsl", GL_FRAGMENT_SHADER);
        shader.link();

        RenderManager renderManager = new RenderManager(shader);

        TextureLoader textureLoader = new TextureLoader();
        Texture texture = textureLoader.loadTexture("src/main/resources/textures/texture.jpg");
        int textureId = textureLoader.generateTexture(texture);
        texture.setTextureId(textureId);

        shader.useProgram();
        shader.setInt("customTexture", 0);
        shader.freeProgram();

        ModelLoader modelLoader = new ModelLoader();
        Model model = modelLoader.loadModel(vertices, indices);

        while(!glfwWindowShouldClose(window.getHandle())) {
            renderManager.renderModel(model, texture);

            glfwSwapBuffers(window.getHandle());
            glfwPollEvents();
        }

        renderManager.dispose();
        modelLoader.cleanup();
        window.destroy();
        glfwTerminate();
    }


}
