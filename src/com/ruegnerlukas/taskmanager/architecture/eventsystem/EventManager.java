package com.ruegnerlukas.taskmanager.architecture.eventsystem;

import java.util.*;

public class EventManager {


	private static Map<Class<? extends Event>, ArrayList<EventListener>> listenerMap = new HashMap<>();
	private static Map<Object, List<EventListener>> originMap = new HashMap<>();
	private static Set<EventListener> mutedListeners = new HashSet<>();




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
			listeners = new ArrayList<>();
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
		unmuteListener(listener);
	}




	public static void deregisterListeners(Object origin) {
		if (originMap.containsKey(origin)) {
			for (EventListener listener : originMap.get(origin)) {
				deregisterListeners(listener);
			}
			originMap.remove(origin);
			unmuteListeners(origin);
		}
	}


//	private static Map<Class<? extends Event>, ArrayList<EventListener>> listenerMap = new HashMap<>();
//	private static Map<Object, List<EventListener>> originMap = new HashMap<>();
//	private static Set<EventListener> mutedListeners = new HashSet<>();




	public static void muteListeners(Object origin) {
		if (originMap.containsKey(origin)) {
			mutedListeners.addAll(originMap.get(origin));
		}
	}




	public static void muteListener(EventListener listener) {
		mutedListeners.add(listener);
	}




	public static void unmuteListeners(Object origin) {
		if (originMap.containsKey(origin)) {
			mutedListeners.removeAll(originMap.get(origin));
		}
	}




	public static void unmuteListener(EventListener listener) {
		mutedListeners.remove(listener);
	}




	public static void fireEvent(Event event) {
		System.out.println("FIRE: " + event);
		ArrayList<EventListener> listeners = listenerMap.get(event.getClass());
		if (listeners != null) {
			for (int i = 0; i < listeners.size(); i++) {
				if (!mutedListeners.contains(listeners.get(i))) {
					if (event.isConsumed()) {
						break;
					} else {
						listeners.get(i).onEvent(event);
					}
				}
			}
		}
	}


}





