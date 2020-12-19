package org.qod.game.graphics;

import org.qod.game.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
    private int width, height;
    private int texture;

    public Texture(String path) {
        texture = load(path);
    }

    private int load(String path) {
        int[] pixels = null;
        try {
            File file = new File(Objects.requireNonNull(Game.class.getClassLoader().getResource(path)).toString().substring(5));
            BufferedImage image = ImageIO.read(file);
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            Game.logger.error("Could not load Texture: '{}'!", path);
            return -1;
        }

        // Reordering of the Pixel Components
        int[] data = new int[width * height];
        for(int i = 0; i < data.length; i++) {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = pixels[i] & 0xff;

            data[i] = (a << 24 | b << 16 | g << 8 | r);
        }

        int result = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, result);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        IntBuffer dataBuffer = ByteBuffer.allocateDirect(4 * pixels.length).order(ByteOrder.nativeOrder()).asIntBuffer();
        dataBuffer.put(data).flip();
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, dataBuffer);
        glBindTexture(GL_TEXTURE_2D, 0);
        return result;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
