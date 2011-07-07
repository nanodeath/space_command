package com.wasome.space_command;

import java.util.ArrayList;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.wasome.space_command.components.*;
import com.wasome.space_command.data.*;
import com.wasome.space_command.network.*;

public class KryoSupport {
	public static void initializeKryo(Kryo kryo){
		kryo.register(Map.class);
		kryo.register(ArrayList.class);
		kryo.register(ClientState.class);
		kryo.register(ClientUpdate.class);
		kryo.register(ServerMessage.class);
		kryo.register(WorldSync.class);
		kryo.register(byte[].class);
		kryo.register(Engine.Direction.class);
		kryo.register(Point.class);
		kryo.register(SelectiveEntitySync.class);
		kryo.register(TakeControl.class);
	}
}
