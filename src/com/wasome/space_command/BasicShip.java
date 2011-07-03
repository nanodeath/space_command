package com.wasome.space_command;

import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.Shape;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.wasome.space_command.behavior.Visible;
import com.wasome.space_command.components.BasicEngine;
import com.wasome.space_command.components.BasicGyroEngine;
import com.wasome.space_command.components.Engine;
import com.wasome.space_command.components.Engine.Direction;
import com.wasome.space_command.components.Inventory;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.player.Player;
import com.wasome.space_command.weapons.Gun;

@Component
@Scope("prototype")
@Visible
public class BasicShip extends Ship {
	@Autowired
	private Camera camera;

	private Image image;

	public BasicShip() throws SlickException {
		super();
		size = new Point<Float>(1f, 1f);

		image = new Image("res/spaceship.png");

		enableDirectControl();

	}

	public void init() {
		if (game.isServer()) {
			inventory = spring.getBean(Inventory.class);
			addComponent(inventory);

			// add rear engines
			// right engine
			Engine rightEngine = spring.getBean(BasicEngine.class);
			rightEngine.setFacingDirection(Direction.STERN);
			rightEngine.setLocalPosition(new Point<Float>(size.x / 2f, -size.y / 2));
			addComponent(rightEngine);

			// left engine
			Engine leftEngine = spring.getBean(BasicEngine.class);
			leftEngine.setFacingDirection(Direction.STERN);
			leftEngine.setLocalPosition(new Point<Float>(-size.x / 2f, -size.y / 2));
			addComponent(leftEngine);

			// adding lateral engines
			// right engine
			Engine rightLateralEngine = spring.getBean(BasicEngine.class);
			rightLateralEngine.setFacingDirection(Direction.STARBOARD);
			rightLateralEngine.setLocalPosition(new Point<Float>(size.x / 2f, 0f));
			addComponent(rightLateralEngine);

			// left engine
			Engine leftLateralEngine = spring.getBean(BasicEngine.class);
			leftLateralEngine.setFacingDirection(Direction.PORT);
			leftLateralEngine.setLocalPosition(new Point<Float>(-size.x / 2f, 0f));
			addComponent(leftLateralEngine);

			// adding gyro
			Engine gyroEngine = spring.getBean(BasicGyroEngine.class);
			addComponent(gyroEngine);

			// adding weapons
			Gun gun = spring.getBean(Gun.class);
			gun.setLocalPosition(new Point<Float>(0f, size.y / 2));
			addComponent(gun);

			for (Entity component : components.updatableElements) {
				game.addToGameWorld(component);
			}
		}
		player = spring.getBean("player1", Player.class);
		player.validateAllKeysAssigned();
	}

	@Override
	public void initializeAtLocation(Point<Float> position) {
		Shape shape = new Rectangle(size.x, size.y);
		body = game.bodyFactory.createDynamicBody(this, shape, position.x, position.y);
		body.setUserData(this);
		body.setAngularDamping(0.5f);
		game.getWorld().add(body);
	}

	private boolean accelerating = false, reversing = false, acceleratingRight = false, acceleratingLeft = false, turningCCW = false, turningCW = false;

	@Override
	public void update() {
		// updateComponents();
		performFlightPlan();
		if (directControlEnabled && player != null) {
			ClientState state = SpaceCommandGameServer.clientStates.get(1);
			if (state == null) {
				return;
			}
			// if (input.isInputDown(TURN_LEFT)) {
			// turnCounterClockwise();
			// turningCCW = true;
			// } else if (turningCCW) {
			// stopTurning();
			// turningCCW = false;
			// }
			// if (input.isInputDown(TURN_RIGHT)) {
			// turnClockwise();
			// turningCW = true;
			// } else if (turningCW) {
			// stopTurning();
			// turningCW = false;
			// }
			//
			if (state.isAccelerating) {
				accelerate();
				accelerating = true;
			} else if (accelerating) {
				stopAccelerating();
				accelerating = false;
			}
			// if (input.isInputDown(REVERSE)) {
			// reverse();
			// reversing = true;
			// } else if (reversing) {
			// stopReversing();
			// reversing = false;
			// }
			//
			// if (input.isInputDown(STRAFE_LEFT)) {
			// turnEnginesOnOff(Direction.STARBOARD, Direction.PORT);
			// acceleratingLeft = true;
			// } else if (acceleratingLeft) {
			// turnEnginesOnOff(null, Direction.STARBOARD);
			// acceleratingLeft = false;
			// }
			//
			// if (input.isInputDown(STRAFE_RIGHT)) {
			// turnEnginesOnOff(Direction.PORT, Direction.STARBOARD);
			// acceleratingRight = true;
			// } else if (acceleratingRight) {
			// turnEnginesOnOff(null, Direction.PORT);
			// acceleratingRight = false;
			// }
			//
			// if (input.isInputDown(EMERGENCY_STOP)) {
			// body.setVelocity(0f, 0f);
			// body.setAngularVelocity(0f);
			// }
		}
	}

	@Override
	public void render() {
		renderComponents();

		Point<Float> screenPoint = camera.worldToScreenCorner(body.getX(), body.getY(), size.x, size.y);
		Image image = getImage().copy();
		float rotation = (float) Math.toDegrees(body.getRotation());
		image.rotate(-rotation);
		game.getGraphics().drawImage(image, screenPoint.x, screenPoint.y);
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
