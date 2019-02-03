package com.ruegnerlukas.taskmanager.architecture.eventsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {


	private static Map<Class<? extends Event>, ArrayList<EventListener>> listenerMap = new HashMap<>();
	private static Map<Object, List<EventListener>> originMap = new HashMap<>();




	@SafeVarargs
	public static void registerListener(Object origin, EventListener listener, Class<? extends Event>... events) {
		for (Class<? extends Event> event : events) {
			addListener(listener, event);
			if (!originMap.containsKey(origin)) {
				originMap.put(origin, new ArrayList<>());
			}
			originMap.get(origin).add(listener);
		}
	}




	@SafeVarargs
	public static void registerListener(EventListener listener, Class<? extends Event>... events) {
		for (Class<? extends Event> event : events) {
			addListener(listener, event);
		}
	}




	private static void addListener(EventListener listener, Class<? extends Event> event) {
		ArrayList<EventListener> listeners = listenerMap.get(event);
		if (listeners == null) {
			listeners = new ArrayList<EventListener>();
			listenerMap.put(event, listeners);
		}
		listeners.add(listener);
	}




	@SafeVarargs
	public static void deregisterListeners(EventListener listener, Class<? extends Event>... events) {
		for (Class<? extends Event> event : events) {
			ArrayList<EventListener> listeners = listenerMap.get(event);
			if (listeners != null) {
				listeners.remove(listener);
			}
		}
	}




	public static void deregisterListeners(EventListener listener) {
		for (Class<? extends Event> event : listenerMap.keySet()) {
			ArrayList<EventListener> listeners = listenerMap.get(event);
			if (listeners != null) {
				listeners.remove(listener);
			}
		}
	}




	public static void deregisterListeners(Object origin) {
		if (originMap.containsKey(origin)) {
			for (EventListener listener : originMap.get(origin)) {
				deregisterListeners(listener);
			}
			originMap.remove(origin);
		}
	}




	public static void fireEvent(Event event) {
		System.out.println("FIRE: " + event);
		ArrayList<EventListener> listeners = listenerMap.get(event.getClass());
		if (listeners != null) {
			for (int i = 0; i < listeners.size(); i++) {
				if (event.isConsumed()) {
					break;
				} else {
					listeners.get(i).onEvent(event);
				}
			}
		}
	}


}





