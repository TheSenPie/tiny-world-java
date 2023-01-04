#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoords;

out vec4 ClipSpace;
out vec2 TexCoords;
out vec2 DUDVTexCoords;
out vec3 toCamera;
out vec3 fromLight;

uniform mat4 m;
uniform mat4 v;
uniform mat4 p;
uniform vec3 lightPos;

uniform vec3 aCameraPos;

const float tiling = 4.0;

void main() {
	vec4 worldPos = m * vec4(aPos, 1.0);
	ClipSpace = p * v * worldPos;
    gl_Position = ClipSpace;
    TexCoords = aTexCoords * tiling;
    DUDVTexCoords = vec2(aPos.x / 2.0 + 0.5, aPos.z / 2.0 + 0.5) * tiling;
    toCamera = aCameraPos - worldPos.xyz;
    fromLight = worldPos.xyz - lightPos;
}
