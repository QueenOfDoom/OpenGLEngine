#version 330

layout (location = 0) out vec4 color;

uniform sampler2D active_texture;

in DATA
{
    vec2 texture_coordinates;
} fragment_in;

void main()
{
    color = texture(active_texture, fragment_in.texture_coordinates);
}