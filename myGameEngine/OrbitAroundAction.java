package myGameEngine;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;

public class OrbitAroundAction extends AbstractInputAction{
	private Camera3Pcontroller orbitController;
	public OrbitAroundAction(Camera3Pcontroller orbitController) {
		this.orbitController = orbitController;
	}
	@Override
	public void performAction(float time, Event evt) {
		// TODO Auto-generated method stub
		float evtVal = evt.getValue();
		float rotAmount = .4f;
		String evtName = evt.getComponent().getName();
		if (evtName.equalsIgnoreCase("X Rotation")) {
			if(evtVal < -0.5)
				rotAmount *= -1f;
			else if(evtVal > 0.5) {}
			else rotAmount = 0.0f;
		}
		else if (evtName.equalsIgnoreCase("LEFT"))
			rotAmount *= -1;
		else if (evtName.equalsIgnoreCase("RIGHT")) {}
		orbitController.setCameraAzimuth(
				(orbitController.getCameraAzimuth()+rotAmount));
	}
}
