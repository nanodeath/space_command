package com.wasome.space_command.weapons;

import java.nio.ByteBuffer;

import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.Shape;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esotericsoftware.kryo.Kryo;
import com.wasome.space_command.Camera;
import com.wasome.space_command.behavior.Visible;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.util.Timer;
import com.wasome.space_command.util.Timer.Task;

@Component
@Scope("prototype")
@Visible
public class BasicBullet extends Projectile {
	@Autowired
	private Camera camera;

	@Autowired
	private Timer timer;

	private static final Point<Float> size = new Point<Float>(0.1f, 0.1f);
	private Point<Float> screenSize;

	@Override
	public void initializeAtLocation(Point<Float> position) {
		Shape rectangle = new Rectangle(size.x, size.y);
		body = new DynamicBody<Projectile>(rectangle, position.x, position.y);
		body.setBullet(true);
		game.getWorld().add(body);
		screenSize = camera.scaleWorldToScreen(size.x, size.y);
		timer.registerOnceAlarm(1000, new SelfDestruct());
	}

	@Override
	public void render() {
		Graphics g = game.getGraphics();
		Point<Float> screen = camera.worldToScreen(body.getX(), body.getY());
		g.setColor(Color.white);
		g.fillRect(screen.x, screen.y, screenSize.x, screenSize.y);
	}

	@Override
	public void update() {
		// TODO should hit things
	}

	@Override
	public float getInitialSpeed() {
		return 10f;
	}

	private class SelfDestruct implements Task {

		@Override
		public void executeAlarmTask() {
			isDestroyed = true;
		}
	}

	@Override
	public void writeObjectData(Kryo kryo, ByteBuffer buffer) {
		buffer.put((byte) (isDestroyed ? 1 : 0));
		if (!isDestroyed) {
			buffer.putFloat(body.getX());
			buffer.putFloat(body.getY());
			buffer.putFloat(body.getXVelocity());
			buffer.putFloat(body.getYVelocity());
			buffer.putFloat(body.getAngularVelocity());
			buffer.putFloat(body.getRotation());
		}
	}

	@Override
	public void readObjectData(Kryo kryo, ByteBuffer buffer) {
		byte destroyed = buffer.get();
		if (destroyed == 1) {
			isDestroyed = true;
		} else {
			float x = buffer.getFloat();
			float y = buffer.getFloat();
			if (isNew()) {
				initializeAtLocation(new Point<Float>(x, y));
			} else {
				body.setPosition(x, y);
			}
			body.setVelocity(buffer.getFloat(), buffer.getFloat());
			body.setAngularVelocity(buffer.getFloat());
			body.setRotation(buffer.getFloat());
		}
	}
}
