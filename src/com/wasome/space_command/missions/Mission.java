package com.wasome.space_command.missions;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

abstract public class Mission implements ApplicationContextAware {
	protected ApplicationContext spring;

	abstract public boolean isComplete();
	abstract public String getOneLineSummary();
	abstract public String getStatus();
	abstract public void finish();
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		spring = applicationContext;
	}
}
