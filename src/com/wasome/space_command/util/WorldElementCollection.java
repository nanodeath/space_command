package com.wasome.space_command.util;

import static java.util.Collections.unmodifiableSet;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.wasome.space_command.Entity;
import com.wasome.space_command.behavior.HasBody;
import com.wasome.space_command.behavior.Visible;

@Visible
@Component
@Scope("prototype")
public class WorldElementCollection extends Entity {
	private final Set<Entity> _updatableThings = new LinkedHashSet<Entity>();
	private final Set<Entity> _renderableThings = new LinkedHashSet<Entity>();
	public final Set<Entity> updatableElements = unmodifiableSet(_updatableThings);
	public final Set<Entity> renderableElements = unmodifiableSet(_renderableThings);

	public void addElement(Entity element) {
		_updatableThings.add(element);
		if (element.getClass().isAnnotationPresent(Visible.class)) {
			_renderableThings.add(element);
		}
	}

	@Override
	public void render() {
		for (Entity entity : _renderableThings) {
			entity.render();
		}
	}

	@Override
	public void update() {
		Iterator<Entity> iterator = _updatableThings.iterator();
		while (iterator.hasNext()) {
			Entity updatable = iterator.next();
			updatable.update();
			if (updatable.isDestroyed()) {
				iterator.remove();
				_renderableThings.remove(updatable);
				if (updatable.getClass().isAnnotationPresent(HasBody.class)) {
					game.getWorld().remove(updatable.getBody());
				}
			}
		}
	}

	@Override
	public boolean isDestroyed() {
		return false;
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
