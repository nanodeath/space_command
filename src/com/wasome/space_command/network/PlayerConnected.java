package com.wasome.space_command.network;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.esotericsoftware.kryonet.Connection;
import com.wasome.space_command.GameClient;
import com.wasome.space_command.GameServer;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.ships.BasicShip;
import com.wasome.space_command.ships.Ship;

@Component
public class PlayerConnected implements ServerMessage, ApplicationContextAware {

	private transient int clientId;
	@Autowired
	private transient GameServer server;
	private transient ApplicationContext context;

	@Override
	public void process(ApplicationContext context, GameClient client) {
		// TODO Auto-generated method stub

	}

	@Override
	public void prepareToSend() {
		Ship mainShip = context.getBean(BasicShip.class);
		server.addToGameWorld(mainShip);
		mainShip.initializeAtLocation(new Point<Float>(6.25f, 4.6875f));
		mainShip.takeControl(clientId);
	}

	@Override
	public int getClientId() {
		return clientId;
	}

	public void setConnection(Connection conn){
		this.clientId = conn.getID();
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
}