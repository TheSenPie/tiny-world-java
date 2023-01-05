package am.aua.entities;

import am.aua.rendererEngine.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static am.aua.rendererEngine.Window.Keyboard;
import static am.aua.rendererEngine.Window.Mouse;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Camera {

    private Vector3f position;
    private Vector3f front;
    private Vector3f up;
    private float pitch;
    private float yaw;
    private float fov;
    private float speed = 10f;
    private float sensitivity = 0.1f;

    private Matrix4f view;

    public Camera() {
        position = new Vector3f();
        front = new Vector3f(0.0f, 0.0f, -1.0f);
        up = new Vector3f(0.0f, 1.0f, 0.0f);
        yaw = -90.0f;
        pitch = 0.0f;
        fov = 45.0f;
        view = new Matrix4f().identity();
    }

    public void move() {
        float delta = Window.getDelta();
        if (Mouse.isKeyDown(GLFW_MOUSE_BUTTON_LEFT)) {
            Vector2f mouseDelta = new Vector2f(Mouse.getDelta()).mul(sensitivity);
            yaw += mouseDelta.x;
            pitch -= mouseDelta.y;

            // make sure that when pitch is out of bounds, screen doesn't get flipped
            if (pitch > 89.0f)
                pitch = 89.0f;
            if (pitch < -89.0f)
                pitch = -89.0f;
        }

        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
            position.add(new Vector3f(front).mul(speed * delta));
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
            position.sub(new Vector3f(front).mul(speed * delta));
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_A)) {
            position.sub(new Vector3f(front).cross(up).normalize().mul(speed * delta));
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_D)) {
            position.add(new Vector3f(front).cross(up).normalize().mul(speed * delta));
        }
    }

    public void update () {
        // recalculate view matrix
        front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        view.identity();
        view.lookAt(position, new Vector3f(position).add(front), up);
    }

    public Matrix4f getView () {
        return view;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void invertPitch() {
        this.pitch *= -1;
    }

    public float getFov() {
        return fov;
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }

    public void setYaw(int angle) {
        yaw = angle;
    }
}
