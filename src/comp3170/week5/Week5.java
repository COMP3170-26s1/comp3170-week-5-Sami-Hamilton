package comp3170.week5;

import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL41.*;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import comp3170.OpenGLException;
import comp3170.IWindowListener;
import comp3170.ShaderLibrary;
import comp3170.Window;
import comp3170.week5.sceneobjects.Camera;
import comp3170.InputManager;

import java.io.File;
import java.io.IOException;


public class Week5 implements IWindowListener {
	private Window window;
	private int width = 800;
	private int height = 800;

	final private File DIRECTORY = new File("src/comp3170/week5/shaders/"); 

	private InputManager input;
	private long oldTime;
	
	private Scene scene;
	private Camera camera;

	public Week5()  throws OpenGLException {		
		
		window = new Window("Flower field", width, height, this);
		window.setResizable(true);
	    window.run();
	}

	public void init() {
		input = new InputManager(window); // create an input manager to listen for keypresses and mouse events		
		oldTime = System.currentTimeMillis(); // initialise oldTime
		
		new ShaderLibrary(DIRECTORY);
		scene = new Scene();
		
		camera = scene.sceneCam();
	}
	
	private Vector2i position = new Vector2i();
	private Vector4f worldPosition = new Vector4f();
		
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;		
		if (input.wasMouseClicked()) {
			//Get the mouse position in screen space
			input.getCursorPos(position); 
			
			//Convert to NDC
			worldPosition.x = (((float) position.x/width) * 2) - 1;
			worldPosition.y = (((float) position.y/height) * -2) + 1;
			worldPosition.mul(mvpMatrix.invert());
			
			//Create flower
			scene.createFlower(worldPosition);
		}
		
		input.clear(); // Run this to clear input before the next frame.
		scene.update(input, deltaTime); // Use update() for scene logic and draw() to...well, draw.
	}

	private Matrix4f viewMatrix  = new Matrix4f();
	private Matrix4f projectionMatrix  = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();
	
	public void draw() {
		//Get view and projection matricies
		camera.GetViewMatrix(viewMatrix);
		camera.GetProjectionMatrix(projectionMatrix);
		
		update();
		
		glClearColor(87.0f/255.0f, 60.0f/255.0f, 23.0f/255.0f, 1.0f); // Dirt brown
		glClear(GL_COLOR_BUFFER_BIT);
		
		//Calculate mvpMatrix
		mvpMatrix.identity();
		mvpMatrix.mul(viewMatrix).mul(projectionMatrix);
		
		scene.draw(mvpMatrix);
			
	}

	@Override
	public void resize(int width, int height) {
		// record the new width and height
		this.width = width;
		this.height = height;
		glViewport(0,0,width,height);
		
		//update values for projection matrix
		camera.resize(width, height);
	}

	@Override
	public void close() {
		// nothing to do
	}

	public static void main(String[] args) throws IOException, OpenGLException {
		new Week5();
	}

}
