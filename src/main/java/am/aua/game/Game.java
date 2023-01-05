package am.aua.game;

import am.aua.entities.Camera;
import am.aua.entities.Light;
import am.aua.rendererEngine.Loader;
import am.aua.rendererEngine.Renderer;
import am.aua.rendererEngine.Window;
import am.aua.terrains.Terrain;
import am.aua.water.WaterFrameBuffers;
import am.aua.water.WaterTile;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import static am.aua.rendererEngine.Window.Keyboard;
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

        fbos = new WaterFrameBuffers();

        camera = new Camera();
        camera.setPosition(-0.8405f, 1.028f,  1.840f);
        camera.setYaw(0);
        camera.update();

        renderer = new Renderer(loader, fbos, camera);
        light = new Light(new Vector3f(0,10,0), new Vector3f(1,1,1));
        terrain = new Terrain(0, 0, loader);
        water = new WaterTile(0, 0, loader);
        water.setY(0.14f);
    }

    @Override
    public void dispose() {
        fbos.dispose();
        renderer.dispose();
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
        GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

        // render to reflection framebuffer
        fbos.bindReflectionFrameBuffer();
        float distance = 2.0f * (camera.getPosition().y - water.getY()); // TODO: 30.12.22 take into account height of water
        camera.getPosition().y -= distance;
        camera.invertPitch();
        camera.update();
        renderer.processTerrain(terrain);
        // process entities
        renderer.render(camera, light, new Vector4f(0, 1, 0, -water.getY())); // TODO: 30.12.22 take into account height of water
        camera.getPosition().y += distance;
        camera.invertPitch();
        camera.update();

        // render to refraction framebuffer
        fbos.bindRefractionFrameBuffer();
        renderer.processTerrain(terrain);
        // process entities
        renderer.render(camera, light, new Vector4f(0, -1, 0, water.getY())); // TODO: 30.12.22 take into account height of water

        // render to screen
        GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
        fbos.unbindCurrentFrameBuffer();
        renderer.processTerrain(terrain);
        // process entities
        renderer.processWater(water);
        renderer.render(camera, light,  new Vector4f(0, -1, 0, 1000)); // patch in case gl disable is ignored for clipping
        renderer.renderWater(camera, light);
    }

    @Override
    public void resize() {
        renderer.resize(camera);
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
