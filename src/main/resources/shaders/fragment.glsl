#version 330 core

out vec4 FragColor;
in vec3 customColor;
in vec2 texCoord;

uniform sampler2D customTexture;

void main()
{
    FragColor = texture(customTexture, texCoord);
}