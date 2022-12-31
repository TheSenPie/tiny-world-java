package am.aua.rendererEngine;

import am.aua.models.RawModel;
import am.aua.shaders.WaterShader;
import am.aua.textures.Texture;
import am.aua.utils.Maths;
import am.aua.water.WaterFrameBuffers;
import am.aua.water.WaterTile;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class WaterRenderer {
    private static final float WAVE_SPEED = 0.03f;

    private WaterFrameBuffers fbos;
    private WaterShader shader;

    private float displacementFactor = 0;

    public WaterRenderer(WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
        this.shader = shader;
        this.fbos = fbos;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render (List<WaterTile> waterTiles) {
        for (WaterTile waterTile : waterTiles) {
            prepareWater(waterTile);
            loadModelMatrix(waterTile);
            GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, waterTile.getModel().getVertexCount(),
                    GL11.GL_UNSIGNED_INT, 0);
            unbindTexturedModel();
        }
    }

    private void prepareWater(WaterTile waterTile) {
        RawModel rawModel = waterTile.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterTile.getDUDV().getID());
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterTile.getTexture().getID());
    }

    private void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    private void loadModelMatrix(WaterTile waterTile) {
        Matrix4f transformationMatrix = Maths.createModelMatrix(new Vector3f(waterTile.getX(), 0, waterTile.getZ()), 0, 0, 0, 1);
        shader.loadTransformationMatrix(transformationMatrix);
    }

    public void prepare() {
        displacementFactor += WAVE_SPEED * Window.getDelta();
        displacementFactor %= 1;
    }

    public float getDisplacementFactor() {
        return displacementFactor;
    }
}