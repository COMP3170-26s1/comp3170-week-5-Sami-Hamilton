package comp3170.week5.sceneobjects;

import static org.lwjgl.opengl.GL41.*;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import static comp3170.Math.TAU;

public class Flower extends SceneObject {
	
	private static final String VERTEX_SHADER = "vertex.glsl";
	private static final String FRAGMENT_SHADER = "fragment.glsl";
	private Shader shader;
	
	private final float HEIGHT = 1.0f;
	private final float WIDTH = 0.1f;
	private Vector3f stemColour = new Vector3f(0f, 0.5f, 0f); // Dark Green
	
	private FlowerHead flowerHead;
	private int minPetals = 5;
	private int maxPetals = 15;

	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	
	private float minSwayAngle = -TAU/32;
	private float maxSwayAngle = TAU/32;
	private final float SWAY_SPEED = TAU/24;
	private int direction = 1;
	
	private Matrix4f modelMatrix = new Matrix4f();
	private AxisAngle4f rotation = new AxisAngle4f();
	private Vector3f translation = new Vector3f();
	private Vector3f scale = new Vector3f();

	public Flower() {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);		
	
		// make the stem of the flower

		// vertices for a wxh square with origin at the end
		// 
		//  (-w/2, h)     (w/2, h)		
		//       2-----------3
		//       | \         |
		//       |   \       |
		//       |     \     |
		//       |       \   |
		//       |         \ |
		//       0-----*-----1
		//  (-w/2, 0)     (w/2, 0)	
		
		//@formatter:off
		vertices = new Vector4f[] {
			new Vector4f(-WIDTH / 2,           0, 0, 1),
			new Vector4f( WIDTH / 2,           0, 0, 1),
			new Vector4f(-WIDTH / 2, HEIGHT, 0, 1),
			new Vector4f( WIDTH / 2, HEIGHT, 0, 1),
		};
		//@formatter:on
		vertexBuffer = GLBuffers.createBuffer(vertices);
		
	    indices = new int[] {
		    	0, 1, 2,
		    	3, 2, 1,
		};
		    
		indexBuffer = GLBuffers.createIndexBuffer(indices);
		
		createFlowerHead();
	}
	
	public void createFlowerHead() {
		//Math.random generates random double between 0.0 and 1.0
		float r = (float) Math.random();
		float g = (float) Math.random();
		float b = (float) Math.random();
		Vector3f petalColour = new Vector3f(r, g, b);

		//nPetals between 5 and 20
		int nPetals = minPetals + (int) (Math.random() * (maxPetals - minPetals) + 1);

		flowerHead = new FlowerHead(nPetals, petalColour);
		
		flowerHead.setParent(this);
		flowerHead.getMatrix().translate(0.0f, HEIGHT, 0.0f).scale(0.5f);
	}
	
	public void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		shader.setUniform("u_mvpMatrix", mvpMatrix);
	    shader.setAttribute("a_position", vertexBuffer);
	    shader.setUniform("u_colour", stemColour);	    
	    
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	    glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);		
	}
	
	public void update(float dt) {
		// TODO: make the flower sway. (TASK 5)
		modelMatrix = getMatrix();
		
		modelMatrix.rotateZ(direction * SWAY_SPEED * dt);
		flowerHead.getMatrix().rotateZ(-direction * SWAY_SPEED * dt);
		modelMatrix.getRotation(rotation);
		
		
		if (rotation.angle >= maxSwayAngle) {
			direction *= -1;
			rotation.angle = maxSwayAngle;
			modelMatrix.getTranslation(translation);
			modelMatrix.getScale(scale);
			modelMatrix.identity();
			modelMatrix.translate(translation).rotate(rotation).scale(scale);
		}
		if (rotation.angle <= minSwayAngle) {
			direction *= -1; 
			rotation.angle = minSwayAngle;
			modelMatrix.getTranslation(translation);
			modelMatrix.getScale(scale);
			modelMatrix.identity();
			modelMatrix.translate(translation).rotate(rotation).scale(scale);
		}
		
		flowerHead.update(dt);
	}
}
