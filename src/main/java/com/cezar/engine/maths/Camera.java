package com.cezar.engine.maths;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position;
    private Vector3f front; // direction vector (where camera is looking)
    private Vector3f up; // camera's up vector
    private Vector3f right; // camera's right vector (for strafing)

    private Vector3f worldUp; // world up vector (usually 0, 1, 0)

    private Matrix4f lookAt;

    private float yaw;
    private float pitch;
    private float mouseSensitivity = 0.1f;

    public Camera() {
        position = new Vector3f(0.0f, 0.0f, 3.0f);
        front = new Vector3f(0.0f, 0.0f, -1.0f); // looking down negative Z axis
        worldUp = new Vector3f(0.0f, 1.0f, 0.0f);

        updateCameraVectors();
        updateViewMatrix();
    }

    /**
     * Updates the camera's right and up vectors based on current front direction
     */
    private void updateCameraVectors() {
        Vector3f direction = new Vector3f();
        direction.x = (float)(Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        direction.y = (float)Math.sin(Math.toRadians(pitch));
        direction.z = (float)(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front = direction.normalize();

        // Calculate right vector
        right = new Vector3f(front).cross(worldUp).normalize();
        // Calculate up vector
        up = new Vector3f(right).cross(front).normalize();
    }

    /**
     * Updates the view matrix based on current camera position and orientation
     */
    public void updateViewMatrix() {
        // LookAt expects position, target (position + front), and up vector
        Vector3f target = new Vector3f(position).add(front);
        lookAt = new Matrix4f().lookAt(position, target, up);
    }

    /**
     * Move camera forward by the specified distance
     * @param distance Distance to move
     */
    public void moveForward(float distance) {
        position.add(new Vector3f(front).mul(distance));
    }

    /**
     * Move camera backward by the specified distance
     * @param distance Distance to move
     */
    public void moveBackward(float distance) {
        position.sub(new Vector3f(front).mul(distance));
    }

    /**
     * Move camera right by the specified distance (strafe right)
     * @param distance Distance to move
     */
    public void moveRight(float distance) {
        position.add(new Vector3f(right).mul(distance));
    }

    /**
     * Move camera left by the specified distance (strafe left)
     * @param distance Distance to move
     */
    public void moveLeft(float distance) {
        position.sub(new Vector3f(right).mul(distance));
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getFront() {
        return front;
    }

    public void setFront(Vector3f front) {
        this.front = front;
    }

    public Vector3f getRight() {
        return right;
    }

    public Vector3f getUp() {
        return up;
    }

    public Vector3f getWorldUp() {
        return worldUp;
    }

    public void setWorldUp(Vector3f worldUp) {
        this.worldUp = worldUp;
        updateCameraVectors();
    }

    public Matrix4f getLookAt() {
        return lookAt;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getMouseSensitivity() {
        return mouseSensitivity;
    }

    public void setMouseSensitivity(float mouseSensitivity) {
        this.mouseSensitivity = mouseSensitivity;
    }

    /**
     * Process mouse movement to update camera orientation
     * @param xOffset Mouse X offset
     * @param yOffset Mouse Y offset
     * @param constrainPitch Whether to constrain pitch to prevent flip
     */
    public void processMouseMovement(float xOffset, float yOffset, boolean constrainPitch) {
        xOffset *= mouseSensitivity;
        yOffset *= mouseSensitivity;

        yaw += xOffset;
        pitch += yOffset;

        if (constrainPitch) {
            if (pitch > 89.0f) pitch = 89.0f;
            if (pitch < -89.0f) pitch = -89.0f;
        }

        updateCameraVectors();
    }
}

