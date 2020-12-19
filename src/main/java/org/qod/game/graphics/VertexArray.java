package org.qod.game.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArray {

    private int vertexArrayObject,
            vertexBufferObject,
            indexBufferObject,
            textureCoordinateBufferObject;
    private final int count;

    public VertexArray(float[] vertices, byte[] indices, float[] textureCoordinates) {
        count = indices.length;

        vertexArrayObject = glGenVertexArrays();
        glBindVertexArray(vertexArrayObject);

        // Vertex Buffer Objects
        vertexBufferObject = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);

        FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(4 * vertices.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.VERTEX_ATTRIB);

        // Texture Coordinate Buffer Object
        textureCoordinateBufferObject = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, textureCoordinateBufferObject);

        FloatBuffer textureBuffer = ByteBuffer.allocateDirect(4 * textureCoordinates.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
        textureBuffer.put(textureCoordinates).flip();
        glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);

        glVertexAttribPointer(Shader.TEX_COORDINATES, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.TEX_COORDINATES);

        // Index Buffer Object
        indexBufferObject = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);

        ByteBuffer indexBuffer = ByteBuffer.allocateDirect(indices.length).order(ByteOrder.nativeOrder());
        indexBuffer.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void bind() {
        glBindVertexArray(vertexArrayObject);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);
    }

    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void draw() {
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0);
    }

    public void render() {
        bind();
        draw();
    }
}
