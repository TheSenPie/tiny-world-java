#version 330 core
out vec4 FragColor;

in vec4 ClipSpace;
in vec2 DUDVTexCoords;
in vec2 TexCoords;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D waterTexture;

uniform float displacementFactor;

const float waveStrength = 0.02;

void main()
{
	vec2 ndc = (ClipSpace.xy / ClipSpace.w) / 2.0 + 0.5;
	vec2 refractTexCoords = vec2(ndc.x, ndc.y);
	vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);

	vec2 distortion1 = (texture(dudvMap, vec2(TexCoords.x + displacementFactor, DUDVTexCoords.y)).rg * 2.0 - 1.0) * waveStrength;
	vec2 distortion2 = (texture(dudvMap, vec2(-TexCoords.x + displacementFactor, DUDVTexCoords.y + displacementFactor)).rg * 2.0 - 1.0) * waveStrength;
	vec2 totalDistortion = distortion1 + distortion2;

	refractTexCoords += totalDistortion;
	refractTexCoords = clamp(refractTexCoords, 0.001, 0.999);

	reflectTexCoords += totalDistortion;
	reflectTexCoords.x = clamp(reflectTexCoords.x, 0.001, 0.999);
	reflectTexCoords.y = clamp(reflectTexCoords.y, -0.999, -0.001);

	vec4 reflectColor = texture(reflectionTexture, reflectTexCoords);
	vec4 refractionColor = texture(refractionTexture, refractTexCoords);
    FragColor = mix(reflectColor, refractionColor, 0.5);
    FragColor = mix(FragColor, texture(waterTexture, vec2(TexCoords.x + displacementFactor, TexCoords.y )), 0.2);
}