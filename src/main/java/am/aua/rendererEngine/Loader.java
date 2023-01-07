package am.aua.rendererEngine;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import am.aua.models.RawModel;
import am.aua.textures.TextureData;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Loader {

    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();

    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,2,textureCoords);
        storeDataInAttributeList(2,3,normals);
        unbindVAO();
        return new RawModel(vaoID,indices.length);
    }

    public RawModel loadToVAO(float[] positions, float[] textureCoords, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,2,textureCoords);
        unbindVAO();
        return new RawModel(vaoID,indices.length);
    }

    public RawModel loadToVAO(float[] positions, int dimension) {
        int vaoID = createVAO();
        storeDataInAttributeList(0,3,positions);
        unbindVAO();
        return new RawModel(vaoID,positions.length / dimension);
    }

    public int loadTexture(String fileName) {
        return loadTexture(fileName, GL_REPEAT);
    }

    public int loadTexture(String fileName, int param) {
        stbi_set_flip_vertically_on_load(true);

        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture); // all upcoming GL_TEXTURE_2D operations now have effect on this texture object
        // set the texture wrapping parameters
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, param);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, param);
        // set texture filtering parameters
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        // load image, create texture and generate mipmaps
        try (MemoryStack stack = stackPush()) {
            IntBuffer w    = stack.mallocInt(1);
            IntBuffer h    = stack.mallocInt(1);
            IntBuffer nrChannels = stack.mallocInt(1);

            ByteBuffer data = stbi_load("src/assets/images/" + fileName, w, h, nrChannels, 0);
            if (data != null) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w.get(0), h.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, data);
                glGenerateMipmap(GL_TEXTURE_2D);
            } else {
                System.out.println("Failed to load texture " + fileName);
            }
            stbi_image_free(data);
        }

        return texture;
    }

    public int loadCubeMap (String[] textureFiles) {
        int texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texture);

        for (int i = 0; i < textureFiles.length; i++) {
            TextureData textureData = decodeTextureFile(textureFiles[i]);
            glTexImage2D(
                    GL_TEXTURE_CUBE_MAP_POSITIVE_X + i,
                    0, GL_RGB, textureData.getWidth(), textureData.getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, textureData.getBuffer()
            );
            textureData.dispose();
        }
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
        textures.add(texture);
        return texture;
    }

    private TextureData decodeTextureFile (String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;

        stbi_set_flip_vertically_on_load(false);
        try (MemoryStack stack = stackPush()) {
            IntBuffer w    = stack.mallocInt(1);
            IntBuffer h    = stack.mallocInt(1);
            IntBuffer nrChannels = stack.mallocInt(1);

            ByteBuffer data = stbi_load("src/assets/images/" + fileName, w, h, nrChannels, 0);
            if (data == null) {
                System.out.println("Failed to load texture " + fileName);
                System.exit(-1);
            }
            buffer = data;
            width = w.get();
            height = h.get();
        }
        return new TextureData(width, height, buffer);
    }

    public void dispose() {
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        for (int texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber, int coordinateSize,float[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber,coordinateSize,GL11.GL_FLOAT,false,0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

}
