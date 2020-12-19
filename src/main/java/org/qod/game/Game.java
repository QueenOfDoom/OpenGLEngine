package org.qod.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.joml.Matrix4f;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import org.qod.game.graphics.Shader;
import org.qod.game.input.KeyInput;
import org.qod.game.level.Level;

import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Game {

    // Logger
    public static final Logger logger = LogManager.getLogger();
    public static Game game;

    // Window Handle
    private long window;

    private Level level;

    // Window Constants
    public static final int WIDTH = 640, HEIGHT = 400;
    public static final String TITLE = "OpenGL Game";

    // Sets up and runs
    public void run() {
        logger.info("Using LWJGL {}", Version.getVersion());

        // Run Game Methods
        init();
        loop();

        // Free Call Backs / Close Window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate Application
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    // LWJGL Setup Method
    public void init() {
        // Create Error Callback
        GLFWErrorCallback.createPrint(System.err).set();
        if(!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW!");

        // Configure Window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Create Window
        window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);
        if(window == NULL) throw new RuntimeException("Failed to create Window!");

        // KeyListener
        glfwSetKeyCallback(window, new KeyInput());

        try(MemoryStack stack = MemoryStack.stackPush()) {
            // Create Pointers for Width and Height
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            // Fill Pointers with Values
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get Resolution of Primary Mon
            GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if(mode == null) throw new RuntimeException("Failed to get Primary Monitor Resolution!");

            // Center Window
            glfwSetWindowPos(window, (mode.width() - pWidth.get(0))/2,(mode.height() - pHeight.get(0))/2);
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Enable VSync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    // Game Loop
    public void loop() {
        // Makes OpenGL Bindings available for GLFW
        GL.createCapabilities();

        logger.info("Using OpenGL {}", glGetString(GL_VERSION));

        // Set Clear Color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // The Depth Test is a per-sample processing operation performed after the Fragment Shader
        // (and sometimes before). The Fragment's output depth value may be tested against the depth
        // of the sample being written to. If the test fails, the fragment is discarded. If the test passes,
        // the depth buffer will be updated with the fragment's output depth, unless a subsequent per-sample
        // operation prevents it (such as turning off depth writes).
        glEnable(GL_DEPTH_TEST);

        Shader.loadAll();

        level = new Level();

        while(!glfwWindowShouldClose(window)) {
            update();
            render();
        }
    }

    // Game Logic Updates
    private void update() {
        glfwPollEvents();
        if(KeyInput.keys[GLFW_KEY_ESCAPE])
            glfwSetWindowShouldClose(window, true);
    }

    // Game Graphics Updates
    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        level.render();
        checkErrors("Render - Loop");
        glfwSwapBuffers(window);
    }

    public void checkErrors(String descriptor) {
        int error = glGetError();
        if(error != GL_NO_ERROR)
            logger.error("OpenGL Error @[{}]: {}", descriptor, error);

    }

    public static void main(String[] args) {
        game = new Game();
        game.run();
    }
}
