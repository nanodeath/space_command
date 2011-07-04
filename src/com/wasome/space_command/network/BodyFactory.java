package com.wasome.space_command.network;

import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Shape;

import com.wasome.space_command.Entity;

public interface BodyFactory {

	public <T> DynamicBody<T> createDynamicBody(Entity entity, Shape shape, float x, float y);

}