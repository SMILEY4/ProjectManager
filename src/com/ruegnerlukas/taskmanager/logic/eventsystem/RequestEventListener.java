package com.ruegnerlukas.taskmanager.logic.eventsystem;



public interface RequestEventListener<T> {

	public T onEvent(RequestEvent<T> event);
	
}
