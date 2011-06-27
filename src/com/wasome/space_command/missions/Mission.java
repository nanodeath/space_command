package com.wasome.space_command.missions;

abstract public class Mission {
	abstract public boolean isComplete();
	abstract public String getOneLineSummary();
	abstract public String getStatus();
	abstract public void finish();
}
