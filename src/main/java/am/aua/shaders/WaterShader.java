package am.aua.shaders;

import am.aua.entities.Camera;
import am.aua.entities.Light;
import org.joml.Matrix4f;

public class WaterShader extends ShaderProgram {

    private final static String VERTEX_FILE = "shaders/water.vs";
    private final static String FRAGMENT_FILE = "shaders/water.fs";

    private String location_modelMatrix;
    private String location_viewMatrix;
    private String location_projectionMatrix;
    private String location_reflectionTexture;
    private String location_refractionTexture;
    private String location_dudvMap;
    private String location_displacementFactor;
    private String location_aCameraPos;
    private String location_normalMap;
    private String location_lightColor;
    private String location_lightPosition;
    private String location_depthMap;
    private String location_nearPlane;
    private String location_farPlane;

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
        location_dudvMap = "dudvMap";
        location_displacementFactor = "displacementFactor";
        location_aCameraPos = "aCameraPos";
        location_normalMap = "normalMap";
        location_lightColor = "lightColor";
        location_lightPosition = "lightPos";
        location_depthMap = "depthMap";
        location_nearPlane = "nearPlane";
        location_farPlane = "farPlane";
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.setMatrix4f(location_projectionMatrix, projection);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = camera.getView();
        super.setMatrix4f(location_viewMatrix, viewMatrix);

        super.setVector3f(location_aCameraPos, camera.getPosition());
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.setMatrix4f(location_modelMatrix, matrix);
    }

    public void loadTextures() {
        super.setInt(location_reflectionTexture, 0);
        super.setInt(location_refractionTexture, 1);
        super.setInt(location_dudvMap, 2);
        super.setInt(location_normalMap, 3);
        super.setInt(location_depthMap, 4);
    }

    public void loadNearFarPlanes(float nearPlane, float farPlane) {
        super.setFloat(location_nearPlane, nearPlane);
        super.setFloat(location_farPlane, farPlane);
    }

    public void loadLight(Light light) {
        super.setVector3f(location_lightColor, light.getColor());
        super.setVector3f(location_lightPosition, light.getPosition());
    }

    public void loadDisplacementFactor(float factor) {
        super.setFloat(location_displacementFactor, factor);
    }

}
