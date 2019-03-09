package myGameEngine;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Node;

public class GamePadYAction extends AbstractInputAction{
	
private Node avN;
	
	public GamePadYAction(Node n) {
		avN = n;
	}
	
	@Override
	public void performAction(float time, Event e) {
		// TODO Auto-generated method stub
		float eValue = e.getValue();
		float buffer = 0.5f;
		float offset = 0.02f;
		if (eValue > buffer)
			avN.moveBackward(offset);
		else if (eValue < -buffer)
			avN.moveForward(offset);
	}

}
