package am.aua.utils;

import am.aua.entities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Maths {

    public static float clamp(float x, float mn, float mx) {
        return Math.max(mn, Math.min(mx, x));
    }

    public static Matrix4f createModelMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(translation);
        matrix.rotate((float) Math.toRadians(rx), 1,0,0);
        matrix.rotate((float) Math.toRadians(ry), 0,1,0);
        matrix.rotate((float) Math.toRadians(rz), 0,0,1);
        matrix.scale(new Vector3f(scale,scale,scale));
        return matrix;
    }

    public static boolean ray_triangle_intersect(
            Vector3f orig, Vector3f dir,
            Vector3f v0, Vector3f v1, Vector3f v2,
            Vector3f tuv) {
        Vector3f v0v1, v0v2, pvec, tvec, qvec;
        float det, invDet;
        v0v1 = new Vector3f(v1).sub(v0);
        v0v2 = new Vector3f(v2).sub(v0);
        pvec = new Vector3f(dir).cross(v0v2);
        det = new Vector3f(v0v1).dot(pvec);

        // if the determinant is negative the triangle is backfacing
        // if the determinant is close to 0, the ray misses the triangle
        if (det < 0.000000001f) return false;

        invDet = 1.0f / det;

        tvec = new Vector3f(orig).sub(v0);
        tuv.y = new Vector3f(tvec).dot(pvec) * invDet; // u
        if (tuv.y < 0 || tuv.y > 1)return false;

        qvec = new Vector3f(tvec).cross(v0v1);
        tuv.z = new Vector3f(dir).dot(qvec) * invDet;
        if (tuv.z < 0 || tuv.y + tuv.z > 1)return false;

        tuv.x = new Vector3f(v0v2).dot(qvec) * invDet;

        return true;
    }
}
