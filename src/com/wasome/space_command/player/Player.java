package com.wasome.space_command.player;

import static com.wasome.space_command.player.PlayerInput.ACCELERATE;
import static com.wasome.space_command.player.PlayerInput.EMERGENCY_STOP;
import static com.wasome.space_command.player.PlayerInput.REVERSE;
import static com.wasome.space_command.player.PlayerInput.SHOOT_GUNS;
import static com.wasome.space_command.player.PlayerInput.SHOW_INVENTORY;
import static com.wasome.space_command.player.PlayerInput.STRAFE_LEFT;
import static com.wasome.space_command.player.PlayerInput.STRAFE_RIGHT;
import static com.wasome.space_command.player.PlayerInput.TURN_LEFT;
import static com.wasome.space_command.player.PlayerInput.TURN_RIGHT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.wasome.space_command.Entity;
import com.wasome.space_command.behavior.Visible;
import com.wasome.space_command.missions.Mission;

@Visible
public class Player extends Entity {
	private List<Mission> missions = new ArrayList<Mission>();
	private int primaryMissionIdx = -1;
	
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
		return game.getInput().isKeyDown(keyMapping.get(input));
	}

	public boolean isPressed(PlayerInput input) {
		return game.getInput().isKeyPressed(keyMapping.get(input));
	}
	
	public void validateAllKeysAssigned(){
		for(PlayerInput input : PlayerInput.values()){
			if(keyMapping.get(input) == null){
				throw new KeyNotMappedException(input);
			}
		}
	}
	
	public void giveMission(Mission mission){
		missions.add(mission);
		if(primaryMissionIdx < 0){
			primaryMissionIdx = missions.size() - 1;
		}
	}
	
	@Override
	public void render() {
		float x = 100f;
		float y = 100f;
		float lineSpacing = 50f;
		Graphics g = game.getGraphics();
//		g.setFont(Game.FONT);
		g.setColor(Color.white);
		for(int i = 0; i < missions.size(); i++){
			Mission mission = missions.get(i);
			String display;
			if(mission.isComplete()){
				display = "☑";
			} else {
				display = "☐";
			}
			display += " " + mission.getOneLineSummary();
			
			g.drawString(display, x, y + lineSpacing * i);
		}
	}
}
