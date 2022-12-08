package am.aua.entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static am.aua.rendererEngine.Window.Keyboard;

public class Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch;
    private float yaw;
    private float roll;

    public Camera() {}

    public void move() {
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
            position.z -= 0.02;
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_D)) {
            position.x += 0.02;
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_A)) {
            position.x -= 0.02;
        }
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

    public float getRoll() {
        return roll;
    }


    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }
}
