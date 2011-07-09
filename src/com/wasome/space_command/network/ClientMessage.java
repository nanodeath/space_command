package com.wasome.space_command.network;

import java.util.Collection;

import org.springframework.context.ApplicationContext;

import com.esotericsoftware.kryonet.Connection;
import com.wasome.space_command.GameServer;

/**
 * Messages sent from the client to the server.
 * @author max
 *
 */
public interface ClientMessage {
	/**
	 * Send from the client to the server.
	 */
	public void clientPrepare();
	public void setClientConnection(Connection clientConnection);
	/**
	 * Process on the server.
	 * @param context
	 * @param server
	 * @param returnMessageQueue
	 */
	public void serverProcess(ApplicationContext context, GameServer server, Collection<ServerMessage> returnMessageQueue);
	public int getPlayerId(); 
}
