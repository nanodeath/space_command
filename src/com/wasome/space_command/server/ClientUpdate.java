package com.wasome.space_command.server;

import java.nio.ByteBuffer;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.esotericsoftware.kryo.Kryo;
import com.wasome.space_command.Entity;
import com.wasome.space_command.SentToClient;

public class ClientUpdate implements ApplicationContextAware {
	public String entityClass;
	public int entityId;
	public byte[] data;
	private transient Kryo kryo;
	
	public void setEntity(Entity entity){
		ByteBuffer buffer = ByteBuffer.allocate(256);
		((SentToClient) entity).writeObjectData(kryo, buffer);
		buffer.flip();
		data = buffer.array();
		entityClass = entity.getClass().getName();
		entityId = entity.getEntityId();
	}

	@SuppressWarnings("unchecked")
	public Class<? extends Entity> getEntityClass() {
		try {
			return (Class<? extends Entity>) Class.forName(entityClass);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void applyToEntity(Entity entity) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		((SentToClient) entity).readObjectData(kryo, buffer);
		entity.setEntityId(entityId);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		kryo = applicationContext.getBean(Kryo.class);
	}
}