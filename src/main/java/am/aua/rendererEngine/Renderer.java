package am.aua.rendererEngine;

import am.aua.entities.Camera;
import am.aua.entities.Light;
import am.aua.models.Entity;
import am.aua.models.TexturedModel;
import am.aua.shaders.StaticShader;
import am.aua.shaders.TerrainShader;
import am.aua.shaders.WaterShader;
import am.aua.terrains.Terrain;
import am.aua.water.WaterFrameBuffers;
import am.aua.water.WaterTile;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Renderer {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;

    private StaticShader shader = new StaticShader();
    private EntityRenderer renderer;

    private WaterRenderer waterRenderer;
    private WaterShader waterShader = new WaterShader();

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();


    private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
    private List<Terrain> terrains = new ArrayList<Terrain>();

    private boolean wireframe = false;
    private List<WaterTile> water = new ArrayList<>();

    public Renderer(WaterFrameBuffers fbos) {
//        GL11.glEnable(GL11.GL_CULL_FACE);
//        GL11.glCullFace(GL11.GL_BACK);
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        waterRenderer = new WaterRenderer(waterShader, projectionMatrix, fbos);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    public void render(Light light, Camera camera, Vector4f clipPlane) {
        prepare();
        shader.start();
        shader.loadCliplPlane(clipPlane);
        shader.loadLight(light);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();
        terrainShader.start();
        terrainShader.loadCliplPlane(clipPlane);
        terrainShader.loadLight(light);
        terrainShader.loadViewMatrix(camera);
        terrainShader.loadTextures();
        terrainRenderer.render(terrains);
        terrainShader.stop();
        terrains.clear();
        entities.clear();
    }

    public void renderWater(Camera camera) {
        waterRenderer.prepare();
        waterShader.start();
        waterShader.loadViewMatrix(camera);
        waterShader.loadTextures();
        waterShader.loadDisplacementFactor(waterRenderer.getDisplacementFactor());
        waterRenderer.render(water);
        waterShader.stop();
        water.clear();
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void processWater(WaterTile waterTile) {
        water.add(waterTile);
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void prepare() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);

        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, wireframe ? GL11.GL_LINE : GL11.GL_FILL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
    }

    private void createProjectionMatrix() {
        float aspectRatio = Window.getWidth() / Window.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix.m33(0);
    }

    public void dispose() {
        shader.dispose();
        terrainShader.dispose();
        waterShader.dispose();
    }

    public boolean isWireframe() {
        return wireframe;
    }

    public void setWireframe(boolean wireframe) {
        this.wireframe = wireframe;
    }
}
