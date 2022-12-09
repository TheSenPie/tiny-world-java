package am.aua.shaders;

import am.aua.entities.Camera;
import org.joml.Matrix4f;

public class WaterShader extends ShaderProgram {

    private final static String VERTEX_FILE = "src/assets/shaders/water.vs";
    private final static String FRAGMENT_FILE = "src/assets/shaders/water.fs";

    private String location_modelMatrix;
    private String location_viewMatrix;
    private String location_projectionMatrix;
    private String location_reflectionTexture;
    private String location_refractionTexture;

    public WaterShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        location_modelMatrix = "m";
        location_viewMatrix = "v";
        location_projectionMatrix = "p";
        location_reflectionTexture = "reflectionTexture";
        location_refractionTexture = "refractionTexture";
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.setMatrix4f(location_projectionMatrix, projection);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = camera.getView();
        super.setMatrix4f(location_viewMatrix, viewMatrix);
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.setMatrix4f(location_modelMatrix, matrix);
    }

    public void loadTextures() {
        super.setInt(location_reflectionTexture, 0);
        super.setInt(location_refractionTexture, 1);
    }

}
