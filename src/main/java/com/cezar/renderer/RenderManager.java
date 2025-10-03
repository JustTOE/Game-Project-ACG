package com.cezar.renderer;

import com.cezar.renderer.model.Model;
import com.cezar.window.Window;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class RenderManager {
    private final Window window;
    private final Shader shader;

    public RenderManager(Window window, Shader shader) {
        this.window = window;
        this.shader = shader;
    }

    public void renderModel(Model model) {
        clear();
        shader.bind();

        // We bind the VAO to the context
        glBindVertexArray(model.getVAO());
        // And then enable the first attribute of the VAO
        // Remember that in this VAO we are storing THE POSITION of each vertex
        // If you check the vertex.glsl file you will see that we specify the location of the position vertex attribute as layout (location = 0)
        // This sets the location of the vertex attribute to 0 and since we want to pass data to this vertex attribute, we pass in 0
        glEnableVertexAttribArray(0);

        glDrawArrays(GL_TRIANGLES, 0, model.getVertex_count());

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shader.unbind();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void dispose() {
        shader.dispose();
    }

}
