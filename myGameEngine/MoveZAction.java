package myGameEngine;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Node;

public class MoveZAction extends AbstractInputAction{
	
private Node avN;
	
	public MoveZAction(Node n) {
		avN = n;
	}
	
	@Override
	public void performAction(float time, Event e) {
		// TODO Auto-generated method stub
		float offset = 0.02f;
		String key = e.getComponent().getName();
		float evtValue = e.getValue();
		if (key.equalsIgnoreCase("Left Shift") || key.equalsIgnoreCase("Button 4"))
			avN.moveDown(offset);
		else
			avN.moveUp(offset);
	}

}
