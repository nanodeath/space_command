package com.wasome.space_command;

import static java.lang.System.currentTimeMillis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.newdawn.fizzy.World;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.network.ClientState;
import com.wasome.space_command.network.SelectiveEntitySync;
import com.wasome.space_command.network.ServerMessage;
import com.wasome.space_command.network.WorldSync;
import com.wasome.space_command.network.ServerMessage.Audience;
import com.wasome.space_command.ships.BasicShip;
import com.wasome.space_command.ships.Ship;

public class GameServer extends Game {
	@Autowired
	protected Server server;

	private final Queue<ClientState> updatesFromClient = new ConcurrentLinkedQueue<ClientState>();
	public static final Map<Integer, ClientState> clientStates = new HashMap<Integer, ClientState>();
	private final Set<Entity> entitiesToUpdate = new LinkedHashSet<Entity>();
	private final Queue<ServerMessage> messagesToSend = new ConcurrentLinkedQueue<ServerMessage>();

	public static UnicodeFont FONT;

	public GameServer() {
		super("Sup bitches");
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		com.esotericsoftware.minlog.Log.set(com.esotericsoftware.minlog.Log.LEVEL_DEBUG);
		KryoSupport.initializeKryo(server.getKryo());
		server.addListener(new Listener() {
			@Override
			public void received(Connection connection, Object object) {
				if (object instanceof ClientState) {
					ClientState state = (ClientState) object;
					state.receivedAt = currentTimeMillis();
					updatesFromClient.add(state);
				} else if (object instanceof String) {
					String message = (String) object;
					if (message.equals("world_sync")) {
						WorldSync syncUp = spring.getBean(WorldSync.class);
						syncUp.setConnection(connection);
						messagesToSend.add(syncUp);
					}
				}
			}
		});

		server.start();
		try {
			server.bind(54555, 54777);
		} catch (IOException e) {
			throw new SlickException("Connection exception", e);
		}
	}

	@Override
	protected void firstUpdate() {
		world = new World(0f);

		addToGameWorld(timer);

		Space space = spring.getBean(Space.class);
		addToGameWorld(space);

		Ship mainShip = spring.getBean(BasicShip.class);
		addToGameWorld(mainShip);
		mainShip.initializeAtLocation(new Point<Float>(6.25f, 4.6875f));

		addToGameWorld(player1);
	}

	@Override
	public void doRender() {
		for (Entity entity : _updatableThings) {
			entity.render();
		}
	}

	@Override
	public void doUpdate() {
		// process updates
		applyClientUpdates();

		// evaluate world stuff
		Iterator<Entity> updatableIterator = _updatableThings.iterator();
		while(updatableIterator.hasNext()){
			Entity entity = updatableIterator.next();
			entity.update();
			if(entity.isDestroyed()){
				entitiesToUpdate.add(entity);
				updatableIterator.remove();
			}
		}
		world.update(((float) msDelta) / 1000f);

		// prepare updates
		if (!entitiesToUpdate.isEmpty()) {
			SelectiveEntitySync syncUp = spring.getBean(SelectiveEntitySync.class);
			syncUp.setEntitiesToUpdate(entitiesToUpdate);
			messagesToSend.add(syncUp);
		}

		// finally send all the updates!
		ServerMessage message;
		while ((message = messagesToSend.poll()) != null) {
			int clientId = message.getClientId();
			message.prepareToSend();
			if (clientId == Audience.EVERYBODY) {
				server.sendToAllTCP(message);
			} else {
				server.sendToTCP(clientId, message);
			}
		}
		entitiesToUpdate.clear();
	}

	private void applyClientUpdates() {
		ClientState state;
		while ((state = updatesFromClient.poll()) != null) {
			clientStates.put(state.playerId, state);
		}
	}

	public static void main(String[] args) throws SlickException {
		AbstractXmlApplicationContext spring = new FileSystemXmlApplicationContext("config/space_command.server.xml");
		GameServer game = spring.getBean(GameServer.class);

		AppGameContainer app = new AppGameContainer(game);
		app.setUpdateOnlyWhenVisible(false);
		app.setAlwaysRender(true);

		// Run logic every 20 ms (50 times per second)
		app.setMinimumLogicUpdateInterval(40);
		app.setMaximumLogicUpdateInterval(40);
		app.setTargetFrameRate(60);

		app.setVSync(true);
		app.setDisplayMode(400, 300, false);
		app.start();
	}

	@Override
	public boolean closeRequested() {
		server.close();
		return super.closeRequested();
	}

	@Override
	public boolean isServer() {
		return true;
	}

	public void trackEntity(Entity enhancedObject) {
		if (!entities.containsKey(enhancedObject.entityId)) {
			entities.put(enhancedObject.entityId, enhancedObject);
		}
	}

	public void updateEntityOnClients(Entity enhancedObject) {
		System.out.println("Updating " + enhancedObject.toString() + " on client");
		trackEntity(enhancedObject);
		entitiesToUpdate.add(enhancedObject);
	}
}
