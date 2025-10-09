#version 330 core

out vec4 FragColor;

struct Material{
    vec3 specular;
    vec3 diffuse;
    vec3 ambient;
    float shininess;
};

struct Light{
    vec3 position;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

uniform Light light;
uniform Material material;

uniform vec3 objectColor;

uniform vec3 lightColor;
uniform vec3 lightPos;

uniform vec3 viewPos;

in vec3 FragPos;
in vec3 Normal;

void main()
{
    vec3 ambient = material.ambient * light.ambient;

    // Diffuse
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(lightPos - FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * material.diffuse * light.diffuse;

    // Specular
    vec3 viewDir = normalize(viewPos - FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = material.specular * spec * light.specular;

    vec3 result = (ambient + diffuse + specular) * objectColor;
    FragColor = vec4(result, 1.0);
}