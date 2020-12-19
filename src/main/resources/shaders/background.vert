#version 330

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 texture_coordinates;

uniform mat4 projection_matrix;

out DATA
{
    vec2 texture_coordinates;
} vertex_out;

void main()
{
    gl_Position = projection_matrix * position;
    vertex_out.texture_coordinates = texture_coordinates;
}