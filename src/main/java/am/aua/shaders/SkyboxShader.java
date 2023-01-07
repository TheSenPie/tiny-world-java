package am.aua.shaders;

import am.aua.entities.Camera;
import am.aua.rendererEngine.Window;
import org.joml.Matrix4f;

public class SkyboxShader extends ShaderProgram{

    private static final String VERTEX_FILE = "shaders/skybox.vs";
    private static final String FRAGMENT_FILE = "shaders/skybox.fs";

    private static final float ROTATE_SPEED = 1f;

    private String location_viewMatrix;
    private String location_projectionMatrix;

    private float rotation = 0;

    private Matrix4f viewMatrixWithoutTranslation = new Matrix4f();

    public SkyboxShader () {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "aPos");
    }

    @Override
    protected void getAllUniformLocations() {
        location_viewMatrix = "v";
        location_projectionMatrix = "p";
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = camera.getView();
        viewMatrixWithoutTranslation.set(viewMatrix);
        viewMatrixWithoutTranslation.m30(0);
        viewMatrixWithoutTranslation.m31(0);
        viewMatrixWithoutTranslation.m32(0);
        rotation += ROTATE_SPEED * Window.getDelta();
        viewMatrixWithoutTranslation.rotate((float) Math.toRadians(rotation), 0, 1, 0);
        super.setMatrix4f(location_viewMatrix, viewMatrixWithoutTranslation);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.setMatrix4f(location_projectionMatrix, projection);
    }
}
