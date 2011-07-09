package com.wasome.space_command;

import static java.lang.System.currentTimeMillis;

import java.io.IOException;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.Resource;

import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.World;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.sun.org.apache.regexp.internal.REUtil;
import com.wasome.space_command.behavior.Visible;
import com.wasome.space_command.network.ClientMessage;
import com.wasome.space_command.network.ClientState;
import com.wasome.space_command.network.RequestWorldSync;
import com.wasome.space_command.network.ServerMessage;
import com.wasome.space_command.player.Player;
import com.wasome.space_command.ships.BasicShip;
import com.wasome.space_command.ships.Ship;
import com.wasome.space_command.util.Timer;
import com.wasome.space_command.util.ZIndexEntityComparator;

public class GameClient extends Game {
	@Autowired
	private Client client;
	private ClientState previousState;

	private static final Set<Entity> _renderableThings = new TreeSet<Entity>(new ZIndexEntityComparator());
	private final Queue<ServerMessage> updatesFromServer = new ConcurrentLinkedQueue<ServerMessage>();
	private final Queue<ClientMessage> updatesToServer = new ConcurrentLinkedQueue<ClientMessage>();

	@Autowired
	private Timer timer;

	@Autowired
	private Camera camera;

	@Resource(name = "player1")
	private Player player;

	private Entity cameraTarget;

	public static UnicodeFont FONT;

	public GameClient() {
		super("Sup bitches");
	}

	@Override
	public void init(GameContainer container) throws SlickException {

		KryoSupport.initializeKryo(client.getKryo());
		client.addListener(new Listener() {

			@Override
			public void connected(Connection connection) {
				System.out.println("Connected!  sending world sync request");
				RequestWorldSync requestWorldSync = new RequestWorldSync();
				connection.sendTCP(requestWorldSync);
			}

			@Override
			public void received(Connection connection, Object object) {
				if (object instanceof ServerMessage) {
					updatesFromServer.add((ServerMessage) object);
				}
			}
		});
		client.start();
	}

	@Override
	protected void firstUpdate() throws SlickException {
		previousState = spring.getBean(ClientState.class);

		world = new World(0f);

		addToGameWorld(timer);

		Space space = spring.getBean(Space.class);
		addToGameWorld(space);

		addToGameWorld(player);

		camera.firstUpdate();

		try {
			client.connect(5000, "localhost", 54555, 54777);
		} catch (IOException e) {
			throw new SlickException("Connection exception", e);
		}
	}

	@Override
	public void doRender() {
		if (cameraTarget != null) {
			camera.centerOnEntity(cameraTarget);
		}
		for (Entity entity : _renderableThings) {
			entity.render();
		}
	}

	private long lastSubmissionTime = 0;

	@Override
	public void doUpdate() {
		// process updates
		applyServerUpdates();

		// update my world
		world.update(((float) msDelta) / 1000f);

		// send inputs
		ClientState newState = previousState.dupe();
		newState.updateInput(input);
		if (!previousState.equivalentTo(newState) || currentTimeMillis() - lastSubmissionTime >= 2000l) {
			System.out.println("client: accelerating: " + newState.isAccelerating);
			lastSubmissionTime = currentTimeMillis();
			client.sendTCP(newState);
			previousState = newState;
		}

		// send updates/messages
		sendServerUpdates();
	}

	private void applyServerUpdates() {
		ServerMessage state;
		while ((state = updatesFromServer.poll()) != null) {
			state.process(spring, this);
		}
	}

	private void sendServerUpdates() {
		ClientMessage message;
		while ((message = updatesToServer.poll()) != null) {
			client.sendTCP(message);
		}
	}

	public static void main(String[] args) throws SlickException {
		AbstractXmlApplicationContext spring = new FileSystemXmlApplicationContext("config/space_command.client.xml");
		Game game = spring.getBean(Game.class);

		AppGameContainer app = new AppGameContainer(game);
		app.setUpdateOnlyWhenVisible(false);
		app.setAlwaysRender(true);

		// Run logic every 20 ms (50 times per second)
		app.setMinimumLogicUpdateInterval(20);
		app.setMaximumLogicUpdateInterval(20);

		app.setTargetFrameRate(60);
		app.setVSync(true);
		app.setDisplayMode(800, 600, false);
		app.start();
	}

	@Override
	public boolean isServer() {
		return false;
	}

	public Entity getOrCreateEntity(int entityId, Class<? extends Entity> entityClass) {

		Entity entity = entities.get(entityId);
		if (entity == null) {
			entity = spring.getBean(entityClass);
			entity.setEntityId(entityId);
			entity.setIsNew(true);
			entities.put(entityId, entity);
			_updatableThings.add(entity);
			addToGameWorld(entity);
			System.out.println("Creating entity on the client: " + entity.toString());
		} else {
			System.out.println("Fetching entity on the client: " + entity.toString());
		}
		if (entityClass == BasicShip.class && cameraTarget == null) {
			cameraTarget = entity;

			((Ship) entity).takeControl(player.getPlayerId());
		}
		return entity;
	}

	@Override
	public boolean closeRequested() {
		client.close();
		return super.closeRequested();
	}

	@Override
	public void addToGameWorld(Entity entity) {
		super.addToGameWorld(entity);
		if (entity.getClass().isAnnotationPresent(Visible.class)) {
			_renderableThings.add(entity);
		}
	}

	public void removeEntity(Entity e) {
		_updatableThings.remove(e);
		_renderableThings.remove(e);
		entities.remove(e.getEntityId());
		Body<?> body;
		if ((body = e.getBody()) != null) {
			world.remove(body);
		}
	}

	public void sendToServer(ClientMessage clientMessage) {
		updatesToServer.add(clientMessage);
	}

	public Player getPlayer() {
		return player;
	}
}
