package a2;

import a2.*;
import myGameEngine.*;
import java.awt.*;
import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;

import net.java.games.input.Controller;
import ray.input.GenericInputManager;
import ray.input.InputManager;
import ray.input.action.Action;
import ray.rage.*;
import ray.rage.asset.texture.Texture;
import ray.rage.asset.texture.TextureManager;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.*;
import ray.rage.scene.controllers.OrbitController;
import ray.rage.util.BufferUtil;
import ray.rml.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.rendersystem.shader.GpuShaderProgram;
import ray.rage.rendersystem.states.*;

public class MyGame extends VariableFrameRateGame {

	// to minimize variable allocation in update()
	GL4RenderSystem rs;
	float elapsTime = 0.0f;
	String elapsTimeStr, counterStr, dispStr1, dispStr2;
	int elapsTimeSec, counter = 0;
	private GenericInputManager im;
	private ArrayList<Controller> controllers;
	private boolean p1Wins = false, p2Wins = false;
	
	//Camera Orbit Controller
	private Camera3Pcontroller orbitController, orbitController2;
	private Action moveFwdAct, moveBwdAct, moveLAct, moveRAct, gPYAct, gPXAct, moveZAct;
	private ArrayList<SceneNode> planets = new ArrayList<SceneNode>();
	private ArrayList<SceneNode> avatars = new ArrayList<SceneNode>();
	
    public MyGame() {
        super();
    }

    public static void main(String[] args) {
        Game game = new MyGame();
        try {
            game.startup();
            game.run();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            game.shutdown();
            game.exit();
        }
    }
	
