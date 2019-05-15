package com.ruegnerlukas.taskmanager_old.architecture.eventsystem;



public abstract class Event {

	
	private final Object source;
	private boolean consumed = false;
	
	
	
	
	public Event(Object source) {
		this.source = source;
	}
	
	
	
	
	
	
	public Object getSource() {
		return source;
	}
	
	
	
	
	protected boolean isConsumed() {
		return consumed;
	}
	
	
	
	
	public void consume() {
		consumed = true;
	}
	
	
}
