package com.cezar.renderer.texture;

import com.sun.source.doctree.TextTree;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class TextureLoader {

    public Texture loadTexture(String fileName) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer nrChannels = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(true);
        ByteBuffer imageData = stbi_load(fileName, width, height, nrChannels, 0);
        if (imageData == null) {
            throw new RuntimeException("Failed to load texture: " + fileName);
        }

        return new Texture(width.get(0), height.get(0), nrChannels.get(0), imageData);
    }

    public int generateTexture(Texture texture) {
        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Set texture wrapping and filtering parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        int format = texture.getNrChannels() == 4 ? GL_RGBA : GL_RGB;
        glTexImage2D(GL_TEXTURE_2D, 0, format, texture.getWidth(), texture.getHeight(), 0, format, GL_UNSIGNED_BYTE, texture.getImageData());
        glGenerateMipmap(GL_TEXTURE_2D);

        stbi_image_free(texture.getImageData());

        return textureId;
    }
}
