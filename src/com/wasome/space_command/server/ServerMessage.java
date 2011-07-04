package com.wasome.space_command.server;

import org.springframework.context.ApplicationContext;

import com.wasome.space_command.GameClient;

public interface ServerMessage {
	public void process(ApplicationContext context, GameClient client);
	public static class Audience {
		public static final int UNDEFINED = -1, EVERYBODY = -2;
	}
	
	public void prepareToSend();
	public int getClientId();
}
