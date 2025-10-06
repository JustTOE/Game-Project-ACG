package com.cezar.engine.window;

import com.cezar.engine.utils.OSUtils;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    private int width;
    private int height;
    private CharSequence title;
    private final long handle;

    public Window(int width, int height, CharSequence title, long monitor, long share) {
        this.width = width;
        this.height = height;
        this.title = title;

        glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        // May not be necessary
        //glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        // MacOS-only option
        if(OSUtils.IS_OS_MAC) {
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        }

        handle = glfwCreateWindow(width, height, title, monitor, share);
        if(handle == NULL) {
            throw new RuntimeException("Failed to create window!");
        }
        glfwMakeContextCurrent(handle);
        glfwSetFramebufferSizeCallback(handle, this::frame_buffer_size_callback);
    }

    public void destroy() {
        glfwDestroyWindow(handle);
    }

    public void frame_buffer_size_callback(long window, int width, int height) {
        glViewport(0, 0, width, height);
    }


    public void setWidth(int width) {
        glfwSetWindowSize(handle, width, this.height);
        this.width = width;
    }

    public void setHeight(int height) {
        glfwSetWindowSize(handle, this.width, height);
        this.height = height;
    }

    public void setTitle(CharSequence title) {
        glfwSetWindowTitle(handle, title);
        this.title = title;
    }

    public long getHandle() {
        return handle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public CharSequence getTitle() {
        return title;
    }
}
