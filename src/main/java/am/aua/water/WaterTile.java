package am.aua.water;

import am.aua.models.RawModel;
import am.aua.rendererEngine.Loader;
import am.aua.textures.Texture;

public class WaterTile {

    private static final float SIZE = 4;
    private static final int DIVISIONS = 128;

    private float x;
    private float z;
    private RawModel model;
    private Texture texture;

    public WaterTile (int gridX, int gridZ, Loader loader, Texture texture){
        this.texture = texture;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = generateWater(loader);
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    public Texture getTexture() {
        return texture;
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

}