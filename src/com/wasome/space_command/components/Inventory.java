package com.wasome.space_command.components;

import static com.wasome.space_command.SpaceCommandGame.getGraphics;
import static com.wasome.space_command.SpaceCommandGame.getInput;

import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.wasome.space_command.ShipComponent;
import com.wasome.space_command.behavior.Visible;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.items.Item;

@Visible
@Component
@Scope("prototype")
public class Inventory extends ShipComponent {
	private boolean visible = false;
	private float sizePerTile = 48f;
	private int tilesWide = 4;
	private int tilesTall = 4;
	private float leftOffset = 10f;
	private float topOffset = 10f;
	// columns, then rows
	private Item[][] stuff;
	
	{
		stuff = new Item[tilesWide][];
		for(int w = 0; w < tilesWide; w++){
			stuff[w] = new Item[tilesTall];
		}
	}

	@Override
	public void update() {
		if (getInput().isKeyPressed(Input.KEY_I)) {
			visible = !visible;
		}
	}

	@Override
	public void render() {
		if (visible) {
			Graphics g = getGraphics();
			g.setAntiAlias(true);
			// draw background
			Color color = new Color(Color.blue);
			color.a = 0.5f;
			g.setColor(color);
			float width = tilesWide * sizePerTile;
			float height = tilesTall * sizePerTile;
			g.fillRoundRect(leftOffset, topOffset, width, height, 2);

			// draw border lines
			g.setColor(Color.cyan);
			g.drawRoundRect(leftOffset, topOffset, width, height, 2);

			// draw horizontal lines
			for(int i = 1; i < tilesTall; i++){
				float x1 = leftOffset;
				float x2 = leftOffset + tilesWide * sizePerTile;
				float y = topOffset + i * sizePerTile;
				g.drawLine(x1, y, x2, y);
			}
			// draw vertical lines
			for (int i = 1; i < tilesWide; i++) {
				float x = leftOffset + i * sizePerTile;
				float y1 = topOffset;
				float y2 = topOffset + tilesTall * sizePerTile;
				g.drawLine(x, y1, x, y2);
			}
			// draw items
			Set<Item> items = new HashSet<Item>();
			for(int invW = 0; invW < stuff.length; invW++){
				for(int invH = 0; invH < stuff[invW].length; invH++){
					Item item = stuff[invW][invH];
					if(item != null && !items.contains(item)){
						items.add(item);
						Image im = item.getInventoryImage();
						g.drawImage(im, leftOffset + invW * sizePerTile, topOffset + invH * sizePerTile);
					}
				}
			}
			g.setAntiAlias(false);
		}
	}

	/**
	 * 
	 * @param item
	 * @return true if successful, false otherwise (due to space or weight constraints)
	 */
	public boolean put(Item item) {
		Point<Integer> size = item.getInventorySize();
		// find a spot that fits
		boolean foundSpot = false;
		int foundW = 0, foundH = 0;
		for(int invW = 0; invW < stuff.length && !foundSpot; invW++){
			for(int invH = 0; invH < stuff[invW].length && !foundSpot; invH++){
				// is space taken?
				if(stuff[invW][invH] == null){
					foundSpot = true;
					foundW = invW;
					foundH = invH;
					inner_loop:
					for(int w = 0; w < size.x; w++){
						for(int h = 0; h < size.y; h++){
							if(invW + w >= stuff.length || invH + h >= stuff[invW].length){
								foundSpot = false;
								// part of the item would fall outside the inventory
								break inner_loop;
							}
							if(stuff[invW + w][invH + h] != null){
								foundSpot = false;
								// selected spot is simply taken
								break inner_loop;
							}
						}
					}
				}
			}
		}
		if(foundSpot){
			for(int w = 0; w < size.x; w++){
				for(int h = 0; h < size.y; h++){
					stuff[foundW + w][foundH + h] = item;
				}
			}
		}
		return foundSpot;
	} 
}
