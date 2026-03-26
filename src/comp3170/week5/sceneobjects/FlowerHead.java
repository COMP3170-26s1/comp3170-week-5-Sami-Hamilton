package comp3170.week5.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import static comp3170.Math.TAU;

public class FlowerHead extends SceneObject {
	
	private static final String VERTEX_SHADER = "vertex.glsl";
	private static final String FRAGMENT_SHADER = "fragment.glsl";
	private Shader shader;

	private Vector3f petalColour;
	
	private float innerRadius = 0.7f;
	private float outerRadius = 1.0f;

	private Vector4f[] vertices;
	private int vertexBuffer;
	
	private int[] indices;
	private int indexBuffer;
	
	private Matrix4f modelMatrix = new Matrix4f();
	private final float ROTATION_SPEED = TAU/4;

	public FlowerHead(int nPetals, Vector3f colour) {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);		
		
		// TODO: Create the flower head. (TASK 1)
		// Consider the best way to draw the mesh with the nPetals input. 
		// Note that this may involve moving some code OUT of this class!
		
		vertices = new Vector4f[(2 * nPetals) + 1];
		
		//Center
		vertices[0] = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
		
		for (int i = 1; i < vertices.length; i+=2) {
			vertices[i] = new Vector4f(0.0f, outerRadius, 0.0f, 1.0f);
			vertices[i+1] = new Vector4f(0.0f, innerRadius, 0.0f, 1.0f);
			
			//TAU/nPetal = rotation per petal
			vertices[i].rotateZ(-(TAU/nPetals) * 0.5f * ((i-1)));
			vertices[i+1].rotateZ(-(TAU/nPetals) * 0.5f * (i));
		}
		
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		
		indices = new int[nPetals * 2 * 3];
		
		int n = 0;
		for (int i = 1; i <= nPetals; i++) {
			indices[n++] = 0;
			indices[n++] = i*2;
			indices[n++] = (((nPetals + i - 2)%nPetals) + 1)*2;
			
			indices[n++] = (i*2) - 1;
			indices[n++] = (((nPetals + i - 2)%nPetals) + 1)*2;
			indices[n++] = i*2;
			
		}
		    
		indexBuffer = GLBuffers.createIndexBuffer(indices);
		
		
		petalColour = colour;
	}

	public void update(float dt) {
		// TODO: Make the flower head rotate. (TASK 5)
		modelMatrix = getMatrix();
		
		modelMatrix.rotateZ(ROTATION_SPEED * dt);
	}

	public void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		shader.setUniform("u_mvpMatrix", mvpMatrix);
	    shader.setAttribute("a_position", vertexBuffer);
	    shader.setUniform("u_colour", petalColour);
		
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	    glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}
}
