package com.wasome.space_command.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Queue class -- a listener can subscribe to all events by class. Inheritance
 * and interfaces are respected. First the event is broadcast on the event
 * class's channel, then the channels of each of its interfaces, then the
 * superclass's channel, then each of the super
 * 
 * @author max
 * 
 */
public class Queue {
	private Map<Class<?>, Set<QueueListener>> listeners = new HashMap<Class<?>, Set<QueueListener>>();

	public void addListener(Class<?> eventType, QueueListener listener) {
		Set<QueueListener> currentListeners = listeners.get(eventType);
		if (currentListeners == null) {
			currentListeners = new HashSet<QueueListener>();
			listeners.put(eventType, currentListeners);
		}
		currentListeners.add(listener);
	}

	public void publish(Event event) {
		Class<?> klass = event.getClass();
		// tested this actually IS necessary, since a class and a subclass
		// could both separately implement an interface and we don't want
		// to double-publish a channel
		Set<Class<?>> alreadyPublishedInterfaces = new HashSet<Class<?>>();
		while (klass != null) {
			publish(event, klass);
			for (Class<?> anInterface : klass.getInterfaces()) {
				if (!alreadyPublishedInterfaces.contains(anInterface)) {
					alreadyPublishedInterfaces.add(anInterface);
					publish(event, anInterface);
				}
			}
			klass = klass.getSuperclass();
		}
	}

	private void publish(Event event, Class<?> klass) {
		Set<QueueListener> eventListeners = listeners.get(klass);
		if (eventListeners != null) {
			for (QueueListener listener : eventListeners) {
				listener.receive(event);
			}
		}
	}

	public void removeListener(QueueListener listener) {
		for(Map.Entry<?, Set<QueueListener>> entry : listeners.entrySet()){
			entry.getValue().remove(listener);
		}
	}
}
