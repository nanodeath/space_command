package com.wasome.space_command.weapons;

import com.wasome.space_command.Entity;
import com.wasome.space_command.SentToClient;
import com.wasome.space_command.data.Point;


abstract public class Projectile extends Entity implements SentToClient {
	abstract public void initializeAtLocation(Point<Float> worldPosition);

	abstract public float getInitialSpeed();
}
