package com.cezar.renderer;

import com.cezar.renderer.model.Model;
import com.cezar.renderer.texture.Texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class RenderManager {
    private final Shader shader;

    public RenderManager(Shader shader) {
        this.shader = shader;
    }

    public void renderModel(Model model, Texture texture) {
        clear();
        shader.useProgram();

        glBindTexture(GL_TEXTURE_2D, texture.getTextureId());
        glBindVertexArray(model.getVAO());
        shader.validate();

        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);

        shader.freeProgram();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void dispose() {
        shader.dispose();
    }

}
