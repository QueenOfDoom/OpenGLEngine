package org.qod.game.utils;

import org.qod.game.Game;

import static org.lwjgl.opengl.GL20.*;

public class ShaderUtils {
    private ShaderUtils() {}

    public static int load(String vertexPath, String fragmentPath) {
        Game.logger.info("Loading Shaders: '{}' and '{}'", vertexPath, fragmentPath);
        String vertexShader = FileUtils.loadAsString(vertexPath);
        String fragmentShader = FileUtils.loadAsString(fragmentPath);
        return create(vertexShader, fragmentShader);
    }

    private static int create(String vertexShader, String fragmentShader) {
        int program = glCreateProgram();
        int vertID = glCreateShader(GL_VERTEX_SHADER);
        int fragID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertID, vertexShader);
        glShaderSource(fragID, fragmentShader);

        glCompileShader(vertID);
        if(glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
            Game.logger.error("Failed to compile Vertex Shader.");
            Game.logger.error(glGetShaderInfoLog(vertID));
            return -1;
        } else {
            Game.logger.info("Compiled Vertex Shader.");
        }

        glCompileShader(fragID);
        if(glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
            Game.logger.error("Failed to compile Fragment Shader.");
            Game.logger.error(glGetShaderInfoLog(fragID));
            return -1;
        } else {
            Game.logger.info("Compiled Fragment Shader.");
        }

        glAttachShader(program, vertID);
        glAttachShader(program, fragID);
        glLinkProgram(program);
        glValidateProgram(program);

        glDeleteShader(vertID);
        glDeleteShader(fragID);

        return program;
    }
}
