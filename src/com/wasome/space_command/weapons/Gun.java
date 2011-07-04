package com.wasome.space_command.weapons;

import com.wasome.space_command.ClientState;
import com.wasome.space_command.GameServer;
import com.wasome.space_command.behavior.Visible;
import com.wasome.space_command.components.ShipComponent;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.util.PointUtil;
import com.wasome.space_command.util.WorldElementCollection;

@Visible
abstract public class Gun extends ShipComponent {
	private long lastFiredAt = 0;
	protected int fireRate = 0;
	protected Point<Float> localPosition;
	
	public void init(){
		super.init();
		subEntities = spring.getBean(WorldElementCollection.class);
	}

	public Point<Float> getLocalPosition() {
		return localPosition;
	}

	public void setLocalPosition(Point<Float> localPosition) {
		this.localPosition = localPosition;
	}

	@Override
	public void update() {
		subEntities.update();
		ClientState state = GameServer.clientStates.get(1);
		if (state == null) {
			return;
		}
		if(state.isShooting){
			long currentTime = server.getGameContainer().getTime();
			if (currentTime - lastFiredAt >= fireRate) {
				lastFiredAt = currentTime;
				fire();
			}
		}
	}
	
	@Override
	public void render() {
		subEntities.render();
	}

	protected void fire() {
		Projectile projectile = createProjectile();
		subEntities.addElement(projectile);
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
