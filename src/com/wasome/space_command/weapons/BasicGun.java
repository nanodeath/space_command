package com.wasome.space_command.weapons;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.wasome.space_command.SpaceCommandGame;
import com.wasome.space_command.data.Point;

@Component
@Scope("prototype")
public class BasicGun extends Gun {
	public BasicGun() {
		fireRate = 500;
	}

	@Override
	protected Projectile createProjectile() {
		Projectile bullet = SpaceCommandGame.spring.getBean(BasicBullet.class);
		Point<Float> location = ship.localToWorld(localPosition);
		bullet.initializeAtLocation(location);
		return bullet;
	}
	
	@Override
	public boolean isDestroyed() {
		return false;
	}
}
