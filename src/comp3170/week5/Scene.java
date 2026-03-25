package comp3170.week5;

import org.joml.Vector3f;
import org.joml.Vector4f;
import comp3170.InputManager;
import comp3170.SceneObject;

import comp3170.week5.sceneobjects.*;

public class Scene extends SceneObject {
	private Camera camera;
	
	public Scene() {
		camera = new Camera();
		createFlower(new Vector4f(0.0f,0.0f,0.f,1.0f));		
	}
	
	public Camera sceneCam() {
		return camera;
	}
	
	public void createFlower(Vector4f position) {
		Vector3f flowerColour = new Vector3f(1.0f, 1.0f, 0.0f);
		Flower flower = new Flower(20, flowerColour);
		flower.setParent(this);	
		flower.getMatrix().translate(position.x,position.y,0.0f);
	}

	public void update(InputManager input, float dt) {
		
		camera.update(input, dt);
		
		// TODO: Update the flowers when animating them. (TASK 5)
	}
	
}