	@Override
	protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) {
		rs.createRenderWindow(new DisplayMode(1000, 700, 24, 60), false);
	}
	
	protected void setupWindowViewports(RenderWindow rw) {
		//calculating the window SIZES for TWO VIEW PORTS w/ SPECIFIED BUFFER VALUE.
		float buffer = 0.005f; //bottom, left, width, height
		float topBot = (buffer/2.0f) + .5f;
		float height = (.5f - (buffer +(buffer/2.0f)));
		float width = 1-(buffer * 2.0f);
		float left = buffer;
		rw.addKeyListener(this);
		Viewport topViewport = rw.getViewport(0);
		
		topViewport.setDimensions(topBot, left, width, height);
		topViewport.setClearColor(new Color(1.0f,0.7f,0.7f));
		
		Viewport botViewport = rw.createViewport(buffer, left, width, height);
		botViewport.setClearColor(new Color(0.5f, 1.0f, 0.5f));
	}

    @Override
    protected void setupCameras(SceneManager sm, RenderWindow rw) {
        SceneNode rootNode = sm.getRootSceneNode();
        Camera camera = sm.createCamera("MainCamera", Projection.PERSPECTIVE);
        camera.setMode('n');
        
        rw.getViewport(0).setCamera(camera);
		
		camera.setRt((Vector3f)Vector3f.createFrom(1.0f, 0.0f, 0.0f));
		camera.setUp((Vector3f)Vector3f.createFrom(0.0f, 1.0f, 0.0f));
		camera.setFd((Vector3f)Vector3f.createFrom(0.0f, 0.0f, -1.0f));
		
		camera.setPo((Vector3f)Vector3f.createFrom(0.0f, 0.0f, 0.0f));
		
		Camera camera2 = sm.createCamera("MainCamera2", Projection.PERSPECTIVE);
        camera2.setMode('n');
		rw.getViewport(1).setCamera(camera2);
		
		camera2.setRt((Vector3f)Vector3f.createFrom(1.0f, 0.0f, 0.0f));
		camera2.setUp((Vector3f)Vector3f.createFrom(0.0f, 1.0f, 0.0f));
		camera2.setFd((Vector3f)Vector3f.createFrom(0.0f, 0.0f, -1.0f));
		
		camera2.setPo((Vector3f)Vector3f.createFrom(0.0f, 0.0f, 0.0f));

        SceneNode cameraNode2 = rootNode.createChildSceneNode(camera2.getName() + "Node");
        cameraNode2.attachObject(camera2);
		
		SceneNode cameraNode = rootNode.createChildSceneNode(camera.getName() + "Node");
        cameraNode.attachObject(camera);
    }
	
    @Override
    protected void setupScene(Engine eng, SceneManager sm) throws IOException {
    	im = new GenericInputManager();
    	RenderSystem rsTexture = sm.getRenderSystem();
    	TextureManager tm = eng.getTextureManager();
    	//Dolphin
        Entity dolphinE = sm.createEntity("dolphin", "dolphinHighPoly.obj");
        dolphinE.setPrimitive(Primitive.TRIANGLES);
        SceneNode dolphinN = sm.getRootSceneNode().createChildSceneNode(dolphinE.getName() + "Node");
        dolphinN.attachObject(dolphinE);
        dolphinN.moveRight(.35f);
        //DolphinRed
        Entity dolphinRedE = sm.createEntity("dolphinRed", "dolphinHighPoly.obj");
        dolphinRedE.setPrimitive(Primitive.TRIANGLES);
        	//dolphinRedState start
	        Texture dolphinRedText = tm.getAssetByPath("Dolphin_HighPolyUVBlue.png");
	        TextureState dolphinRedState = (TextureState)
	        		rsTexture.createRenderState(RenderState.Type.TEXTURE);
	        dolphinRedState.setTexture(dolphinRedText);
	        dolphinRedE.setRenderState(dolphinRedState);
        	//dolphinRedState end
        SceneNode dolphinRedN = sm.getRootSceneNode().createChildSceneNode(dolphinRedE.getName() + "Node");
        dolphinRedN.attachObject(dolphinRedE);
        dolphinRedN.moveLeft(.35f);
        
        //Earth
        Entity earthE = sm.createEntity("earth", "earth.obj");
        earthE.setPrimitive(Primitive.TRIANGLES);
        SceneNode earthN = sm.getRootSceneNode().createChildSceneNode(earthE.getName() + "Node");
        earthN.attachObject(earthE);
        earthN.moveForward(8.0f);
        earthN.moveUp(3f);
        
        planets.add(earthN);
        
        //Moon
        Entity moonE = sm.createEntity("moon", "sphere.obj");
        moonE.setPrimitive(Primitive.TRIANGLES);
        SceneNode moonN = sm.getRootSceneNode().createChildSceneNode(moonE.getName() + "Node");
        moonN.scale(.6f, .6f, .6f);
        moonN.attachObject(moonE);
        setupOrbitController(moonN, earthN, 3f, 1f, sm);
        sm.getAmbientLight().setIntensity(new Color(.1f, .1f, .1f));
		
		Light plight = sm.createLight("testLamp1", Light.Type.POINT);
		plight.setAmbient(new Color(.3f, .3f, .3f));
        plight.setDiffuse(new Color(.7f, .7f, .7f));
		plight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
        plight.setRange(5f);
		SceneNode plightNode = sm.getRootSceneNode().createChildSceneNode("plightNode");
        plightNode.attachObject(plight);
        
        avatars.add(dolphinRedN);
        avatars.add(dolphinN);
        
        setupInputs(sm);
        setupOrbitCamera(eng,sm);
        
        ManualObject one = sm.createManualObject("1");
        ManualObject two = sm.createManualObject("2");
        /*ManualObject three = sm.createManualObject("3");
        ManualObject four = sm.createManualObject("4");
        ManualObject five = sm.createManualObject("5");
        ManualObject six = sm.createManualObject("6");*/
        
        ManualObjectSection oneSec = one.createManualSection("1Section");
        oneSec.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
        ManualObjectSection twoSec = one.createManualSection("2Section");
        twoSec.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
       /* ManualObjectSection threeSec = one.createManualSection("3Section");
        threeSec.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
        ManualObjectSection fourSec = one.createManualSection("4Section");
        fourSec.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
        ManualObjectSection fiveSec = one.createManualSection("5Section");
        fiveSec.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
        ManualObjectSection sixSec = one.createManualSection("6Section");
        sixSec.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));*/
        
        float[] oneVert = new float[] {
        		100f,5.5f,100f,
        		100f,5.5f,-100f,
        		-100f, 5.5f, 100f};
        float[] twoVert = new float[] {
        		100f, 5.5f, -100f,
        		-100f, 5.5f, 100f,
        		-100f, 5.5f, -100f
        };
        int[] indicies = new int[] {0, 1, 2};
        
        FloatBuffer oneVertBuff = BufferUtil.directFloatBuffer(oneVert);
        FloatBuffer twoVertBuff = BufferUtil.directFloatBuffer(twoVert);
        
        IntBuffer indexBuf = BufferUtil.directIntBuffer(indicies);
        
        oneSec.setVertexBuffer(oneVertBuff);
        oneSec.setIndexBuffer(indexBuf);
        twoSec.setVertexBuffer(twoVertBuff);
        twoSec.setIndexBuffer(indexBuf);
        
        //Material onemat = sm.getMaterialManager().getAssetByPath("default.mtl");
        //onemat.setEmissive(Color.BLACK);
        Texture oneTex = sm.getTextureManager().getAssetByPath("hexagons.jpeg");
        TextureState oneState = (TextureState)sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        oneState.setTexture(oneTex);
        //oneSec.setMaterial(onemat);
        one.setPrimitive(Primitive.TRIANGLES);
        SceneNode oneNode = sm.getRootSceneNode().createChildSceneNode("1Node");
        oneNode.attachObject(one);
        oneNode.moveLeft(50f);
        oneNode.moveDown(8f);
        
        
        
    }
    
    protected float distance(SceneNode n1, SceneNode n2) {
    	float x1, x2, y1, y2, z1, z2;
    	Vector3 pos1, pos2;
    	pos1 = n1.getLocalPosition();
    	pos2 = n2.getLocalPosition();
    	x1 = pos1.x();
    	x2 = pos2.x();
    	y1 = pos1.y();
    	y2 = pos2.y();
    	z1 = pos1.z();
    	z2 = pos2.z();
    	return (float)((Math.pow(x1 - x2, 2) ) + (Math.pow(y1 -y2, 2)) + (Math.pow(z1 - z2, 2)));
    }
    
    protected void collision(SceneManager sm, float minDistance) {
    	if(p1Wins || p2Wins == true) {}
    	else {
	    	float distance;
	    	Iterator<SceneNode> dolphinI = avatars.iterator();
	    	while(dolphinI.hasNext()) {
	    		SceneNode current = (SceneNode)dolphinI.next();
	    		Iterator<SceneNode> planetI = planets.iterator();
	    		while(planetI.hasNext()) {
	    			SceneNode planet = (SceneNode)planetI.next();
	    			distance = distance(current, planet);
	    			if (distance <= minDistance * minDistance) {
	    				if(current == avatars.get(0)) {
	    					setupOrbitController(sm.getSceneNode("moonNode"), current, 1.5f, 2f, sm);
	    					p2Wins = true;
	    				}
	    				else if (current == avatars.get(1)) {
	    					setupElevationController(planet, sm);
	    					p1Wins = true;
	    				}
	    			}
	    		}
	    	}
    	}
    	Iterator<SceneNode> p = avatars.iterator();
    	while(p.hasNext()) {
    		SceneNode current = p.next();
    		Vector3 loc = current.getLocalPosition();
    		if (loc.y() < -1f){
    			current.setLocalPosition((Vector3) Vector3f.createFrom(loc.x(), -1, loc.z()));
    		}
    	}
    }
    
    protected void setupOrbitController(SceneNode node, SceneNode target, float dist, float speed, SceneManager sm) {
    	OrbitController ocE = new OrbitController(target, speed, dist);
    	ocE.addNode(node);
    	sm.addController(ocE);
    }
    
    protected void setupElevationController(SceneNode target, SceneManager sm) {
    	ElevationController ecE = new ElevationController(target, .02f, 1f);
    	ecE.addNode(target);
    	sm.addController(ecE);
    }
    
    public float getElapsTime() {
    	return elapsTime;
    }
    
    protected void setupOrbitCamera(Engine eng, SceneManager sm) {
    	try {
    		if (im.getKeyboardName() == null)
    		throw new NullGamePadException(null);
    	}
    	catch (NullGamePadException e) {
    		e.printStackTrace();
    	}
    	SceneNode dolphinN = sm.getSceneNode("dolphinNode");
    	SceneNode cameraN = sm.getSceneNode("MainCameraNode");
    	Camera camera = sm.getCamera("MainCamera");
    	String kbName = im.getKeyboardName();
    	orbitController =
    			new Camera3Pcontroller(camera, cameraN, dolphinN, kbName, im);
    	
    	if(im.getFirstGamepadName() != null) {
    	SceneNode dolphinRedN = sm.getSceneNode("dolphinRedNode");
    	SceneNode camera2N = sm.getSceneNode("MainCamera2Node");
    	Camera camera2 = sm.getCamera("MainCamera2");
    	String gpName = im.getFirstGamepadName();
    	orbitController2 =
    			new Camera3Pcontroller(camera2, camera2N, dolphinRedN, gpName, im);
    	}
    }
    
    protected void setupInputs(SceneManager sm) {
    	System.out.println("Setting up inputs");
    	im = new GenericInputManager();
    	controllers = im.getControllers();
    	printControllers(controllers);
    	
    	
    	//DolphinInputs gamePad
    	try {
    		if (im.getKeyboardName() == null)
    			throw new NullKeyboardException(null);
    	}
    	catch (NullKeyboardException e) {
    		e.printStackTrace();
    	}
    	String kbName = im.getKeyboardName();
    	String gpName = im.getFirstGamepadName();
	    SceneNode dolphinN =
	    		sm.getSceneNode("dolphinNode");
	    SceneNode dolphinRedN =
	    		sm.getSceneNode("dolphinRedNode");
	    	//Player 1: keyboard actions
	    moveFwdAct = new MoveForwardAction(dolphinN);
	    im.associateAction(kbName,
	    		net.java.games.input.Component.Identifier.Key.W,
	    		moveFwdAct, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	    moveBwdAct = new MoveBackwardAction(dolphinN);
	    im.associateAction(kbName,
	    		net.java.games.input.Component.Identifier.Key.S, 
	    		moveBwdAct, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	    
	    moveLAct = new MoveLeftAction(dolphinN);
	    im.associateAction(kbName,
	    		net.java.games.input.Component.Identifier.Key.A, 
	    		moveLAct, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	    moveRAct = new MoveRightAction(dolphinN);
	    im.associateAction(kbName,
	    		net.java.games.input.Component.Identifier.Key.D, 
	    		moveRAct, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	    moveZAct = new MoveZAction(dolphinN);
	    
	    im.associateAction(kbName,
	    		net.java.games.input.Component.Identifier.Key.LSHIFT,
	    		moveZAct, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	    im.associateAction(kbName,
	    		net.java.games.input.Component.Identifier.Key.SPACE,
	    		moveZAct, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	    
	    	//Player 2: gamepad actions
	    if (gpName != null) {
		    gPYAct = new GamePadYAction(dolphinRedN);
		    im.associateAction(gpName,
		    		net.java.games.input.Component.Identifier.Axis.Y, 
		    		gPYAct, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    gPXAct = new GamePadXAction(dolphinRedN);
		    im.associateAction(gpName,
		    		net.java.games.input.Component.Identifier.Axis.X, 
		    		gPXAct, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    moveZAct = new MoveZAction(dolphinRedN);
		    im.associateAction(gpName,
		    		net.java.games.input.Component.Identifier.Button._5, 
		    		moveZAct, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    im.associateAction(gpName,
		    		net.java.games.input.Component.Identifier.Button._4, 
		    		moveZAct, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	    }
    }

    @Override
    protected void update(Engine engine) {
		// build and set HUD
		rs = (GL4RenderSystem) engine.getRenderSystem();
		elapsTime += engine.getElapsedTimeMillis();
		elapsTimeSec = Math.round(elapsTime/1000.0f);
		elapsTimeStr = Integer.toString(elapsTimeSec);
		counterStr = Integer.toString(counter);
		dispStr1 = "Visit the planet to win!";
		dispStr2 = "Visit the planet to win!";
		if(p1Wins == true) {
			dispStr1 = "YOU WIN!";
			dispStr2 = "YOU LOSE!";
		}
		else if (p2Wins == true) {
			dispStr2 = "YOU WIN!";
			dispStr1 = "YOU LOSE!";
		}
		//player 2 hud
		rs.setHUD(dispStr2, 15, 15);
		//player 1 hud
		rs.setHUD2(dispStr1, 15, ((int)(rs.getCanvas().getHeight()*.515f)));
		im.update(elapsTime);
		orbitController.updateCameraPosition();
		if (orbitController2 != null) {
			orbitController2.updateCameraPosition();
		}
		SceneManager sm = engine.getSceneManager();
		collision(sm, 3f);
    }
    
    public void printControllers(ArrayList<Controller> controllers) {
    	Iterator<Controller> it = controllers.iterator();
    	int i = 1;
    	if (it.hasNext()) {
    		System.out.println("Controllers detected...");
    		while (it.hasNext()) {
        		System.out.print("\tController " + i + ": " + it.next().getName());
        		i++;
    		}
    		System.out.println();
    	}
    	else {
    		System.out.println("Controllers not detected...");
    	}
    }
}
