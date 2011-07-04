package com.wasome.space_command.server;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.wasome.space_command.Entity;

@Component
@Scope("prototype")
public class SelectiveEntitySync extends EntitySync implements ApplicationContextAware {
	private transient ApplicationContext spring;

	private transient Collection<Entity> entitiesToUpdate;

	public SelectiveEntitySync() {
	}

	public void setEntitiesToUpdate(Collection<Entity> entitiesToUpdate) {
		this.entitiesToUpdate = entitiesToUpdate;
	}

	@Override
	public void prepareToSend() {
		clientUpdates = new ArrayList<ClientUpdate>();
		for (Entity entity : entitiesToUpdate) {
			ClientUpdate update = spring.getBean(ClientUpdate.class);
			update.setEntity(entity);
			clientUpdates.add(update);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.spring = context;
	}
}
