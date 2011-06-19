package com.wasome.space_command;

import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public final class SpaceCommandGame extends BasicGame {
	private AbstractXmlApplicationContext spring;

	private static boolean updating;

	private static boolean rendering;
	
	private static GameContainer gameContainer;
	private static int msDelta;
	private static Graphics graphics;

	private final Set<WorldUpdatable> updatableThings = new HashSet<WorldUpdatable>();
	private final Set<WorldRenderable> renderableThings = new HashSet<WorldRenderable>();

	public SpaceCommandGame() {
		super("Game");
	}
	
	public static GameContainer getGameContainer(){
		if(!updating && !rendering){
			throw new ConstructNotAvailableException(); 
		}
		return gameContainer;
	}
	public static int getTimeDelta(){
		if(!updating){
			throw new ConstructNotAvailableException();
		}
		return msDelta;
	}
	public static Graphics getGraphics(){
		if(!rendering){
			throw new ConstructNotAvailableException();
		}
		return graphics;
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(GameContainer gc, int msDelta) throws SlickException {
		gameContainer = gc;
		SpaceCommandGame.msDelta = msDelta;
		
		updating = true;
		for(WorldUpdatable thing : updatableThings){
			thing.update();
		}
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
		foo.spring = spring;

		AppGameContainer app = new AppGameContainer(foo);

		// Run logic every 20 ms (50 times per second)
		app.setMinimumLogicUpdateInterval(20);
		app.setMaximumLogicUpdateInterval(20);

		app.setVSync(true);
		app.setDisplayMode(1024, 768, false);
		app.start();
	}

}
