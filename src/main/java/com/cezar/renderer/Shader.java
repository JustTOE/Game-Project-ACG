package com.cezar.renderer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public Shader(String vertexPath, String fragmentPath) {

        // This creates a "program" that runs on the GPU.
        // IMPORTANT: the method returns a HANDLE or a "pointer, more specifically, a location into the memory where that program resides
        programId = glCreateProgram();

        // We load the shaders from their respective files and attribute them a type. Pretty self explaining
        vertexShaderId = loadShader(vertexPath, GL_VERTEX_SHADER);
        fragmentShaderId = loadShader(fragmentPath, GL_FRAGMENT_SHADER);

        // The vertex shader as well as the fragment shader are "appended" to the program since they can't run independently
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);

        // This links the previous shaders into one final shader program object
        glLinkProgram(programId);

        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking shader program: " + glGetProgramInfoLog(programId));
        }

        glValidateProgram(programId);

        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating shader program: " + glGetProgramInfoLog(programId));
        }

    }

    public void updatePulsatingColor() {
        bind();
        double timeValue = glfwGetTime();
        double greenValue = (Math.sin(timeValue) / 2.0) + 0.5;
        int colorLocation = glGetUniformLocation(programId, "customColor");
        glUniform4f(colorLocation, 0.0f, (float)greenValue, 0.0f, 1.0f);
    }

    private int loadShader(String filePath, int type) {
        StringBuilder shaderSource = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading shader file: " + filePath, e);
        }

        int shaderId = glCreateShader(type);
        glShaderSource(shaderId, shaderSource);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Error compiling shader " + filePath + ": " + glGetShaderInfoLog(shaderId));
        }

        return shaderId;
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void dispose() {
        unbind();

        if (programId != 0) {
            glDetachShader(programId, vertexShaderId);
            glDetachShader(programId, fragmentShaderId);

            glDeleteShader(vertexShaderId);
            glDeleteShader(fragmentShaderId);

            glDeleteProgram(programId);
        }
    }

    public int getProgramId() {
        return programId;
    }
}
