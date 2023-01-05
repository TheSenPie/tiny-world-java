package am.aua.water;

import am.aua.models.RawModel;
import am.aua.rendererEngine.Loader;
import am.aua.utils.Triangle;
import org.joml.Vector3f;

import java.util.ArrayList;

public class WaterTile {

    private static final float SIZE = 4;
    private static final int DIVISIONS = 128;

    private float x;
    private float y;
    private float z;
    private RawModel model;

    private ArrayList<Triangle> collider;

    public WaterTile (int gridX, int gridZ, Loader loader) {
        this.x = gridX * SIZE;
        this.y = 0;
        this.z = gridZ * SIZE;
        this.model = generateWater(loader);
        this.collider = new ArrayList<>();
        makeCollider();
    }

    private void makeCollider () {
        collider.clear();
        // trinagle 1
        Triangle triangle1 = new Triangle();
        Vector3f t1_v1 = new Vector3f(0, 0, 0);
        Vector3f t1_v2 = new Vector3f(SIZE, 0, SIZE);
        Vector3f t1_v3 = new Vector3f(0, 0, SIZE);
        triangle1.v1 = t1_v1;
        triangle1.v2 = t1_v2;
        triangle1.v3 = t1_v3;
        // triangle 2
        Triangle triangle2 = new Triangle();
        Vector3f t2_v1 = new Vector3f(0, 0, 0);
        Vector3f t2_v2 = new Vector3f(SIZE, 0, 0);
        Vector3f t2_v3 = new Vector3f(SIZE, 0, SIZE);
        triangle2.v1 = t2_v1;
        triangle2.v2 = t2_v2;
        triangle2.v3 = t2_v3;

        collider.add(triangle1);
        collider.add(triangle2);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    private RawModel generateWater(Loader loader){
        int vertices_size = (DIVISIONS + 1) * (DIVISIONS + 1);
        int indices_size = DIVISIONS * (DIVISIONS + 1) * 2 + 2 * (DIVISIONS - 1);
        float[] vertices = new float[vertices_size * 3];
        float[] textureCoords = new float[vertices_size * 2];
        int[] indices = new int[indices_size];
        int vertexPointer = 0;
        for (int i = 0; i <= DIVISIONS; i++) { // j = s
            for (int j = 0; j  <= DIVISIONS; j++) { // i = n
                float x, y, z;
                x = (float) j / ((float) DIVISIONS - 1) * SIZE;
                y = 0;
                z = (float) i / ((float) DIVISIONS - 1) * SIZE;

                float u, v;
                u = (float) j / ((float) DIVISIONS - 1);
                v = (float) i / ((float) DIVISIONS - 1);

                vertices[vertexPointer * 3] = x;
                vertices[vertexPointer * 3 + 1] = y;
                vertices[vertexPointer * 3 + 2] = z;
                textureCoords[vertexPointer * 2] = u;
                textureCoords[vertexPointer * 2 + 1] = v;

                vertexPointer++;
            }}
        // init the indices
        int pointer = 0;
        for (int i = 0; i < DIVISIONS; i++) {
            for (int j = 0; j <= DIVISIONS; j++) {
                // quad
                indices[pointer]         = i * (DIVISIONS + 1) + j;
                indices[pointer + 1]     = (i + 1) * (DIVISIONS + 1) + j;
                pointer += 2;
            }
            if (i + 1 < DIVISIONS) {
                // add indices for degenrate triangles
                indices[pointer] = (i + 1) * (DIVISIONS + 1) + (DIVISIONS - 1) + 1;
                indices[pointer + 1] = (i + 1) * (DIVISIONS + 1);
                pointer += 2;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, indices);
    }

    public ArrayList<Triangle> getCollider () {
        return collider;
    }

    public void setY(float y) {
        this.y = y;
    }

}