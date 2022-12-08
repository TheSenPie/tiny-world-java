package am.aua;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public class Shader {
    // program ID
    private int ID;

    private FloatBuffer fb = BufferUtils.createFloatBuffer(16);

    // constructor reads and builds the shader
    public Shader(String vertexPath, String fragmentPath) {
        // 1. retrieve the vertex/fragment source code from filePath
        StringBuilder vertexCode = new StringBuilder();
        StringBuilder fragmentCode = new StringBuilder();
        try {
            BufferedReader vertexReader = new BufferedReader(new FileReader(vertexPath));
            BufferedReader fragmentReader = new BufferedReader(new FileReader(fragmentPath));
            String line;
            while((line = vertexReader.readLine()) != null){
                vertexCode.append(line).append("//\n");
            }
            while((line = fragmentReader.readLine()) != null){
                fragmentCode.append(line).append("//\n");
            }
            vertexReader.close();
            fragmentReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // 2. compile shaders
        int vertex, fragment;

        // vertex Shader
        vertex = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertex, vertexCode);
        GL20.glCompileShader(vertex);
        // check for shader compile errors
        int success[] = new int[1];
        GL20.glGetShaderiv(vertex, GL20.GL_COMPILE_STATUS, success);
        if (success[0] == 0) {
            String infoLog = GL20.glGetShaderInfoLog(vertex, 512);
            System.out.println("ERROR::SHADER::VERTEX::COMPILATION_FAILED\\n" + infoLog + "\n");
        }

        fragment = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragment, fragmentCode);
        GL20.glCompileShader(fragment);
        // check for shader compile errors
        GL20.glGetShaderiv(fragment, GL20.GL_COMPILE_STATUS, success);
        if(success[0] == 0) {
            String infoLog = GL20.glGetShaderInfoLog(fragment, 512);
            System.out.println("ERROR::SHADER::FRAGMENT::COMPILATION_FAILED\n" + infoLog + "\n");
        }

        ID = GL20.glCreateProgram();
        GL20.glAttachShader(ID, vertex);
        GL20.glAttachShader(ID, fragment);
        GL20.glLinkProgram(ID);
        // print linking errors if any
        GL20.glGetProgramiv(ID, GL20.GL_LINK_STATUS, success);
        if(success[0] == 0) {
            String infoLog = GL20.glGetProgramInfoLog(ID, 512);
            System.out.println("ERROR::SHADER::PROGRAM::LINKING_FAILED\n" + infoLog + "\n");
        }
        // delete the shaders as they're linked into our program now and no longer necessary
        GL20.glDeleteShader(vertex);
        GL20.glDeleteShader(fragment);
    }

    // use/activate the shader
    public void use() {
        GL20.glUseProgram(ID);
    }

    // utility uniform functions
    public void setBool(String name, boolean value) {
        int val = value ? 1 : 0;
        GL20.glUniform1i(GL20.glGetUniformLocation(ID, name), val);
    }
    public void setInt(String name, int value) {
        GL20.glUniform1i(GL20.glGetUniformLocation(ID, name), value);
    }
    public void setFloat(String name, float value) {
        GL20.glUniform1f(GL20.glGetUniformLocation(ID, name), value);
    }

    public void setMatrix(String name, Matrix4f value) {
        GL20.glUniformMatrix4fv(GL20.glGetUniformLocation(ID, name), false, value.get(fb));
    }

    public void dispose() {
        GL20.glDeleteProgram(ID);
    }
}
