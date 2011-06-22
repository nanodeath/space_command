package com.wasome.space_command;

import org.newdawn.fizzy.Circle;
import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.Shape;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wasome.space_command.data.Point;

@Component
public class CatMothership extends Ship {
	private Image image;
	@Autowired
	private Camera camera;

	@SuppressWarnings("rawtypes")
	public CatMothership() throws SlickException {
		super();
		size = new Point<Float>(8f, 8f);
		Shape shape = new Circle(size.x / 2);
		body = new DynamicBody(shape, 10f, 15f);

		SpaceCommandGame.getWorld().add(body);
		body.setAngularVelocity(0.05f);
		image = new Image("res/hovership.png");
	}

	@Override
	public void render() {
		renderComponents();

		Point<Float> screenPoint = camera.worldToScreenCorner(body.getX(),
				body.getY(), size.x, size.y);
		Image image = getImage().copy();
		float rotation = (float) Math.toDegrees(body.getRotation());
		image.rotate(-rotation);
		SpaceCommandGame.getGraphics().drawImage(image, screenPoint.x,
				screenPoint.y);
	}

	@Override
	public void update() {
		updateComponents();
	}

	@Override
	protected Image getImage() {
		return image;
	}

}
