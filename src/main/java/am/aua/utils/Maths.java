package am.aua.utils;

import am.aua.entities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Maths {

    public static float clamp(float x, float mn, float mx) {
        return Math.max(mn, Math.min(mx, x));
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0));
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        viewMatrix.translate(negativeCameraPos);
        return viewMatrix;
    }
}
