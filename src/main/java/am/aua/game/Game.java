package am.aua.game;

import am.aua.entities.Camera;
import am.aua.entities.Light;
import am.aua.models.Entity;
import am.aua.models.RawModel;
import am.aua.models.TexturedModel;
import am.aua.rendererEngine.Loader;
import am.aua.rendererEngine.Renderer;
import am.aua.rendererEngine.Window;
import am.aua.terrains.Terrain;
import am.aua.textures.Texture;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static am.aua.rendererEngine.Window.Keyboard;
import static am.aua.rendererEngine.Window.Mouse;
import static org.lwjgl.glfw.GLFW.*;

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
        light = new Light(new Vector3f(0,10,0), new Vector3f(1,1,1));
        terrain = new Terrain(
                0, 0, loader,
                new Texture(loader.loadTexture("grass.png")),
                new Texture(loader.loadTexture("height_map.png"))
        );
    }

    @Override
    public void dispose() {

    }

    @Override
    public void update() {
        if (Keyboard.isPressed(GLFW_KEY_T)) {
            renderer.setWireframe(!renderer.isWireframe());
        }

        camera.move();
        camera.update();
    }

    @Override
    public void render() {
        renderer.processTerrain(terrain);
        // render entities after
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
