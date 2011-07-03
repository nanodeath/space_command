package com.wasome.space_command.server;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import com.wasome.space_command.Entity;
import com.wasome.space_command.SpaceCommandGameClient;

public class WorldSync implements ServerMessage {
	public List<ClientUpdate> clientUpdates;

	public WorldSync(){}
	public WorldSync(List<ClientUpdate> clientUpdates){
		this.clientUpdates = clientUpdates;
	}
	
	@Override
	public void process(ApplicationContext context, SpaceCommandGameClient client) {
		Set<Entity> newEntities = new HashSet<Entity>();
		for(ClientUpdate clientUpdate : clientUpdates){
			Entity e = client.getOrCreateEntity(clientUpdate.entityId, clientUpdate.getEntityClass());
			if(e.isNew()){
				newEntities.add(e);
			}
			clientUpdate.setApplicationContext(context);
			clientUpdate.applyToEntity(e);
		}
		for(Entity newEntity : newEntities){
			newEntity.setIsNew(false);
		}
	}

}
