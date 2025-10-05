package com.cezar.renderer;

import com.cezar.renderer.model.Model;
import com.cezar.renderer.texture.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.channels.FileLock;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
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

        doMaths(model, texture);

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

    // Rotate and Scale
    public void doMaths(Model model, Texture texture) {
        Matrix4f trans = new Matrix4f(); // Creates identity matrix by default
        trans.rotate((float) Math.toRadians(90.0f), 0.0f, 0.0f, 1.0f);
        trans.scale(0.5f, 0.5f, 0.5f);

        int transformLoc = glGetUniformLocation(model.getVAO(), "transform");
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        trans.get(floatBuffer);

        glUniformMatrix4fv(transformLoc, false, floatBuffer);
    }

}
