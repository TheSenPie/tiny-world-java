package am.aua.shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public abstract class ShaderProgram {
    // program ID
    private int programID;

    private int vertexShaderID;

    private int fragmentShaderID;

    private FloatBuffer fb = BufferUtils.createFloatBuffer(16);

    // constructor reads and builds the shader
    public ShaderProgram(String vertexPath, String fragmentPath) {
        // 1. retrieve the vertex/fragment source code from filePath
        vertexShaderID = loadShader(vertexPath, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentPath, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);

        // print linking errors if any
        int[] success = new int[1];
        GL20.glGetProgramiv(programID, GL20.GL_LINK_STATUS, success);
        if(success[0] == 0) {
            String infoLog = GL20.glGetProgramInfoLog(programID, 512);
            System.out.println("ERROR::SHADER::PROGRAM::LINKING_FAILED\n" + infoLog + "\n");
        }
        getAllUniformLocations();
    }

    protected abstract void bindAttributes();

    protected abstract void getAllUniformLocations();

    // use/activate the shader
    public void use() {
        GL20.glUseProgram(programID);
    }

    // utility uniform functions
    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    protected void setBool(String name, boolean value) {
        int val = value ? 1 : 0;
        GL20.glUniform1i(GL20.glGetUniformLocation(programID, name), val);
    }
    protected void setInt(String name, int value) {
        GL20.glUniform1i(GL20.glGetUniformLocation(programID, name), value);
    }
    protected void setFloat(String name, float value) {
        GL20.glUniform1f(GL20.glGetUniformLocation(programID, name), value);
    }

    protected void setMatrix4f(String name, Matrix4f value) {
        GL20.glUniformMatrix4fv(GL20.glGetUniformLocation(programID, name), false, value.get(fb));
    }

    protected void setVector3f(String name, Vector3f value) {
        GL20.glUniform3f(GL20.glGetUniformLocation(programID, name), value.x, value.y, value.z);
    }

    protected void setVector4f(String name, Vector4f value) {
        GL20.glUniform4f(GL20.glGetUniformLocation(programID, name), value.x, value.y, value.z, value.w);
    }

    protected void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    public void start(){
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void dispose() {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine())!=null){
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        // check for shader compile errors
        int success[] = new int[1];
        GL20.glGetShaderiv(shaderID, GL20.GL_COMPILE_STATUS, success);
        if (success[0] == 0) {
            String infoLog = GL20.glGetShaderInfoLog(shaderID, 512);
            if (type ==  GL20.GL_VERTEX_SHADER) {
                System.out.println("ERROR::SHADER::VERTEX::COMPILATION_FAILED\\n" + infoLog + "\n");
            } else if (type == GL20.GL_FRAGMENT_SHADER) {
                System.out.println("ERROR::SHADER::FRAG::COMPILATION_FAILED\\n" + infoLog + "\n");
            }
        }
        return shaderID;
    }
}
