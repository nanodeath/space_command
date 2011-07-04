package com.wasome.space_command;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.newdawn.fizzy.World;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.wasome.space_command.network.BodyFactory;
import com.wasome.space_command.player.Player;
import com.wasome.space_command.util.Timer;

public abstract class Game extends BasicGame implements ApplicationContextAware {
	@Autowired
	public BodyFactory bodyFactory;

	protected boolean updating;
	protected boolean rendering;
	protected GameContainer gameContainer;
	protected int msDelta;
	protected Graphics graphics;
	protected Input input;
	protected World world;
	protected ApplicationContext spring;
	protected final Set<Entity> _updatableThings = new LinkedHashSet<Entity>();
	public final Set<Entity> updatableThings = Collections.unmodifiableSet(_updatableThings);
	@Autowired
	protected Timer timer;
	@Resource(name = "player1")
	protected Player player1;
	
	protected final Map<Integer, Entity> entities = new LinkedHashMap<Integer, Entity>();


	public Game(String title) {
		super(title);
	}
	
	@Override
	final public void render(GameContainer container, Graphics graphics)
			throws SlickException {
		this.gameContainer = container;
		this.graphics = graphics;
		rendering = true;
		doRender();
		rendering = false;
	}
	public abstract void doRender();

	int updateCount = 0;
	@Override
	final public void update(GameContainer container, int delta)
			throws SlickException {
		this.gameContainer = container;
		this.input = gameContainer.getInput();
		msDelta = delta;
		updating = true;
		if(updateCount == 0){
			firstUpdate();
		}
		doUpdate();
		updating = false;
		updateCount++;
	}
	public abstract void doUpdate();
	protected void firstUpdate() throws SlickException {}

	public void addToGameWorld(Entity entity) {
		_updatableThings.add(entity);
	}

	public World getWorld() {
		return world;
	}
	
	public Graphics getGraphics(){
		return graphics;
	}

	public GameContainer getGameContainer() {
		return gameContainer;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		spring = applicationContext;
	}

	public Input getInput() {
		return input;
	}
	
	abstract public boolean isServer();
	
	public Entity getEntity(Integer id){
		return entities.get(id);
	}
}