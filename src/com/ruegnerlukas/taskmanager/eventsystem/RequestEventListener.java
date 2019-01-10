package com.ruegnerlukas.taskmanager.eventsystem;



public interface RequestEventListener<T> {

	public T onEvent(RequestEvent<T> event);
	
}
