package com.wasome.space_command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.wasome.space_command.data.Point;

@Component
@Scope("singleton")
public class Camera {
	private float x;
	private float y;
	private float screenHeight, screenWidth;
	/**
	 * By what factor is a screen unit larger than a world unit?
	 */
	private float scale = 32f;
	
	@Autowired
	private Game game;

	public Camera() {
		x = y = 0f;
	}
	
	public void firstUpdate(){
		screenHeight = game.gameContainer.getHeight();
		screenWidth = game.gameContainer.getWidth();
	}

	/**
	 * Convert the given screen coordinates to world coordinates, taking into account the current position of the camera.
	 * @param screenX x-coordinate on the screen in pixels
	 * @param screenY y-coordinate on the screen in pixels
	 * @return converted point in world units
	 */
	public Point<Float> screenToWorld(float screenX, float screenY) {
		return new Point<Float>((screenX - x) / scale, -((screenY - y) - screenHeight)/scale);
	}

	/**
	 * Convert the given world coordinates to screen coordinates, taking into account the current position of the camera.
	 * @param worldX x-coordinate in the world in "world units"
	 * @param worldY y-coordinate in the world in "world units"
	 * @return converted point in pixels
	 */
	public Point<Float> worldToScreen(float worldX, float worldY) {
		return new Point<Float>(worldX * scale + x, -worldY * scale
				+ screenHeight + y);
	}
	
	/**
	 * Convert the given world coordinates to screen coordinates, but first finding the top-left
	 * corner of the implicit world object.
	 * @param worldX the center x-coord of an object in the world
	 * @param worldY the center y-coord of an object in the world
	 * @param worldW the width of an object
	 * @param worldH the height of an object
	 * @return converted point in pixels
	 */
	public Point<Float> worldToScreenCorner(float worldX, float worldY, float worldW, float worldH){
		Point<Float> point = worldToScreen(worldX, worldY);
		Point<Float> size = scaleWorldToScreen(worldW, worldH);
		// TODO not sure why this +16 is necessary on the 16 size :(
		// At least it's only in one place, now.
		return new Point<Float>(point.x - size.x / 2, point.y - size.y / 2);
	}

	/**
	 * Scale the provided coordinates from world units to screen pixels.  IGNORES any current camera position.
	 * @param worldX
	 * @param worldY
	 * @return 
	 */
	public Point<Float> scaleWorldToScreen(float worldX, float worldY) {
		return new Point<Float>(worldX * scale, worldY * scale);
	}

	/**
	 * Scale the provided coordinates from screen pixels to world units.  IGNORES any current camera position.
	 * @param screenX
	 * @param screenY
	 * @return 
	 */
	public Point<Float> scaleScreenToWorld(float screenX, float screenY) {
		return new Point<Float>(screenX / scale, screenY / scale);
	}

	/**
	 * Move the camera to the given screen coordinates.
	 * @param screenX
	 * @param screenY
	 */
	public void moveTo(float screenX, float screenY) {
		x = screenX;
		y = screenY;
	}

	public final float getX() {
		return x;
	}

	public final float getY() {
		return y;
	}
	
	public final float getScale() {
		return scale;
	}
	
	public void centerOnEntity(Entity e){
		Point<Float> center = scaleWorldToScreen(e.getBody().getX(), e.getBody().getY());
		x = -center.x + screenWidth / 2;
		y = center.y - screenHeight / 2;
	}
}
