#version 330 core
out vec4 FragColor;

in vec3 FragPos;
in vec2 TexCoords;
in vec3 Normal;
in vec3 LightPos;   // extra in variable, since we need the light position in view space we calculate this in the vertex shader

uniform sampler2D grass;
uniform sampler2D grassRocky;
uniform sampler2D heightmap;

uniform vec3 lightColor;
uniform float shininess;
uniform float specularStrength;

const float tiling = 4.0;
const float tiling_grass = 3.0;

void main()
{
    // ambient
    float ambientStrength = 0.1;
    vec3 ambient = ambientStrength * lightColor;

     // diffuse
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(LightPos - FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;

    // specular
    float specularStrength = 0.5;
    vec3 viewDir = normalize(-FragPos); // the viewer is always at (0,0,0) in view-space, so viewDir is (0,0,0) - Position => -Position
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);
    vec3 specular = specularStrength * spec * lightColor;

    vec3 result = (ambient + diffuse + specular);
    vec4 grassColor = mix(texture(grassRocky, TexCoords * tiling), texture(grass, TexCoords * tiling), texture(heightmap, TexCoords).x);
    FragColor = vec4(result, 1.0) * grassColor;
}