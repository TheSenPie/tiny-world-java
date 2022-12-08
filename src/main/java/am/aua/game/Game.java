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

    private Entity cube;

    @Override
    public void create() {
        Loader loader = new Loader();

        renderer = new Renderer();

        camera = new Camera();
        light = new Light(new Vector3f(20000,20000,2000), new Vector3f(1,1,1));
        terrain = new Terrain(0,0, loader, new Texture(loader.loadTexture("grass.png")));

        float[] vertices = {
                .5f, .5f, .5f,
                -.5f, .5f, .5f,
                -.5f,-.5f, .5f,
                .5f,-.5f, .5f,
                // v0,v1,v2,v3 (front)

                .5f, .5f, .5f,
                .5f,-.5f, .5f,
                .5f,-.5f,-.5f,
                .5f, .5f,-.5f,
                // v0,v3,v4,v5 (right)

                .5f, .5f, .5f,
                .5f, .5f,-.5f,
                -.5f, .5f,-.5f,
                -.5f, .5f, .5f,
                // v0,v5,v6,v1 (top)

                // position
                -.5f, .5f, .5f,
                -.5f, .5f,-.5f,
                -.5f,-.5f,-.5f,
                -.5f,-.5f, .5f,
                // v1,v6,v7,v2 (left)

                // position
                -.5f,-.5f,-.5f,
                .5f,-.5f,-.5f,
                .5f,-.5f, .5f,
                -.5f,-.5f, .5f,
                // v7,v4,v3,v2 (bottom)

                // position
                .5f,-.5f,-.5f,
                -.5f,-.5f,-.5f,
                -.5f, .5f,-.5f,
                .5f, .5f,-.5f,
                // v4,v7,v6,v5 (back)
        };

        float[] textureCoords = {
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0
        };

        float[] normals = {
                // normal
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                // v0,v1,v2,v3 (front)

                // normal
                1, 0, 0,
                1, 0, 0,
                1, 0, 0,
                // v0,v3,v4,v5 (right)

                // normal
                0, 1, 0,
                0, 1, 0,
                0, 1, 0,
                0, 1, 0,
                // v0,v5,v6,v1 (top)

                // normal
                -1, 0, 0,
                -1, 0, 0,
                -1, 0, 0,
                -1, 0, 0,
                // v1,v6,v7,v2 (left)

                // normal
                0,-1, 0,
                0,-1, 0,
                0,-1, 0,
                0,-1, 0,
                // v7,v4,v3,v2 (bottom)

                // normal
                0, 0,-1,
                0, 0,-1,
                0, 0,-1,
                0, 0,-1,
                // v4,v7,v6,v5 (back)
        };

        int[] indices = {
                0, 1, 2,   2, 3, 0,    // v0-v1-v2, v2-v3-v0 (front)
                4, 5, 6,   6, 7, 4,    // v0-v3-v4, v4-v5-v0 (right)
                8, 9,10,  10,11, 8,    // v0-v5-v6, v6-v1-v0 (top)
                12,13,14,  14,15,12,    // v1-v6-v7, v7-v2-v1 (left)
                16,17,18,  18,19,16,    // v7-v4-v3, v3-v2-v7 (bottom)
                20,21,22,  22,23,20     // v4-v7-v6, v6-v5-v4 (back)
        };

        RawModel model = loader.loadToVAO(vertices, textureCoords, normals, indices);

        TexturedModel staticModel = new TexturedModel(model, new Texture(loader.loadTexture("grass.png")));

        cube = new Entity(staticModel, new Vector3f(0,0,-5),0,0,0,1);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void update() {
        if (Keyboard.isKeyDown(GLFW_KEY_T)) {
            renderer.setWireframe(!renderer.isWireframe());
        }

        if (Mouse.isKeyDown(GLFW_MOUSE_BUTTON_LEFT)) {
            camera.setYaw(camera.getYaw() - Mouse.getDelta().x * 0.01f);
            camera.setRoll(camera.getRoll() + Mouse.getDelta().y * 0.01f);
        }

        camera.move();
    }

    @Override
    public void render() {
        renderer.processTerrain(terrain);
        renderer.processEntity(cube);
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
