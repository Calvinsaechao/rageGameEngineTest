package myGameEngine;

import ray.input.action.AbstractInputAction;

public class OrbitRadiasAction extends AbstractInputAction{
	private Camera3Pcontroller control;
	public OrbitRadiasAction(Camera3Pcontroller control) {
		this.control = control;
	}
	@Override
	public void performAction(float time, net.java.games.input.Event evt) {
		// TODO Auto-generated method stub
		float evtVal = evt.getValue();
		float zoomAmount = 0.01f;
		String evtName = evt.getComponent().getName();
		if(evtName.equalsIgnoreCase("Z Axis")) {
			if(evtVal < -0.5)
				zoomAmount *= -1f;
			else if(evtVal > 0.5) {}
			else zoomAmount = 0.0f;
		}
		else if (evtName.equalsIgnoreCase("Q")) {}
		else if (evtName.equalsIgnoreCase("E"))
			zoomAmount *=-1f;
		control.setRadias(
				(control.getRadias()+zoomAmount));
	}
}
