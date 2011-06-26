package com.wasome.space_command;

import java.util.LinkedHashSet;
import java.util.Set;

import org.newdawn.fizzy.World;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.wasome.space_command.behavior.Visible;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.util.Timer;

@Component
public final class SpaceCommandGame extends BasicGame {
	public static AbstractXmlApplicationContext spring;

	private static boolean updating;

	private static boolean rendering;
	
	private static GameContainer gameContainer;
	private static int msDelta;
	private static Graphics graphics;
	private static Input input;
	private static World world;

	private static final Set<Entity> updatableThings = new LinkedHashSet<Entity>();
	private static final Set<Entity> renderableThings = new LinkedHashSet<Entity>();
	
	@Autowired
	private Timer timer;

	public SpaceCommandGame() {
		super("Game");
	}
	
	public static final GameContainer getGameContainer(){
		if(!updating && !rendering){
			throw new ConstructNotAvailableException(); 
		}
		return gameContainer;
	}
	public static final int getTimeDelta(){
		if(!updating){
			throw new ConstructNotAvailableException();
		}
		return msDelta;
	}
	public static final Graphics getGraphics(){
		if(!rendering){
			throw new ConstructNotAvailableException();
		}
		return graphics;
	}
	public static final Input getInput(){
		if(!updating){
			throw new ConstructNotAvailableException();
		}
		return input;
	}
	public static final World getWorld(){
		return world;
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		world = new World(0f);
		
		addToGameWorld(timer);
		
		Space space = spring.getBean(Space.class);
		addToGameWorld(space);
//		updatableThings.add(space);
//		renderableThings.add(space);
		
		Ship mainShip = spring.getBean(BasicShip.class);
		addToGameWorld(mainShip);
		mainShip.initializeAtLocation(new Point<Float>(25f, 20f));
//		ships.add(mainShip);
//		updatableThings.add(mainShip);
//		renderableThings.add(mainShip);
		
//		Ship otherShip = spring.getBean(BasicShip.class);
//		addToGameWorld(otherShip);
//		ships.add(otherShip);
//		updatableThings.add(otherShip);
//		renderableThings.add(otherShip);
		
		Ship mothership = spring.getBean(CatMothership.class);
		addToGameWorld(mothership);
		mothership.initializeAtLocation(new Point<Float>(10f, 15f));
//		ships.add(mothership);
//		updatableThings.add(mothership);
//		renderableThings.add(mothership);
	}

	@Override
	public void update(GameContainer gc, int msDelta) throws SlickException {
		gameContainer = gc;
		SpaceCommandGame.msDelta = msDelta;
		SpaceCommandGame.input = gameContainer.getInput();
		updating = true;
		for(Entity entity : updatableThings){
			entity.update();
		}
		world.update(((float)msDelta) / 1000f);
		updating = false;
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		gameContainer = gc;
		graphics = g;
		
		rendering = true;
		for(Entity entity : renderableThings){
			Visible visible = entity.getClass().getAnnotation(Visible.class);
			switch(visible.when()){
			case ALWAYS:
				entity.render();
				break;
			case CONDITIONALLY:
				if(entity.shouldRender())
					entity.render();
				break;
			}
		}
		rendering = false;
	}

	public static void main(String[] args) throws SlickException {
		AbstractXmlApplicationContext spring = new FileSystemXmlApplicationContext(
				"config/space_command.xml");
		SpaceCommandGame foo = spring.getBean(SpaceCommandGame.class);
		SpaceCommandGame.spring = spring;

		AppGameContainer app = new AppGameContainer(foo);

		// Run logic every 20 ms (50 times per second)
		app.setMinimumLogicUpdateInterval(20);
		app.setMaximumLogicUpdateInterval(20);

		app.setVSync(true);
		app.setDisplayMode(1024, 768, false);
		app.start();
	}

	public static void addToGameWorld(Entity entity) {
		updatableThings.add(entity);
		if(entity.getClass().isAnnotationPresent(Visible.class)){
			renderableThings.add(entity);
		}
	}
}
