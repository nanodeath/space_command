package com.wasome.space_command.ships;

import org.newdawn.fizzy.Circle;
import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Shape;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wasome.space_command.Camera;
import com.wasome.space_command.behavior.Visible;
import com.wasome.space_command.data.Point;

@Component
@Visible
public class CatMothership extends Ship {
	private Image image;
	@Autowired
	private Camera camera;

	public CatMothership() throws SlickException {
		super();
		size = new Point<Float>(8f, 8f);
		image = new Image("res/hovership.png");
	}

	@Override
	public void initializeAtLocation(Point<Float> point) {
		Shape shape = new Circle(size.x / 2);
		body = new DynamicBody<Ship>(shape, point.x, point.y);
		body.setUserData(this);
		game.getWorld().add(body);
		body.setAngularVelocity(0.05f);
	}

	@Override
	public void render() {
		renderComponents();

		Point<Float> screenPoint = camera.worldToScreenCorner(body.getX(),
				body.getY(), size.x, size.y);
		Image image = getImage().copy();
		float rotation = (float) Math.toDegrees(body.getRotation());
		image.rotate(-rotation);
		game.getGraphics().drawImage(image, screenPoint.x,
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

	@Override
	public boolean isDestroyed() {
		return false;
	}

}
