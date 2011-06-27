package com.wasome.space_command.events;

import com.wasome.space_command.Ship;
import com.wasome.space_command.items.Item;

public class AcquiredItemEvent extends Event {
	public final Ship ship;
	public final Item item;

	public AcquiredItemEvent(Ship ship, Item item) {
		this.ship = ship;
		this.item = item;
	}
}
