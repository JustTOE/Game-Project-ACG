package com.cezar.renderer.texture;

import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;

public class Texture {

    private final int width;
    private final int height;
    private final int nrChannels;
    private final ByteBuffer imageData;
    private int textureId;

    public Texture(int width, int height, int nrChannels, ByteBuffer imageData) {
        this.width = width;
        this.height = height;
        this.nrChannels = nrChannels;
        this.imageData = imageData;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getNrChannels() {
        return nrChannels;
    }

    public ByteBuffer getImageData() {
        return imageData;
    }

    public int getTextureId() {
        return textureId;
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }
}
