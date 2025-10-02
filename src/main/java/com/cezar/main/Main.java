package com.cezar.main;

import com.cezar.window.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {
    public static void main(String[] args) {
        init();
        loop();

        glfwTerminate();
    }

    private static void init() {

        if(!glfwInit()) {
            System.out.println("Unable to initialize GLFW!");
            // Terminate if not successfully initialized
            glfwTerminate();
        }
    }

    private static void loop() {
        Window window = new Window(800, 600, "Window", NULL, NULL);
        while(!glfwWindowShouldClose(window.getHandle())) {
            glfwSwapBuffers(window.getHandle());
            glfwPollEvents();
        }
    }
}
