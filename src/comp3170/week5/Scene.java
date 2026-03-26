package comp3170.week5;

import java.util.ArrayList;

import org.joml.Vector4f;
import comp3170.InputManager;
import comp3170.SceneObject;

import comp3170.week5.sceneobjects.*;

public class Scene extends SceneObject {
	private Camera camera;
	private ArrayList<Flower> flowers = new ArrayList<>();
	
	public Scene() {
		camera = new Camera();
		camera.setParent(this);
		
		createFlower(new Vector4f(0.0f,0.0f,0.f,1.0f));		
	}
	
	public Camera sceneCam() {
		return camera;
	}
	
	public void createFlower(Vector4f position) {
		Flower flower = new Flower();
		flower.setParent(this);	
		flower.getMatrix().translate(position.x,position.y,0.0f);
		
		flowers.add(flower);
	}

	public void update(InputManager input, float dt) {
		
		camera.update(input, dt);
		
		for(Flower flower : flowers) {
			flower.update(dt);
		}
	}
	
}
