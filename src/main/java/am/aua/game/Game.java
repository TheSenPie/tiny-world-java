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
import am.aua.water.WaterFrameBuffers;
import am.aua.water.WaterTile;
import org.joml.Vector3f;

import static am.aua.rendererEngine.Window.Keyboard;
import static am.aua.rendererEngine.Window.Mouse;
import static org.lwjgl.glfw.GLFW.*;

public class Game implements ApplicationListener {
    private Renderer renderer;

    private Camera camera;

    private Light light;
    private Terrain terrain;
    private WaterTile water;
    private WaterFrameBuffers fbos;

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
        water = new WaterTile(
                0, 0, loader,
                new Texture(loader.loadTexture("water.png"))
        );
        fbos = new WaterFrameBuffers();
    }

    @Override
    public void dispose() {
        fbos.dispose();
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
        fbos.bindReflectionFrameBuffer();
        renderer.prepare();
        renderer.processTerrain(terrain);
        // process entities
        renderer.render(light, camera);
        fbos.unbindCurrentFrameBuffer();

        renderer.processTerrain(terrain);
        // process entities
        renderer.processWater(water);
        renderer.prepare();
        renderer.render(light, camera);
        renderer.renderWater(camera);
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
