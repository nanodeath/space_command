package com.wasome.space_command;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.fizzy.BoundingBox;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wasome.space_command.behavior.Visible;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.ships.Ship;

@Component
@Visible
public class Space extends Entity {
	private List<Ship> selectedShips = new LinkedList<Ship>();
	@Autowired
	private Camera camera;
	
	public Space() throws SlickException {
		super();
		image = new Image("res/nebula.jpg");
	}

	@Override
	public void render() {
		Graphics g = game.getGraphics();
		float screenLeftX = camera.getX() % image.getWidth();
		float screenLeftY = camera.getY() % image.getHeight();
		g.drawImage(image, screenLeftX, screenLeftY);
		g.drawImage(image, screenLeftX, screenLeftY - image.getHeight());
		g.drawImage(image, screenLeftX - image.getWidth(), screenLeftY);
		g.drawImage(image, screenLeftX - image.getWidth(), screenLeftY - image.getHeight());
		
		for (Ship ship : selectedShips) {
			BoundingBox bb = ship.getBody().getBoundingBox();
			float worldWidth = Math.abs(bb.upperRight.x - bb.lowerLeft.x);
			float worldHeight = Math.abs(bb.upperRight.y - bb.lowerLeft.y);
			Point<Float> corner = camera.worldToScreenCorner(
					(bb.upperRight.x + bb.lowerLeft.x) / 2,
					(bb.upperRight.y + bb.lowerLeft.y) / 2, worldWidth,
					worldHeight);
			Point<Float> size = camera.scaleWorldToScreen(worldWidth,
					worldHeight);
			g.setColor(Color.white);
			g.drawRect(corner.x, corner.y, size.x, size.y);
		}
	}

	@Override
	public void update() {
//		Input input = getInput();
//		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
//			int mouseX = input.getMouseX(), mouseY = input.getMouseY();
//			World world = getWorld();
//			Point<Float> worldPoint = camera.screenToWorld(mouseX, mouseY);
//			List<Body<?>> bodies = world.bodiesAt(worldPoint.x, worldPoint.y);
//			List<Ship> ships = new LinkedList<Ship>();
//			for (Body<?> body : bodies) {
//				Object userData = body.getUserData();
//				if (userData instanceof Ship) {
//					ships.add((Ship) userData);
//					break;
//				}
//			}
//			selectedShips.clear();
//			selectedShips.addAll(ships);
//		} else if(input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)){
//			int mouseX = input.getMouseX(), mouseY = input.getMouseY();
//			Point<Float> worldPoint = camera.screenToWorld(mouseX, mouseY);
//			
//			for(Ship selectedShip : selectedShips){
//				selectedShip.goTo(worldPoint);
//			}
//		}
	}

	@Override
	public boolean isDestroyed() {
		return false;
	}
}
