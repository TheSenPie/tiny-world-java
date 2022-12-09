#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoords;
layout (location = 2) in vec3 aNormal;

out vec3 FragPos;
out vec2 TexCoords;
out vec3 Normal;
out vec3 LightPos;

uniform sampler2D texture2;

uniform vec3 lightPos; // we now define the uniform in the vertex shader and pass the 'view space' lightpos to the fragment shader. lightPos is currently in world space.

uniform mat4 m;
uniform mat4 v;
uniform mat4 p;

void main() {
	float height = (texture(texture2, aTexCoords).x - 0.5) * 0.69;
	vec3 shiftedPos = aPos + vec3(0, height , 0);

    TexCoords = aTexCoords;
    gl_Position = p * v * m * vec4(shiftedPos, 1.0);
    FragPos = vec3(v * m * vec4(shiftedPos, 1.0));
    Normal = mat3(transpose(inverse(v * m))) * aNormal;
    LightPos = vec3(v * vec4(lightPos, 1.0)); // Transform world-space light position to view-space light position
}
