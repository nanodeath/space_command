package com.wasome.space_command.weapons;

import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.Shape;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.wasome.space_command.Camera;
import com.wasome.space_command.SpaceCommandGame;
import com.wasome.space_command.data.Point;

@Component
@Scope("prototype")
public class BasicBullet extends Projectile {
	@Autowired
	private Camera camera;
	
	private Point<Float> size = new Point<Float>(0.1f, 0.1f); 
	private Point<Float> screenSize;
	
	@Override
	public void initializeAtLocation(Point<Float> position) {
		Shape rectangle = new Rectangle(size.x, size.y);
		body = new DynamicBody<Projectile>(rectangle, position.x, position.y);
		body.setBullet(true);
		screenSize = camera.scaleWorldToScreen(size.x, size.y);
	}

	@Override
	public void render() {
		Graphics g = SpaceCommandGame.getGraphics();
		Point<Float> screen = camera.worldToScreen(body.getX(), body.getY());
		g.setColor(Color.white);
		g.fillRect(screen.x, screen.y, screenSize.x, screenSize.y);
	}

	@Override
	public void update() {
		// TODO should hit things
	}

	@Override
	public boolean isDestroyed() {
		// TODO when it has hit something, it should die.
		return false;
	}

	@Override
	public float getInitialSpeed() {
		return 10f;
	}

}
