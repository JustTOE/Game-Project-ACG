package com.cezar.engine.renderer;

import com.cezar.engine.renderer.model.Model;
import com.cezar.engine.renderer.texture.Texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class RenderManager {
    private final Shader shader;

    public RenderManager(Shader shader) {
        this.shader = shader;
    }

    public void renderModel(Model model, Texture texture) {
        glBindTexture(GL_TEXTURE_2D, texture.getTextureId());
        glBindVertexArray(model.getVAO());

        if (model.useIndexedRendering()) {
            glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
        } else {
            glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
        }

        glBindVertexArray(0);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void dispose() {
        shader.dispose();
    }

}
