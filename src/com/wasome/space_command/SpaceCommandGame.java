package com.wasome.space_command;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.newdawn.fizzy.World;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

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

	private final Set<WorldUpdatable> updatableThings = new HashSet<WorldUpdatable>();
	private final Set<WorldRenderable> renderableThings = new HashSet<WorldRenderable>();

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
	
	private List<Ship> ships = new LinkedList<Ship>();

	@Override
	public void init(GameContainer arg0) throws SlickException {
		world = new World(0f);
		Ship mainShip = spring.getBean(BasicShip.class);
		ships.add(mainShip);
		updatableThings.add(mainShip);
		renderableThings.add(mainShip);
		
		Ship mothership = spring.getBean(CatMothership.class);
		ships.add(mothership);
		updatableThings.add(mothership);
		renderableThings.add(mothership);
	}

	@Override
	public void update(GameContainer gc, int msDelta) throws SlickException {
		gameContainer = gc;
		SpaceCommandGame.msDelta = msDelta;
		SpaceCommandGame.input = gameContainer.getInput();
		updating = true;
		for(WorldUpdatable thing : updatableThings){
			thing.update();
		}
		world.update(((float)msDelta) / 1000f);
		updating = false;
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		gameContainer = gc;
		graphics = g;
		
		rendering = true;
		for(WorldRenderable thing : renderableThings){
			thing.render();
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

}
