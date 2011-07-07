package com.wasome.space_command.network;

import org.springframework.context.ApplicationContext;

import com.wasome.space_command.GameServer;
import com.wasome.space_command.ships.Ship;

public class TakeControl implements ClientMessage {

	private int playerId;
	private int shipId;

	public TakeControl() {
	}

	public TakeControl(int playerId, Ship ship) {
		this.playerId = playerId;
		shipId = ship.getEntityId();
	}

	@Override
	public void prepareToSend() {
	}

	@Override
	public void process(ApplicationContext context, GameServer server) {
		Ship ship = (Ship) server.getEntity(shipId);
		ship.takeControl(playerId);
	}

	@Override
	public int getPlayerId() {
		return playerId;
	}

}
