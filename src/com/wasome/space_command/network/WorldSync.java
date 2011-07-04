package com.wasome.space_command.network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esotericsoftware.kryonet.Connection;
import com.wasome.space_command.Entity;
import com.wasome.space_command.GameClient;
import com.wasome.space_command.GameServer;
import com.wasome.space_command.util.WorldElementCollection;

@Component
@Scope("prototype")
public class WorldSync extends EntitySync implements ApplicationContextAware {
	private transient ApplicationContext context;
	@Autowired
	private transient GameServer server;
	private transient int clientId;

	public WorldSync(){}
	
	public void setConnection(Connection clientConnection){
		this.clientId = clientConnection.getID();
	}
	
	/**
	 * Prepare to send the sync payload -- happens on the server.
	 */
	@Override
	public void prepareToSend(){
		clientUpdates = new ArrayList<ClientUpdate>();
		for (Entity entity : server.updatableThings) {
			if (entity instanceof SentToClient) {
				server.trackEntity(entity);
				
				ClientUpdate cu = context.getBean(ClientUpdate.class);
				cu.setEntity(entity);
				clientUpdates.add(cu);
			}
			WorldElementCollection subEntities = entity.getSubEntities();
			if (subEntities != null) {
				for (Entity subEntity : subEntities.transitiveClosureUpdatable()) {
					if (subEntity instanceof SentToClient) {
						server.trackEntity(entity);
						
						ClientUpdate cu = context.getBean(ClientUpdate.class);
						cu.setEntity(subEntity);
						clientUpdates.add(cu);
					}
				}
			}
		}
	}
//	
//	/**
//	 * Process the payload on the client.
//	 */
//	@Override
//	public void process(ApplicationContext context, GameClient client) {
//		Set<Entity> newEntities = new HashSet<Entity>();
//		for(ClientUpdate clientUpdate : clientUpdates){
//			Entity e = client.getOrCreateEntity(clientUpdate.entityId, clientUpdate.getEntityClass());
//			if(e.isNew()){
//				newEntities.add(e);
//			}
//			clientUpdate.setApplicationContext(context);
//			clientUpdate.applyToEntity(e);
//		}
//		for(Entity newEntity : newEntities){
//			newEntity.setIsNew(false);
//		}
//	}

	@Override
	public int getClientId() {
		return clientId;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
}
