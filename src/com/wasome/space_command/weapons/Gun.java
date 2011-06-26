package com.wasome.space_command.weapons;

import static com.wasome.space_command.SpaceCommandGame.getGameContainer;
import static com.wasome.space_command.SpaceCommandGame.getInput;
import static com.wasome.space_command.SpaceCommandGame.getWorld;

import org.newdawn.slick.Input;

import com.wasome.space_command.ShipComponent;
import com.wasome.space_command.behavior.Visible;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.util.PointUtil;
import com.wasome.space_command.util.WorldElementCollection;

@Visible
abstract public class Gun extends ShipComponent {
	private long lastFiredAt = 0;
	protected int fireRate = 0;
	protected Point<Float> localPosition;

	protected WorldElementCollection projectiles = new WorldElementCollection();

	public Point<Float> getLocalPosition() {
		return localPosition;
	}

	public void setLocalPosition(Point<Float> localPosition) {
		this.localPosition = localPosition;
	}

	@Override
	public void update() {
		projectiles.update();
		if (getInput().isKeyDown(Input.KEY_SPACE)) {
			long currentTime = getGameContainer().getTime();
			if (currentTime - lastFiredAt >= fireRate) {
				lastFiredAt = currentTime;
				fire();
			}
		}
	}
	
	@Override
	public void render() {
		projectiles.render();
	}

	protected void fire() {
		Projectile projectile = createProjectile();
		getWorld().add(projectile.getBody());
		projectiles.addElement(projectile);
		Point<Float> worldPosition = ship.localToWorld(localPosition);
		projectile.getBody().setPosition(worldPosition.x, worldPosition.y);
		float rotation = ship.getBody().getRotation();
		projectile.getBody().setRotation(rotation);
		
		Point<Float> projectileVelocity = PointUtil.rotateAbout(Point.ORIGIN_FLOAT, new Point<Float>(0f, getInitialProjectileVelocity(projectile)), ship.getRotation(), true);
		projectile.getBody().setVelocity(projectileVelocity.x, projectileVelocity.y);
	}

	abstract protected Projectile createProjectile();
	
	protected float getInitialProjectileVelocity(Projectile projectile){
		return projectile.getInitialSpeed();
	}
}
