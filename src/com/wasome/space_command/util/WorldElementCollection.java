package com.wasome.space_command.util;

import static java.util.Collections.unmodifiableSet;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.newdawn.fizzy.Body;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.wasome.space_command.Entity;

@Component
@Scope("prototype")
public class WorldElementCollection extends Entity {
	private final Set<Entity> _elements = new LinkedHashSet<Entity>();
	public final Set<Entity> updatableElements = unmodifiableSet(_elements);

	public void addElement(Entity element) {
		_elements.add(element);
	}

	@Override
	public void render() {
		for (Entity entity : _elements) {
			entity.render();
		}
	}

	@Override
	public void update() {
		Iterator<Entity> iterator = _elements.iterator();
		while (iterator.hasNext()) {
			Entity updatable = iterator.next();
			updatable.update();
			if (updatable.isDestroyed()) {
				server.updateEntityOnClients(updatable);
				iterator.remove();
				Body<?> body;
				if ((body = updatable.getBody()) != null) {
					game.getWorld().remove(body);
				}
			}
		}
	}

	public Set<Entity> transitiveClosureUpdatable() {
		return transitiveClosureUpdatable(null);
	}

	/**
	 * @param ignore
	 *            the entity to ignore, to prevent infinite recursion.
	 * @return
	 */
	private Set<Entity> transitiveClosureUpdatable(Entity ignore) {
		Set<Entity> entities = new HashSet<Entity>();
		for (Entity entity : updatableElements) {
			if (entity != ignore && !entities.contains(entity)) {
				entities.add(entity);
				WorldElementCollection subEntities = entity.getSubEntities();
				if (subEntities != null) {
					entities.addAll(subEntities.transitiveClosureUpdatable(ignore));
				}
			}
		}
		return entities;
	}
}
