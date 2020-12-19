package org.qod.game.level;

import org.qod.game.Game;
import org.qod.game.graphics.Shader;
import org.qod.game.graphics.Texture;
import org.qod.game.graphics.VertexArray;

public class Level {

    private VertexArray background;
    private Texture backgroundTexture;

    public Level() {
        float[] background_vertices = new float[]{
                -16.0f, -9.0f, 0.0f, // Bottom Left Corner
                -16.0f,  9.0f, 0.0f, // Top Left Corner
                 16.0f,  9.0f, 0.0f, // Top Right Corner
                 16.0f, -9.0f, 0.0f, // Bottom Right Corner
        };

        byte[] background_indices = new byte[]{
                0, 1, 2,
                2, 3, 0
        };

        float[] background_texture_coordinates = new float[]{
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        background = new VertexArray(background_vertices, background_indices, background_texture_coordinates);
        backgroundTexture = new Texture("images/background.png");
    }

    public void render() {
        backgroundTexture.bind();
        Shader.BACKGROUND.enable();
        background.render();
        Shader.BACKGROUND.disable();
        backgroundTexture.unbind();
    }
}
