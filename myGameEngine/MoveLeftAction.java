package myGameEngine;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Node;

public class MoveLeftAction extends AbstractInputAction{
	
private Node avN;
	
	public MoveLeftAction(Node n) {
		avN = n;
	}
	
	@Override
	public void performAction(float time, Event e) {
		// TODO Auto-generated method stub
		avN.moveLeft(-0.02f);
	}

}
