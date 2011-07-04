package com.wasome.space_command;

import org.newdawn.fizzy.Body;
import org.newdawn.slick.Image;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.wasome.space_command.data.Point;
import com.wasome.space_command.util.WorldElementCollection;

abstract public class Entity implements ApplicationContextAware {
	protected ApplicationContext spring;
	private boolean isNew = true;
	private float zIndex = 0f;
	protected WorldElementCollection subEntities;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		spring = applicationContext;
	}

	@Autowired
	protected Game game;
	
	@Autowired(required=false)
	protected GameClient client;
	
	@Autowired(required=false)
	protected GameServer server;

	protected Image image;

	public void setImage(Image image) {
		this.image = image;
	}

	public void render() {
	}

	public void update() {
		if (isNew)
			isNew = false;
	}
	
	public void init(){
		if(server != null && this instanceof SentToClient){
			server.updateEntityOnClients(this);
		}
	}

	public void addToWorld() {
		game.addToGameWorld(this);
	}
	
	public void initializeAtLocation(Point<Float> point) {	
	}

	public boolean isDestroyed() {
		return false;
	}

	protected Body body;

	public Body<?> getBody() {
		return body;
	}

	public static int ENTITY_ID_COUNTER = 0;
	protected int entityId = ENTITY_ID_COUNTER++;

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setIsNew(boolean flag) {
		isNew = flag;
	}

	public float getZIndex() {
		return zIndex;
	}

	/**
	 * Sets the zIndex of this element after it's been added to an ordered set.
	 * @param zIndex
	 */
	public void setZIndex(float zIndex) {
		this.zIndex = zIndex;
	}
	
	public WorldElementCollection getSubEntities(){
		return subEntities;
	}
}
