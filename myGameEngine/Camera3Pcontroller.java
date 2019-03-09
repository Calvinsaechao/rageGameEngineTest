package myGameEngine;

import ray.input.InputManager;
import ray.input.action.Action;
import ray.rage.scene.Camera;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class Camera3Pcontroller {
	private Camera camera;
	private SceneNode cameraN;
	private SceneNode target;
	private float cameraAzimuth;
	private float cameraElevation;
	private float radias;
	private Vector3 targetPos;
	private Vector3 worldUpVec;
	
	public Camera3Pcontroller(Camera cam, SceneNode camN,
			SceneNode targ, String controllerName,
			InputManager im) {
		camera = cam;
		cameraN = camN;
		target = targ;
		cameraAzimuth = 225.0f;
		cameraElevation = 20.0f;
		radias = 2.0f;
		worldUpVec = Vector3f.createFrom(0.0f, 1.0f, 0.0f);
		setupInput(im, controllerName);
	}
	public void updateCameraPosition() {
		double theta = Math.toRadians(cameraAzimuth);
		double phi = Math.toRadians(cameraElevation);
		double x = radias * Math.cos(phi) * Math.sin(theta);
		double y = radias * Math.sin(phi);
		double z = radias * Math.cos(phi) * Math.cos(theta);
		cameraN.setLocalPosition(Vector3f.createFrom
				((float)x, (float)y, (float)z).add(target.getWorldPosition()));
		cameraN.lookAt(target, worldUpVec);
	}
	public void setupInput(InputManager im, String cn) {
		Action orbitAAction = new OrbitAroundAction(this);
		Action orbitElevAction = new OrbitElevationAction(this);
		Action orbitRadAction = new OrbitRadiasAction(this);
			//Keyboard
		im.associateAction(cn,
				net.java.games.input.Component.Identifier.Key.LEFT,
				orbitAAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(cn,
				net.java.games.input.Component.Identifier.Key.RIGHT,
				orbitAAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		im.associateAction(cn,
				net.java.games.input.Component.Identifier.Key.UP,
				orbitElevAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(cn,
				net.java.games.input.Component.Identifier.Key.DOWN,
				orbitElevAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		im.associateAction(cn,
				net.java.games.input.Component.Identifier.Key.Q,
				orbitRadAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(cn,
				net.java.games.input.Component.Identifier.Key.E,
				orbitRadAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
			//GamePad
		im.associateAction(cn,
				net.java.games.input.Component.Identifier.Axis.RX,
				orbitAAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(cn,
				net.java.games.input.Component.Identifier.Axis.RY,
				orbitElevAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(cn,
				net.java.games.input.Component.Identifier.Axis.Z,
				orbitRadAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	}
	/**
	 * Retrieves cameraAzimuth
	 * @return float
	 */
	public float getCameraAzimuth() {
		return cameraAzimuth;
	}
	public float getCameraElevation() {
		return cameraElevation;
	}
	public float getRadias() {
		return radias;
	}
	
	public void setCameraAzimuth(float x) {
		cameraAzimuth = x % 360;
	}
	public void setCameraElevation(float x) {
		cameraElevation = x;
	}
	public void setRadias(float x) {
		radias = x % 360;
	}
}
