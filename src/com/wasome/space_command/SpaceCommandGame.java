package com.wasome.space_command;

import java.awt.Font;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.newdawn.fizzy.World;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.wasome.space_command.behavior.Visible;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.items.AssortedAmmo;
import com.wasome.space_command.items.Item;
import com.wasome.space_command.missions.CollectItemsMission;
import com.wasome.space_command.player.Player;
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
	
	@Resource(name="player1")
	private Player player1;

	public static UnicodeFont FONT;

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
		// fonts
		FONT = new UnicodeFont(new Font("Verdana", Font.PLAIN, 16));
		FONT.addAsciiGlyphs();
		FONT.addGlyphs(400, 600);
		//FONT.addGlyphs(Integer.parseInt("2610", 16), Integer.parseInt("2611", 16));// \\Add Glyphs
		FONT.addGlyphs("☑☐");
		FONT.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
		try {
			FONT.loadGlyphs();
		} catch (SlickException e) {
			throw new RuntimeException(e);
		}
		
		world = new World(0f);
		
		addToGameWorld(timer);
		
		Space space = spring.getBean(Space.class);
		addToGameWorld(space);
		
		Ship mainShip = spring.getBean(BasicShip.class);
		addToGameWorld(mainShip);
		mainShip.initializeAtLocation(new Point<Float>(25f, 20f));
		
		Ship mothership = spring.getBean(CatMothership.class);
		addToGameWorld(mothership);
		mothership.initializeAtLocation(new Point<Float>(10f, 15f));
		
		Item item = spring.getBean(AssortedAmmo.class);
		item.initializeAtLocation(new Point<Float>(5f, 25f));
		addToGameWorld(item);
		
		player1.giveMission(new CollectItemsMission(AssortedAmmo.class, 1));
		addToGameWorld(player1);
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
			entity.render();
		}
		rendering = false;
	}

	public static void main(String[] args) throws SlickException {
		AbstractXmlApplicationContext spring = new FileSystemXmlApplicationContext(
				"config/space_command.xml");
		SpaceCommandGame game = spring.getBean(SpaceCommandGame.class);
		SpaceCommandGame.spring = spring;

		AppGameContainer app = new AppGameContainer(game);

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
