package com.wasome.space_command.server;

import org.springframework.context.ApplicationContext;

import com.wasome.space_command.GameClient;

public interface ServerMessage {
	public void process(ApplicationContext context, GameClient client);
}
