package org.qod.game.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.qod.game.Game;
import org.qod.game.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public enum Shader {
    BACKGROUND("shaders/background.vert", "shaders/background.frag");

    public static final int VERTEX_ATTRIB = 0;
    public static final int TEX_COORDINATES = 1;

    private final int ID;
    private boolean enable = false;
    private final Map<String, Integer> locationCache = new HashMap<>();

    Shader(String vertex, String fragment) {
        ID = ShaderUtils.load(vertex, fragment);
    }

    public static void loadAll() {
        // BACKGROUND
        glActiveTexture(GL_TEXTURE1);
        Matrix4f projectionMatrix = new Matrix4f().ortho(-16.0f, 16.0f, -9.0f, 9.0f, -1.0f, 1.0f);
        BACKGROUND.setUniformMatrix4f("projection_matrix", projectionMatrix);
        BACKGROUND.setUniform1i("active_texture", 1);
    }

    public int getUniform(String name) {
        if(locationCache.containsKey(name))
            return locationCache.get(name);
        int result = glGetUniformLocation(ID, name);
        if(result == -1) {
            Game.logger.warn("Could not find uniform variable {}!", name);
        } else {
            Game.logger.info("Put {} into the Location Cache!", name);
            locationCache.put(name, result);
        }
        return result;
    }

    public void setUniform1i(String name, int value) {
        if(!enable) enable();
        glUniform1i(getUniform(name), value);
    }

    public void setUniform1f(String name, float value) {
        if(!enable) enable();
        glUniform1f(getUniform(name), value);
    }

    public void setUniform2f(String name, float alpha, float beta) {
        if(!enable) enable();
        glUniform2f(getUniform(name), alpha, beta);
    }

    public void setUniform3f(String name, Vector3f vector) {
        if(!enable) enable();
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }

    public void setUniformMatrix4f(String name, Matrix4f matrix) {
        if(!enable) enable();
        // Matrix Size: 4x4, Float Size: 4 Bytes
        FloatBuffer buffer = ByteBuffer.allocateDirect(4 * 4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        glUniformMatrix4fv(getUniform(name), false, matrix.get(buffer));
    }

    public void enable() {
        glUseProgram(ID);
        enable = true;
    }

    public void disable() {
        glUseProgram(0);
        enable = false;
    }
}
