#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoords;

out vec4 ClipSpace;
out vec2 TexCoords;
out vec2 DUDVTexCoords;

uniform mat4 m;
uniform mat4 v;
uniform mat4 p;

const float tiling = 4.0;

void main() {
	ClipSpace = p * v * m * vec4(aPos, 1.0);
    gl_Position = ClipSpace;
    TexCoords = aTexCoords * tiling;
    DUDVTexCoords = vec2(aPos.x / 2.0 + 0.5, aPos.z / 2.0 + 0.5) * tiling;
}
