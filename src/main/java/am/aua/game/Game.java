package am.aua.game;

import am.aua.entities.Camera;
import am.aua.entities.Light;
import am.aua.rendererEngine.Loader;
import am.aua.rendererEngine.Renderer;
import am.aua.rendererEngine.Window;
import am.aua.terrains.Terrain;
import am.aua.textures.Texture;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static am.aua.rendererEngine.Window.Keyboard;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Game implements ApplicationListener {
    private Renderer renderer;

    private Camera camera;

    private Light light;
    private Terrain terrain;

    @Override
    public void create() {
        Loader loader = new Loader();

        renderer = new Renderer();

        camera = new Camera();
        light = new Light(new Vector3f(20000,20000,2000), new Vector3f(1,1,1));
        terrain = new Terrain(0,0, loader, new Texture(loader.loadTexture("grass.png")));
    }

    @Override
    public void dispose() {

    }

    @Override
    public void update() {
        if (Keyboard.isKeyDown(GLFW_KEY_T)) {
            renderer.setWireframe(!renderer.isWireframe());
        }

        camera.move();
    }

    @Override
    public void render() {
        renderer.processTerrain(terrain);
        renderer.render(light, camera);
    }

    @Override
    public void resize() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    public static void main(String[] args) {
        Game game = new Game();
        Window.create(game);
        Window.loop();
    }
}
