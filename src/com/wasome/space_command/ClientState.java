package com.wasome.space_command;

import javax.annotation.Resource;

import org.newdawn.slick.Input;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.wasome.space_command.player.Player;
import com.wasome.space_command.player.PlayerInput;

@Component
@Scope("prototype")
public class ClientState implements ApplicationContextAware {
	public boolean isAccelerating;

	@Resource(name = "player1")
	private transient Player player1;
	
	public int playerId = 1;

	public long receivedAt;

	private transient ApplicationContext spring;

	public ClientState dupe() {
		ClientState cs = spring.getBean(ClientState.class);
		cs.isAccelerating = isAccelerating;
		return cs;
	}

	public void updateInput(Input input) {
		isAccelerating = player1.isInputDown(PlayerInput.ACCELERATE);
	}

	public boolean equivalentTo(ClientState newState) {
		return isAccelerating == newState.isAccelerating;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		spring = applicationContext;
	}

}
