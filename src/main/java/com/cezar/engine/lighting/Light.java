package com.cezar.engine.lighting;

import org.joml.Vector3f;

/**
 * Represents a light source with position and color properties.
 */
public class Light {
    private Vector3f position;
    private Vector3f color;
    private float intensity;

    public Light(Vector3f position, Vector3f color) {
        this(position, color, 1.0f);
    }

    public Light(Vector3f position, Vector3f color, float intensity) {
        this.position = new Vector3f(position);
        this.color = new Vector3f(color);
        this.intensity = intensity;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color.set(color);
    }

    public void setColor(float r, float g, float b) {
        this.color.set(r, g, b);
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}