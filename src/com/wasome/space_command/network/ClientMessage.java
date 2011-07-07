package com.wasome.space_command.network;

import org.springframework.context.ApplicationContext;

import com.wasome.space_command.GameServer;

/**
 * Messages sent from the client to the server.
 * @author max
 *
 */
public interface ClientMessage {
	public void prepareToSend();
	public void process(ApplicationContext context, GameServer server);
	public int getPlayerId(); 
}
