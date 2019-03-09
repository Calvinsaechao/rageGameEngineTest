package myGameEngine;

import ray.input.action.AbstractInputAction;

public class OrbitElevationAction extends AbstractInputAction{
	private Camera3Pcontroller control;
	public OrbitElevationAction(Camera3Pcontroller control) {
		this.control = control;
	}
	@Override
	public void performAction(float time, net.java.games.input.Event evt) {
		// TODO Auto-generated method stub
		float evtVal = evt.getValue();
		float elevation = 0.4f;
		String evtName = evt.getComponent().getName();
		if(evtName.equalsIgnoreCase("Y Rotation")) {
			if(evtVal < -0.5) {}
			else if(evtVal > 0.5)
				elevation *= -1;
			else elevation = 0.0f;
			control.setCameraElevation(
					control.getCameraElevation() + elevation);
		}
		else if (evtName.equalsIgnoreCase("UP")) {}
		else if (evtName.equalsIgnoreCase("DOWN")) {
			elevation *= -1;
		}
		control.setCameraElevation(
				control.getCameraElevation() + elevation);
	}
}
