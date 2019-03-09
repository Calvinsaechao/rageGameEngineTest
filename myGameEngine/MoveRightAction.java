package myGameEngine;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Node;

public class MoveRightAction extends AbstractInputAction{
	
private Node avN;
	
	public MoveRightAction(Node n) {
		avN = n;
	}
	
	@Override
	public void performAction(float time, Event e) {
		// TODO Auto-generated method stub
		avN.moveRight(-0.02f);
	}

}
