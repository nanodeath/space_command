package com.wasome.space_command.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.wasome.space_command.WorldElement;
import com.wasome.space_command.WorldRenderable;
import com.wasome.space_command.WorldUpdatable;

public class WorldElementCollection implements WorldElement {
	private final Set<WorldUpdatable> updatableThings = new HashSet<WorldUpdatable>();
	private final Set<WorldRenderable> renderableThings = new HashSet<WorldRenderable>();
	
	public void addElement(WorldElement element){
		updatableThings.add(element);
		renderableThings.add(element);
	}
	
	public void addElement(WorldRenderable renderable){
		renderableThings.add(renderable);
	}
	
	public void addElement(WorldUpdatable updatable){
		updatableThings.add(updatable);
	}

	@Override
	public void render() {
		for(WorldRenderable renderable : renderableThings){
			renderable.render();
		}
	}

	@Override
	public void update() {
		Iterator<WorldUpdatable> iterator = updatableThings.iterator();
		while(iterator.hasNext()){
			WorldUpdatable updatable = iterator.next();
			updatable.update();
			if(updatable.isDestroyed()){
				iterator.remove();
			}
		}
	}

	@Override
	public boolean isDestroyed() {
		return false;
	}

}
