package com.ruegnerlukas.taskmanager.eventsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

	
	private static Map< Class<? extends Event>, ArrayList<EventListener> > listenerMap = new HashMap<>();
	private static Map< Class<? extends RequestEvent<?>>, ArrayList<RequestEventListener<?>> > reqListenerMap = new HashMap<>();
	private static Map<Object, List<EventListener>> originMap = new HashMap<>();



	@SafeVarargs
	public static void registerListener(Object origin, EventListener listener, Class<? extends Event>... events) {
		for(Class<? extends Event> event : events) {
			ArrayList<EventListener> listeners = listenerMap.get(event);
			if(listeners == null) {
				listeners = new ArrayList<EventListener>();
				listenerMap.put(event, listeners);
			}
			listeners.add(listener);
			if(!originMap.containsKey(origin)) {
				originMap.put(origin, new ArrayList<>());
			}
			originMap.get(origin).add(listener);
		}
	}


	
	@SafeVarargs
	public static void registerListener(EventListener listener, Class<? extends Event>... events) {
		for(Class<? extends Event> event : events) {
			ArrayList<EventListener> listeners = listenerMap.get(event);
			if(listeners == null) {
				listeners = new ArrayList<EventListener>();
				listenerMap.put(event, listeners);
			}
			listeners.add(listener);
		}
	}
	
	
	
	
	@SafeVarargs
	public static void registerListener(RequestEventListener<?> listener, Class<? extends RequestEvent<?>>... events) {
		for(Class<? extends RequestEvent<?>> event : events) {
			ArrayList<RequestEventListener<?>> listeners = reqListenerMap.get(event);
			if(listeners == null) {
				listeners = new ArrayList<RequestEventListener<?>>();
				reqListenerMap.put(event, listeners);
			}
			listeners.add(listener);
		}
	}
	
	
	
	
	@SafeVarargs
	public static void deregisterListeners(EventListener listener, Class<? extends Event>... events) {
		for(Class<? extends Event> event : events) {
			ArrayList<EventListener> listeners = listenerMap.get(event);
			if(listeners != null) {
				listeners.remove(listener);
			}
		}
	}
	
	
	
	
	@SafeVarargs
	public static void deregisterListeners(RequestEventListener<?> listener, Class<? extends RequestEvent<?>>... events) {
		for(Class<? extends RequestEvent<?>> event : events) {
			ArrayList<RequestEventListener<?>> listeners = reqListenerMap.get(event);
			if(listeners != null) {
				listeners.remove(listener);
			}
		}
	}
	
	
	
	
	public static void deregisterListeners(EventListener listener) {
		for(Class<? extends Event> event : listenerMap.keySet()) {
			ArrayList<EventListener> listeners = listenerMap.get(event);
			if(listeners != null) {
				listeners.remove(listener);
			}
		}
	}
	
	
	
	
	public static void deregisterListeners(RequestEventListener<?> listener) {
		for(Class<? extends RequestEvent<?>> event : reqListenerMap.keySet()) {
			ArrayList<RequestEventListener<?>> listeners = reqListenerMap.get(event);
			if(listeners != null) {
				listeners.remove(listener);
			}
		}
	}




	public static void deregisterListeners(Object origin) {
		if(originMap.containsKey(origin)) {
			for(EventListener listener : originMap.get(origin)) {
				deregisterListeners(listener);
			}
			originMap.remove(origin);
		}
	}




	public static void fireEvent(Event event) {
		System.out.println("FIRE: " + event);
		ArrayList<EventListener> listeners = listenerMap.get(event.getClass());
		if(listeners != null) {
			for(int i=0; i<listeners.size(); i++) {
				if(event.isConsumed()) {
					break;
				} else {
					listeners.get(i).onEvent(event);
				}
			}
		}
	}
	
	
	
	
	public static <T> List<T> fireRequestEvent(RequestEvent<T> event) {
		List<T> values = new ArrayList<T>();
		ArrayList<RequestEventListener<?>> listeners = reqListenerMap.get(event.getClass());
		if(listeners != null) {
			for(int i=0; i<listeners.size(); i++) {
				if(event.isConsumed()) {
					break;
				} else {
					RequestEventListener<T> listener = (RequestEventListener<T>) listeners.get(i);
					T value = listener.onEvent(event);
					if(value != null) {
						values.add(value);
					}
				}
			}
		}
		return values;
	}
	
	
}





