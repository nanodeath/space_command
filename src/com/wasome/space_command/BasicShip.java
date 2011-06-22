package com.wasome.space_command;

import static com.wasome.space_command.SpaceCommandGame.getInput;

import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.Shape;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.wasome.space_command.components.BasicEngine;
import com.wasome.space_command.components.Engine;
import com.wasome.space_command.components.Engine.Direction;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.util.PointUtil;

@Component
@Scope("prototype")
public class BasicShip extends Ship {
	@Autowired
	private Camera camera;

	private Image image;

	public BasicShip() throws SlickException {
		super();
		size = new Point<Float>(1f, 1f);
		Shape shape = new Rectangle(size.x, size.y);
		body = new DynamicBody(shape, 10f, 10f);
		body.setAngularDamping(0.5f);

		SpaceCommandGame.getWorld().add(body);
		image = new Image("res/spaceship.png");
		enableDirectControl();

		// add engines
		// right engine
		Engine rightEngine = SpaceCommandGame.spring.getBean(BasicEngine.class);
		rightEngine.setFacingDirection(Direction.STERN);
		rightEngine
				.setLocalPosition(new Point<Float>(size.x / 2f, -size.y / 2));
		addComponent(rightEngine);

		// left engine
		Engine leftEngine = SpaceCommandGame.spring.getBean(BasicEngine.class);
		leftEngine.setFacingDirection(Direction.STERN);
		leftEngine
				.setLocalPosition(new Point<Float>(-size.x / 2f, -size.y / 2));
		addComponent(leftEngine);
	}

	private boolean accelerating = false, reversing = false;

	@Override
	public void update() {
		updateComponents();
		System.out.println("Angular velocity: " + body.getAngularVelocity()
				+ "\t, rotation: " + body.getRotation());
		if (directControlEnabled) {
			Input input = getInput();
			if (input.isKeyDown(Input.KEY_Q)) {
				if (!input.isKeyDown(Input.KEY_E))
					body.setAngularVelocity(-1f);
				// turnCounterClockwise();
			} else if (input.isKeyDown(Input.KEY_E)) {
				// turnClockwise();
				body.setAngularVelocity(1f);
			}
			if (input.isKeyDown(Input.KEY_W)) {
				accelerate();
				System.out.println("Accelerating");
				accelerating = true;
			} else if (accelerating) {
				stopAccelerating();
				accelerating = false;
			}
			if (input.isKeyDown(Input.KEY_S)) {
				reverse();
				reversing = true;
			} else if (reversing) {
				stopReversing();
				reversing = false;
			}
			if (input.isKeyDown(Input.KEY_G)) {
				body.setVelocity(0f, 0f);
				body.setAngularVelocity(0f);
			}
		}
	}

	@Override
	public void render() {
		renderComponents();

		Point<Float> screenPoint = camera.worldToScreenCorner(body.getX(),
				body.getY(), size.x, size.y);
		Image image = getImage().copy();
		float rotation = (float) Math.toDegrees(body.getRotation());
		image.rotate(rotation);
		SpaceCommandGame.getGraphics().drawImage(image, screenPoint.x,
				screenPoint.y);
	}

	@Override
	protected Image getImage() {
		return image;
	}
}
