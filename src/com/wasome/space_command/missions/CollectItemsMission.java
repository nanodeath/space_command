package com.wasome.space_command.missions;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.wasome.space_command.SpaceCommandGame;
import com.wasome.space_command.events.AcquiredItemEvent;
import com.wasome.space_command.events.Event;
import com.wasome.space_command.events.Queue;
import com.wasome.space_command.events.QueueListener;
import com.wasome.space_command.items.Item;

@Component
@Scope("prototype")
public class CollectItemsMission extends Mission implements QueueListener {
	private Class<? extends Item> itemClass;
	private int count, countGoal;
	private boolean finished;

	private Queue mainQueue;

	public CollectItemsMission(Class<? extends Item> itemClass, int countGoal) {
		this.itemClass = itemClass;
		this.count = 0;
		this.countGoal = countGoal;
		finished = false;
		mainQueue = SpaceCommandGame.spring.getBean("mainQueue", Queue.class);
		mainQueue.addListener(AcquiredItemEvent.class, this);
	}

	@Override
	public void receive(Event event) {
		if(event instanceof AcquiredItemEvent){
			AcquiredItemEvent acq = (AcquiredItemEvent)event;
			if(itemClass.isInstance(acq.item)){
				count += 1;
			}
		}
	}

	@Override
	public boolean isComplete() {
		return count >= countGoal || finished;
	}

	@Override
	public String getOneLineSummary() {
		return String.format("Collect %d %ss (%s)", countGoal, itemClass.toString(), getStatus());
	}

	@Override
	public String getStatus() {
		return String.format("%s/%s", count, countGoal);
	}

	@Override
	public void finish() {
		finished = true;
		mainQueue.removeListener(this);
	}
}
