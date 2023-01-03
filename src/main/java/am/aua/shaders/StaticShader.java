package am.aua.shaders;

import am.aua.entities.Camera;
import am.aua.entities.Light;
import am.aua.utils.Maths;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class StaticShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/assets/shaders/shader.vs";
    private static final String FRAGMENT_FILE = "src/assets/shaders/shader.fs";

    private String location_modelMatrix;
    private String location_viewMatrix;
    private String location_projectionMatrix;
    private String location_lightPosition;
    private String location_lightColor;
    private String location_shininess;
    private String location_specularStrength;
    private String location_plane;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "aPos");
        super.bindAttribute(1, "aTextureCoords");
        super.bindAttribute(2, "aNorm");
    }

    @Override
    protected void getAllUniformLocations() {
        location_modelMatrix = "m";
        location_viewMatrix = "v";
        location_projectionMatrix = "p";
        location_lightPosition = "lightPos";
        location_lightColor = "lightColor";
        location_shininess = "shininess";
        location_specularStrength = "specularStrength";
        location_plane = "plane";
    }

    public void loadClipPlane(Vector4f plane) {
        super.setVector4f(location_plane, plane);
    }

    public void loadShineVariables(float shininess, float specularStrength) {
        super.setFloat(location_shininess, shininess);
        super.setFloat(location_specularStrength, specularStrength);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.setMatrix4f(location_modelMatrix, matrix);
    }

    public void loadLight(Light light) {
        super.setVector3f(location_lightPosition, light.getPosition());
        super.setVector3f(location_lightColor, light.getColor());
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = camera.getView();
        super.setMatrix4f(location_viewMatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.setMatrix4f(location_projectionMatrix, projection);
    }
}
