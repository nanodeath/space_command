package com.wasome.space_command;

import org.newdawn.fizzy.Body;
import org.newdawn.slick.Image;

public class Entity {
	protected Image image;
	public void setImage(Image image){
		this.image = image;
	}
	
	public void render(){}
	
	public void update(){}
	
	public void addToWorld(){
		SpaceCommandGame.addToGameWorld(this);
	}
	
	public boolean shouldRender(){
		return true;
	}
	
	public boolean isDestroyed(){
		return false;
	}
	
	protected Body<?> body;
	public Body<?> getBody(){
		return body;
	}
}
