package com.cezar.engine.window;

import com.cezar.engine.maths.Camera;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Handles keyboard and mouse input for the game engine.
 * Processes WASD movement and mouse look.
 */
public class InputManager {
    private final Camera camera;

    private float cameraSpeed = 2.5f; // Units per second

    // Mouse tracking
    private float lastX = 400.0f;
    private float lastY = 300.0f;
    private boolean firstMouse = true;

    private GLFWCursorPosCallback cursorCallback;
    private GLFWKeyCallback keyCallback;

    /**
     * Creates an InputManager bound to a specific camera
     * @param camera The camera to control
     */
    public InputManager(Camera camera) {
        this.camera = camera;
    }

    /**
     * Initialize mouse and keyboard callbacks for the window
     * @param window The window to attach callbacks to
     */
    public void setupCallbacks(Window window) {
        cursorCallback = GLFWCursorPosCallback.create((windowHandle, xPos, yPos) -> handleMouseMovement(xPos, yPos));
        glfwSetCursorPosCallback(window.getHandle(), cursorCallback);

        keyCallback = GLFWKeyCallback.create((windowHandle, key, scancode, action, mods) -> handleKeyPress(windowHandle, key, action));
        glfwSetKeyCallback(window.getHandle(), keyCallback);
    }

    /**
     * Process keyboard input and update camera position accordingly.
     * Uses deltaTime to ensure consistent movement speed across different frame rates.
     *
     * @param window The GLFW window to poll input from
     * @param deltaTime Time elapsed since last frame (in seconds)
     */
    public void processInput(Window window, float deltaTime) {
        long windowHandle = window.getHandle();

        // Calculate actual movement speed for this frame
        float frameSpeed = cameraSpeed * deltaTime;

        // WASD movement
        if (glfwGetKey(windowHandle, GLFW_KEY_W) == GLFW_PRESS) {
            camera.moveForward(frameSpeed);
        }
        if (glfwGetKey(windowHandle, GLFW_KEY_S) == GLFW_PRESS) {
            camera.moveBackward(frameSpeed);
        }
        if (glfwGetKey(windowHandle, GLFW_KEY_A) == GLFW_PRESS) {
            camera.moveLeft(frameSpeed);
        }
        if (glfwGetKey(windowHandle, GLFW_KEY_D) == GLFW_PRESS) {
            camera.moveRight(frameSpeed);
        }

        // Update camera view matrix after movement
        camera.updateViewMatrix();
    }

    /**
     * Handles mouse movement and updates camera orientation
     * @param xPos Current mouse X position
     * @param yPos Current mouse Y position
     */
    private void handleMouseMovement(double xPos, double yPos) {
        if (firstMouse) {
            lastX = (float) xPos;
            lastY = (float) yPos;
            firstMouse = false;
        }

        float xOffset = (float) xPos - lastX;
        float yOffset = lastY - (float) yPos; // Reversed: y-coordinates go from bottom to top
        lastX = (float) xPos;
        lastY = (float) yPos;

        camera.processMouseMovement(xOffset, yOffset, true);
    }

    /**
     * Handles discrete key press events (not continuous input)
     * @param windowHandle The window handle
     * @param key The key code
     * @param action The action (press, release, repeat)
     */
    private void handleKeyPress(long windowHandle, int key, int action) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
            glfwSetWindowShouldClose(windowHandle, true);
        }
    }

    /**
     * Cleanup callback resources
     */
    public void dispose() {
        if (cursorCallback != null) {
            cursorCallback.free();
        }
        if (keyCallback != null) {
            keyCallback.free();
        }
    }

    /**
     * Get the current camera movement speed
     * @return Speed in units per second
     */
    public float getCameraSpeed() {
        return cameraSpeed;
    }

    /**
     * Set the camera movement speed
     * @param cameraSpeed Speed in units per second
     */
    public void setCameraSpeed(float cameraSpeed) {
        this.cameraSpeed = cameraSpeed;
    }
}
