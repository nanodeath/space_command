package com.wasome.space_command.network;

import java.util.Collection;

import org.springframework.context.ApplicationContext;

import com.esotericsoftware.kryonet.Connection;
import com.wasome.space_command.GameServer;
import com.wasome.space_command.ships.Ship;

public class TakeControl implements ClientMessage {
	private int shipId;
	private transient Connection clientConnection;

	public TakeControl() {
	}

	public TakeControl(Ship ship) {
		shipId = ship.getEntityId();
	}

	@Override
	public void clientPrepare() {
	}

	@Override
	public void serverProcess(ApplicationContext context, GameServer server, Collection<ServerMessage> returnMessageQueue) {
		Ship ship = (Ship) server.getEntity(shipId);
		ship.takeControl(clientConnection.getID());
	}

	@Override
	public void setClientConnection(Connection clientConnection) {
		this.clientConnection = clientConnection;
	}
}
