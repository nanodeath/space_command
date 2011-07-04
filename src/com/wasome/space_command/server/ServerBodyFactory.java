package com.wasome.space_command.server;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Shape;
import org.springframework.beans.factory.annotation.Autowired;

import com.wasome.space_command.Entity;
import com.wasome.space_command.GameServer;

public class ServerBodyFactory implements BodyFactory {
	private Map<Body<?>, Entity> bodyToEntityMap = new ConcurrentHashMap<Body<?>, Entity>();
	private Timer sweeper;

	private final Enhancer dynamicBodyEnhancer;
	@Autowired
	private GameServer game;

	{
		dynamicBodyEnhancer = new Enhancer();
		dynamicBodyEnhancer.setSuperclass(DynamicBody.class);
		dynamicBodyEnhancer.setInterfaces(new Class[] { HasEntity.class });
		dynamicBodyEnhancer.setCallback(new MethodInterceptor() {
			@Override
			public Object intercept(Object enhancedObject, Method method, Object[] argumentArray, MethodProxy methodProxy) throws Throwable {
				String name = method.getName();
				if (name.equals("getEntity")) {
					return bodyToEntityMap.get(enhancedObject);
				}

				// checking for side effects
				if (name.startsWith("apply") || name.startsWith("set")) {
					game.updateEntityOnClients(bodyToEntityMap.get(enhancedObject));
				}
				return methodProxy.invokeSuper(enhancedObject, argumentArray);
			}
		});

		/**
		 * Even if we don't end up doing the sweeper quite like this, it's a
		 * reminder that we need to do _something_ to clear up this map so it
		 * doesn't just get perpetually larger!
		 */
		sweeper = new Timer("BodyFactory Sweeper", true);
		sweeper.schedule(new TimerTask() {

			@Override
			public void run() {
				Iterator<Map.Entry<Body<?>, Entity>> it = bodyToEntityMap.entrySet().iterator();
				synchronized (bodyToEntityMap) {
					while (it.hasNext()) {
						Map.Entry<Body<?>, Entity> entry = it.next();
						// TODO put a better check in here for whether we can
						// remove something
						if (!entry.getKey().isActive()) {
							it.remove();
						}
					}
				}
			}
		}, 5 * 60 * 1000, 5 * 60 * 1000);
	}

	@SuppressWarnings("unchecked")
	public <T> DynamicBody<T> createDynamicBody(Entity entity, Shape shape, float x, float y) {
		DynamicBody<T> body = (DynamicBody<T>) dynamicBodyEnhancer.create(new Class[] { Shape.class, float.class, float.class }, new Object[] { shape, x, y });
		bodyToEntityMap.put(body, entity);
		return body;
	}
}
