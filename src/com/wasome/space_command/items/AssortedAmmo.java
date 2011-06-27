package com.wasome.space_command.items;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.wasome.space_command.data.Point;

@Component
@Scope("prototype")
public class AssortedAmmo extends Item {
	@Override
	public void initializeAtLocation(Point<Float> world) {
		super.initializeAtLocation(world);
		body.setAngularVelocity(0.5f);
	}

	public AssortedAmmo() throws SlickException {
		size = new Point<Float>(1.5f, 1.5f);
		inventorySize = new Point<Integer>(1, 1);
		spaceImage = new Image("res/Ammo-Container.48.png");
		inventoryImage = spaceImage;
	}
}