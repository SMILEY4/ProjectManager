package com.ruegnerlukas.taskmanager_old.architecture.eventsystem;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener;

import java.util.*;

public class EventManager {


	private static Map<Class<? extends com.ruegnerlukas.taskmanager.architecture.eventsystem.Event>, ArrayList<com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener>> listenerMap = new HashMap<>();
	private static Map<Object, List<com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener>> originMap = new HashMap<>();
	private static Set<com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener> mutedListeners = new HashSet<>();




	@SafeVarargs
	public static void registerListener(Object origin, com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener listener, Class<? extends com.ruegnerlukas.taskmanager.architecture.eventsystem.Event>... events) {
		for (Class<? extends com.ruegnerlukas.taskmanager.architecture.eventsystem.Event> event : events) {
			addListener(listener, event);
			if (!originMap.containsKey(origin)) {
				originMap.put(origin, new ArrayList<>());
			}
			originMap.get(origin).add(listener);
		}
	}




	@SafeVarargs
	public static void registerListener(com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener listener, Class<? extends com.ruegnerlukas.taskmanager.architecture.eventsystem.Event>... events) {
		for (Class<? extends com.ruegnerlukas.taskmanager.architecture.eventsystem.Event> event : events) {
			addListener(listener, event);
		}
	}




	private static void addListener(com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener listener, Class<? extends com.ruegnerlukas.taskmanager.architecture.eventsystem.Event> event) {
		ArrayList<com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener> listeners = listenerMap.get(event);
		if (listeners == null) {
			listeners = new ArrayList<>();
			listenerMap.put(event, listeners);
		}
		listeners.add(listener);
	}




	@SafeVarargs
	public static void deregisterListeners(com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener listener, Class<? extends com.ruegnerlukas.taskmanager.architecture.eventsystem.Event>... events) {
		for (Class<? extends com.ruegnerlukas.taskmanager.architecture.eventsystem.Event> event : events) {
			ArrayList<com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener> listeners = listenerMap.get(event);
			if (listeners != null) {
				listeners.remove(listener);
			}
		}
	}




	public static void deregisterListeners(com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener listener) {
		for (Class<? extends com.ruegnerlukas.taskmanager.architecture.eventsystem.Event> event : listenerMap.keySet()) {
			ArrayList<com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener> listeners = listenerMap.get(event);
			if (listeners != null) {
				listeners.remove(listener);
			}
		}
		unmuteListener(listener);
	}




	public static void deregisterListeners(Object origin) {
		if (originMap.containsKey(origin)) {
			for (com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener listener : originMap.get(origin)) {
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




	public static void muteListener(com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener listener) {
		mutedListeners.add(listener);
	}




	public static void unmuteListeners(Object origin) {
		if (originMap.containsKey(origin)) {
			mutedListeners.removeAll(originMap.get(origin));
		}
	}




	public static void unmuteListener(com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener listener) {
		mutedListeners.remove(listener);
	}




	public static void fireEvent(com.ruegnerlukas.taskmanager.architecture.eventsystem.Event event) {
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





