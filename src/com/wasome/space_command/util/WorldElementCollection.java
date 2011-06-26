package com.wasome.space_command.util;

import static com.wasome.space_command.SpaceCommandGame.getWorld;
import static java.util.Collections.unmodifiableSet;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.wasome.space_command.Entity;
import com.wasome.space_command.behavior.HasBody;
import com.wasome.space_command.behavior.Visible;

@Visible
public class WorldElementCollection extends Entity {
	private final Set<Entity> _updatableThings = new LinkedHashSet<Entity>();
	private final Set<Entity> _renderableThings = new LinkedHashSet<Entity>();
	public final Set<Entity> updatableElements = unmodifiableSet(_updatableThings);
	public final Set<Entity> renderableElements = unmodifiableSet(_renderableThings);
	
	public void addElement(Entity element){
		_updatableThings.add(element);
		if(element.getClass().isAnnotationPresent(Visible.class)){
			_renderableThings.add(element);
		}
	}

	@Override
	public void render() {
		for(Entity entity : _renderableThings){
			entity.render();
		}
	}

	@Override
	public void update() {
		Iterator<Entity> iterator = _updatableThings.iterator();
		while(iterator.hasNext()){
			Entity updatable = iterator.next();
			updatable.update();
			if(updatable.isDestroyed()){
				iterator.remove();
				_renderableThings.remove(updatable);
				if(updatable.getClass().isAnnotationPresent(HasBody.class)){
					getWorld().remove(updatable.getBody());
				}
			}
		}
	}

	@Override
	public boolean isDestroyed() {
		return false;
	}
}
