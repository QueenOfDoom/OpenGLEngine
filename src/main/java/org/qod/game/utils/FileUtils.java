package org.qod.game.utils;

import org.qod.game.Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class FileUtils {
    private FileUtils() {}

    public static String loadAsString(String path) {
        StringBuilder builder = new StringBuilder();
        try {
            File file = new File(Objects.requireNonNull(Game.class.getClassLoader().getResource(path)).toString().substring(5));
            Game.logger.debug("Loading File from '{}'", file.toString());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String buffer;
            while((buffer = reader.readLine()) != null) {
                builder.append(buffer).append('\n');
            }
            reader.close();
        } catch (IOException e) {
            Game.logger.error("File: '{}' not found!", path);
            throw new RuntimeException("File not Found - Cannot Continue");
        }
        return builder.toString();
    }
}
