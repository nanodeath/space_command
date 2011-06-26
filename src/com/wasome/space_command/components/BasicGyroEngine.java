package com.wasome.space_command.components;

import org.jbox2d.common.MathUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class BasicGyroEngine extends Engine {
	public BasicGyroEngine(){
		maximumThrust = 4f;
		pointingTowards = Direction.GYRO;
	}
	
	public BasicGyroEngine(Direction d) {
		this();
	}
	

	@Override
	public void setFacingDirection(Direction d) {
	}

	@Override
	public void update() {
		if(currentThrustPercent != 0f){
			float torque = currentThrustPercent * maximumThrust;
			ship.getBody().applyTorque(torque);
		}
	}
	
	@Override
	public void setOutput(float percent) {
		currentThrustPercent = MathUtils.clamp(percent, -1f, 1f);
	}

	@Override
	public void render() {

	}

	@Override
	public boolean isDestroyed() {
		return false;
	}
}
