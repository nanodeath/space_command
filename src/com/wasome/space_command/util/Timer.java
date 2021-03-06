package com.wasome.space_command.util;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.wasome.space_command.Entity;
import com.wasome.space_command.Game;
import com.wasome.space_command.GameServer;

@Component
public class Timer extends Entity {
	private TreeMap<Long, Task> onceAlarms = new TreeMap<Long, Task>();
	private long currentMs;
	private static final int threshold = 0;

	public void registerOnceAlarm(long delayMs, Task alarm) {
		onceAlarms.put(currentMs + delayMs, alarm);
	}

	@Override
	public void update() {
		currentMs = game.getGameContainer().getTime();
		// We only really want to go through the hassle of iterating
		// if we know there's something to execute
		if (!onceAlarms.isEmpty()
				&& onceAlarms.firstKey() <= currentMs + threshold) {
			Iterator<Map.Entry<Long, Task>> it = onceAlarms.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<Long, Task> entry = it.next();
				// We know it's sorted, so we can make this assumption (that the
				// first key we hit that's past the threshold means all the
				// remaining keys aren't applicable)
				if (entry.getKey() > currentMs + threshold) {
					break;
				}
				it.remove();
				entry.getValue().executeAlarmTask();
			}
		}
	}

	@Override
	public boolean isDestroyed() {
		return false;
	}

	public static interface Task {
		public void executeAlarmTask();
	}
}
