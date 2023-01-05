package am.aua.textures;

import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.stbi_image_free;

public class TextureData {
    private int width;
    private int height;
    private ByteBuffer buffer;

    public TextureData (int width, int height, ByteBuffer buffer) {
        this.width = width;
        this.height = height;
        this.buffer = buffer;
    }

    public int getWidth () {
        return width;
    }

    public int getHeight () {
        return height;
    }

    public ByteBuffer getBuffer () {
        return buffer;
    }

    public void dispose () {
        stbi_image_free(buffer);
    }
}
