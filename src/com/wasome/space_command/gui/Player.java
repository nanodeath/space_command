package com.wasome.space_command.gui;

import static com.wasome.space_command.SpaceCommandGame.getInput;
import static com.wasome.space_command.gui.PlayerInput.*;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Input;

public class Player {
	@SuppressWarnings("serial")
	public class InvalidKeyException extends RuntimeException {
		public InvalidKeyException(NoSuchFieldException e) {
			super(e);
		}
	}
	
	@SuppressWarnings("serial")
	public class KeyNotMappedException extends RuntimeException {
		private PlayerInput input;

		public KeyNotMappedException(PlayerInput input){
			this.input = input;
		}

		@Override
		public String getMessage() {
			return "Key not mapped: " + input.toString();
		}
		
	}

	private Map<PlayerInput, Integer> keyMapping = new HashMap<PlayerInput, Integer>();

	private Integer getKey(String key) {
		try {
			return (Integer) Input.class.getField("KEY_" + key.toUpperCase())
					.get(null);
		} catch (NoSuchFieldException e) {
			throw new InvalidKeyException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setTurnLeftKey(String key) {
		keyMapping.put(TURN_LEFT, getKey(key));
	}

	public void setTurnRightKey(String key) {
		keyMapping.put(TURN_RIGHT, getKey(key));
	}

	public void setAccelerateKey(String key) {
		keyMapping.put(ACCELERATE, getKey(key));
	}
	
	public void setReverseKey(String key) {
		keyMapping.put(REVERSE, getKey(key));
	}

	public void setStrafeLeftKey(String key) {
		keyMapping.put(STRAFE_LEFT, getKey(key));
	}

	public void setStrafeRightKey(String key) {
		keyMapping.put(STRAFE_RIGHT, getKey(key));
	}

	public void setEmergencyStopKey(String key) {
		keyMapping.put(EMERGENCY_STOP, getKey(key));
	}

	public void setShootGunsKey(String key) {
		keyMapping.put(SHOOT_GUNS, getKey(key));
	}

	public void setShowInventoryKey(String key) {
		keyMapping.put(SHOW_INVENTORY, getKey(key));
	}

	public boolean isInputDown(PlayerInput input) {
		return getInput().isKeyDown(keyMapping.get(input));
	}

	public boolean isPressed(PlayerInput input) {
		return getInput().isKeyPressed(keyMapping.get(input));
	}
	
	public void validateAllKeysAssigned(){
		for(PlayerInput input : PlayerInput.values()){
			if(keyMapping.get(input) == null){
				throw new KeyNotMappedException(input);
			}
		}
	}
}
