package com.wasome.space_command.network;

import java.util.Collection;

import org.springframework.context.ApplicationContext;

import com.esotericsoftware.kryonet.Connection;
import com.wasome.space_command.GameServer;

public class RequestWorldSync implements ClientMessage {

	private transient Connection clientConnection;

	@Override
	public void clientPrepare() {
	}

	@Override
	public void serverProcess(ApplicationContext context, GameServer server, Collection<ServerMessage> returnMessageQueue) {
		WorldSync sync = context.getBean(WorldSync.class);
		sync.setConnection(clientConnection);
		returnMessageQueue.add(sync);
	}

	@Override
	public int getPlayerId() {
		return 0;
	}

	@Override
	public void setClientConnection(Connection clientConnection) {
		this.clientConnection = clientConnection;
	}

}
