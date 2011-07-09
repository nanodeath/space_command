package com.wasome.space_command.network;

import org.springframework.context.ApplicationContext;

import com.wasome.space_command.GameClient;

public class AssignPlayerId implements ServerMessage {

	@Override
	public void process(ApplicationContext context, GameClient client) {
		// TODO Auto-generated method stub

	}

	@Override
	public void prepareToSend() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getClientId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
