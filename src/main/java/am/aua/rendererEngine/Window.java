package am.aua.rendererEngine;

import am.aua.game.ApplicationListener;
import am.aua.utils.Maths;
import am.aua.utils.Time;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;

    private static long window;

    private static Vector2i size;

    private static ApplicationListener game;
    public static Mouse Mouse;
    public static Keyboard Keyboard;

    // timing variables
    private static long lastSecond;
    private static long frames, fps, lastFrame, frameDelta;

    public static void create(ApplicationListener applicationListener) {
        Mouse = new Mouse();
        Keyboard = new Keyboard();
        Window.game = applicationListener;


        lastFrame = Time.NOW();
        lastSecond = Time.NOW();

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        size = new Vector2i(SCREEN_WIDTH, SCREEN_HEIGHT);
        window = glfwCreateWindow(size.x, size.y, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // configure callbacks
        glfwSetFramebufferSizeCallback(window, new SizeCallback());
        glfwSetCursorPosCallback(window, new CursorCallback());
        // Setup a key and mouse callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, new KeyCallback());
        glfwSetMouseButtonCallback(window, new MouseCallback());

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private static void init () {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        game.create();
    }

    private static void update () {
        buttonArrayUpdate(GLFW_MOUSE_BUTTON_LAST, Mouse.buttons);
        buttonArrayUpdate(GLFW_KEY_LAST, Keyboard.keys);

        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        }

        game.update();

        // reset update delta
        Mouse.delta.zero();
    }

    private static void render () {
        frames++;
        game.render();
    }

    private static void dispose () {
        game.dispose();
        glfwTerminate();
    }

    private static void buttonArrayUpdate(int n, Button[] buttons) {
        for (int i = 0; i < n; i++) {
            buttons[i].pressed = buttons[i].down && !buttons[i].last;
            buttons[i].last = buttons[i].down;
        }
    }

    public static void loop () {
        init();

        while (!glfwWindowShouldClose(window)) {
            long now = Time.NOW();

            frameDelta = now - lastFrame;
            lastFrame = now;

            if (now - lastSecond > Time.NS_PER_SECOND) {
                fps = frames;
                frames = 0;
                lastSecond = now;

                System.out.println("FPS: " + fps);
            }

            update();
            render();
            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }

        dispose();
        System.exit(0);
    }

    public static void mouseSetGrabbed (boolean grabbed) {
        glfwSetInputMode(window, GLFW_CURSOR, grabbed ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
    }

    public static boolean mouseGetGrabbed () {
        return glfwGetInputMode(window, GLFW_CURSOR) == GLFW_CURSOR_DISABLED;
    }

    public static float getWidth() {
        return size.x;
    }

    public static float getHeight() {
        return size.y;
    }

    // returns time since last frame in seconds
    public static float getDelta() {
        return frameDelta / (float) Time.NS_PER_SECOND;
    }

    public static class Button {
        private boolean down;
        private boolean last;
        private boolean pressed;

        public boolean isDown() {
            return down;
        }
        public boolean isPressed() {
            return pressed;
        }
    }

    public static class Mouse {
        private Button[] buttons;
        private Vector2f position;
        private Vector2f delta;

        private Mouse () {
            buttons = new Button[GLFW_MOUSE_BUTTON_LAST];
            for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) {
                buttons[i] = new Button();
            }

            position = new Vector2f();
            delta = new Vector2f();
        }

        public Vector2f getPosition() {
            return position;
        }
        public Vector2f getDelta() {
            return delta;
        }

        public boolean isKeyDown (int button) {
            return buttons[button].down;
        }
    }

    public static class Keyboard {
        private Button keys[];

        private Keyboard () {
            keys = new Button[GLFW_KEY_LAST];
            for (int i = 0; i < GLFW_KEY_LAST; i++) {
                keys[i] = new Button();
            }
        }

        public boolean isKeyDown (int key) {
            return keys[key].down;
        }

        public boolean isPressed (int key) {
            return keys[key].pressed;
        }
    }


    private static class CursorCallback implements GLFWCursorPosCallbackI {

        @Override
        public void invoke(long window, double xpos, double ypos) {
            float xposf = (float) xpos;
            float yposf = (float) ypos;
            Mouse.delta.set(xposf, yposf).sub(Mouse.position);
            Mouse.delta.x = Maths.clamp(Mouse.delta.x, -100.0f, 100.0f);
            Mouse.delta.y = Maths.clamp(Mouse.delta.y, -100.0f, 100.0f);
            Mouse.position.set(xposf, yposf);
        }
    }
    private static class SizeCallback implements GLFWFramebufferSizeCallbackI {
        @Override
        public void invoke(long window, int width, int height) {
            glViewport(0, 0, width, height);
            size.set(width, height);
        }
    }

    private static class KeyCallback implements GLFWKeyCallbackI {

        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key < 0) {
                return;
            }

            switch (action) {
                case GLFW_PRESS:
                    Keyboard.keys[key].down = true;
                    break;
                case GLFW_RELEASE:
                    Keyboard.keys[key].down = false;
                    break;
                default:
                    break;
            }
        }
    }

    private static class MouseCallback implements GLFWMouseButtonCallbackI {

        @Override
        public void invoke(long window, int button, int action, int mods) {
            if (button < 0) {
                return;
            }

            switch (action) {
                case GLFW_PRESS:
                    Mouse.buttons[button].down = true;
                    break;
                case GLFW_RELEASE:
                    Mouse.buttons[button].down = false;
                    break;
                default:
                    break;
            }
        }
    }

}
