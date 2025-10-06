package com.cezar.engine.renderer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private final int programId;

    private final List<Integer> shaderHandles = new ArrayList<>();

    public Shader() {
        programId = glCreateProgram();
    }

    public void attach(String path, int type) {
        int shaderId = loadShaderFromFile(path, type);
        glAttachShader(programId, shaderId);
        shaderHandles.add(shaderId);
    }

    public void link() {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking shader program: " + glGetProgramInfoLog(programId));
        }
    }

    public void validate() {
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating shader program: " + glGetProgramInfoLog(programId));
        }
    }

    private int loadShaderFromFile(String filePath, int type) {
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

    public void useProgram() {
        glUseProgram(programId);
    }

    public void freeProgram() {
        glUseProgram(0);
    }

    public void dispose() {
        useProgram();

        if (programId != 0) {
            for(int shader : shaderHandles) {
                glDetachShader(programId, shader);
                glDeleteShader(shader);
            }

            glDeleteProgram(programId);
        }
    }

    public int getProgramId() {
        return programId;
    }

    public void setBoolean(String name, boolean value) {
        glUniform1i(glGetUniformLocation(programId, name), value ? 1 : 0);
    }

    public void setFloat(String name, float value) {
        glUniform1f(glGetUniformLocation(programId, name), value);
    }

    public void setInt(String name, int value) {
        glUniform1i(glGetUniformLocation(programId, name), value);
    }
}
