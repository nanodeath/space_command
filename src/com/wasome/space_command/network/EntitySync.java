package com.wasome.space_command.network;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import com.wasome.space_command.Entity;
import com.wasome.space_command.GameClient;

public class EntitySync implements ServerMessage {
	public List<ClientUpdate> clientUpdates;

	public EntitySync() {
	}

	public EntitySync(List<ClientUpdate> clientUpdates) {
		this.clientUpdates = clientUpdates;
	}

	/**
	 * Nothing special needs to be done on the server side to prepare the client
	 * updates for transfer -- they're sent as-is.
	 */
	public void prepareToSend() {
	}

	@Override
	public void process(ApplicationContext context, GameClient client) {
		Set<Entity> newEntities = new HashSet<Entity>();
		for (ClientUpdate clientUpdate : clientUpdates) {
			Entity e = client.getOrCreateEntity(clientUpdate.entityId, clientUpdate.getEntityClass());
			if (e.isNew()) {
				newEntities.add(e);
			}
			clientUpdate.setApplicationContext(context);
			clientUpdate.applyToEntity(e);
			
			if(e.isDestroyed()){
				client.removeEntity(e);
			}
		}
		for (Entity newEntity : newEntities) {
			newEntity.setIsNew(false);
		}
	}

	/**
	 * By default broadcasts to everyone.
	 */
	@Override
	public int getClientId() {
		return Audience.EVERYBODY;
	}
}
