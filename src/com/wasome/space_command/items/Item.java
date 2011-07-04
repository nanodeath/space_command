package com.wasome.space_command.items;

import javax.annotation.Resource;

import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.CollisionEvent;
import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.Shape;
import org.newdawn.fizzy.WorldListener;
import org.newdawn.slick.Image;
import org.springframework.beans.factory.annotation.Autowired;

import com.wasome.space_command.Camera;
import com.wasome.space_command.Entity;
import com.wasome.space_command.behavior.Visible;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.events.AcquiredItemEvent;
import com.wasome.space_command.events.Queue;
import com.wasome.space_command.player.Inventory;
import com.wasome.space_command.ships.Ship;

@Visible
abstract public class Item extends Entity implements WorldListener {

	protected Inventory inventory;
	protected Image spaceImage;
	protected Image inventoryImage;
	protected Point<Float> position;
	protected Point<Float> size;
	protected Point<Integer> inventorySize;
	protected Point<Integer> positionInIqnventory;
	
	@Resource(name="mainQueue")
	private Queue mainQueue;

	@Autowired
	private Camera camera;

	public void initializeAtLocation(Point<Float> world) {
		this.position = world;
		
		Shape shape = new Rectangle(size.x, size.y);
		body = new DynamicBody<Item>(shape, world.x, world.y);
		body.setUserData(this);

		game.getWorld().add(body);
		game.getWorld().addBodyListener(body, this);
	}

	@Override
	public void render() {
		if (inventory == null) {
			Point<Float> screen = camera.worldToScreenCorner(body.getX(),
					body.getY(), size.x, size.y);
			Image image = spaceImage.copy();
			image.rotate((float) Math.toDegrees(-body.getRotation()));
			
			game.getGraphics().drawImage(image, screen.x,
					screen.y);
		}
	}

	public Image getInventoryImage() {
		return inventoryImage;
	}
	
	public Point<Integer> getInventorySize(){
		return inventorySize;
	}

	public void addToInventory(Inventory i) {
		inventory = i;
		position = null;
		body.setActive(false);
		mainQueue.publish(new AcquiredItemEvent(i.getShip(), this));
	}

	public void dropIntoSpace(Point<Float> worldPosition) {
		inventory = null;
		position = worldPosition;
		body.setActive(true);
	}
	

	@Override
	public void collided(CollisionEvent event) {
		Body<?> other = event.getBodyA() == body ? event.getBodyB() : event.getBodyA();
		Object userData = other.getUserData();
		if(userData instanceof Ship){
			Ship otherShip = (Ship)userData;
			Inventory inv = otherShip.getInventory();
			if(inv != null && inv.put(this)){
				addToInventory(inv);
			}
		}
	}

	@Override
	public void separated(CollisionEvent event) {
	}
}
