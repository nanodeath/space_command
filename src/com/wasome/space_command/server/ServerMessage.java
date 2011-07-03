package com.wasome.space_command.server;

import org.springframework.context.ApplicationContext;

import com.wasome.space_command.SpaceCommandGameClient;

public interface ServerMessage {
	public void process(ApplicationContext context, SpaceCommandGameClient client);
}
