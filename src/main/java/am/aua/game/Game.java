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
import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

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
                -0.5f,0.5f,0,
                -0.5f,-0.5f,0,
                0.5f,-0.5f,0,
                0.5f,0.5f,0,

                -0.5f,0.5f,1,
                -0.5f,-0.5f,1,
                0.5f,-0.5f,1,
                0.5f,0.5f,1,

                0.5f,0.5f,0,
                0.5f,-0.5f,0,
                0.5f,-0.5f,1,
                0.5f,0.5f,1,

                -0.5f,0.5f,0,
                -0.5f,-0.5f,0,
                -0.5f,-0.5f,1,
                -0.5f,0.5f,1,

                -0.5f,0.5f,1,
                -0.5f,0.5f,0,
                0.5f,0.5f,0,
                0.5f,0.5f,1,

                -0.5f,-0.5f,1,
                -0.5f,-0.5f,0,
                0.5f,-0.5f,0,
                0.5f,-0.5f,1
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
                0, 0, -1,
                0, 0, 1,
                0, 1, 0,
                0, -1, 0,
                1, 0, 0,
                -1, 0, 0,
        };

        int[] indices = {
                0,1,3,
                3,1,2,
                4,5,7,
                7,5,6,
                8,9,11,
                11,9,10,
                12,13,15,
                15,13,14,
                16,17,19,
                19,17,18,
                20,21,23,
                23,21,22
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
