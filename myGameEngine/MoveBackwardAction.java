package myGameEngine;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Node;

public class MoveBackwardAction extends AbstractInputAction{
	
private Node avN;
	
	public MoveBackwardAction(Node n) {
		avN = n;
	}
	
	@Override
	public void performAction(float time, Event e) {
		// TODO Auto-generated method stub
		avN.moveBackward(0.02f);
	}

}
