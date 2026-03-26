package comp3170.week5.sceneobjects;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import comp3170.SceneObject;
import comp3170.InputManager;

public class Camera extends SceneObject {

	private float zoom = 100.0f;
	private final float ZOOM_SPEED = 100.0f;
	
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();
	
	private Vector3f position = new Vector3f();
	private AxisAngle4f angle = new AxisAngle4f();
	
	private float width;
	private float height;
	
	public Camera() {
		
	}
	
	public void resize(int w, int h) {
		width = w;
		height = h;
	}
	
	public Matrix4f GetViewMatrix(Matrix4f dest) {
		viewMatrix = getMatrix();
		
		//Saves translation in "position"
		viewMatrix.getTranslation(position);
		
		//Saves rotation in "angle"
		viewMatrix.getRotation(angle);
		
		//Set view matrix to just translation and rotation
		viewMatrix.identity();
		viewMatrix.translate(position).rotate(angle);
		
		return viewMatrix.invert(dest);
	}
	
	public Matrix4f GetProjectionMatrix(Matrix4f dest) {
		projectionMatrix.identity();
		projectionMatrix.scale(width/zoom, height/zoom, 1.0f);
		
		return projectionMatrix.invert(dest);
	}
	
	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(GLFW_KEY_UP)) {
			zoom += ZOOM_SPEED * deltaTime;
		}
			
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			zoom -= ZOOM_SPEED * deltaTime;
			
			//Prevent reflection
			if (zoom <= 20) {
				zoom = 20;
			}
		}
	}
}