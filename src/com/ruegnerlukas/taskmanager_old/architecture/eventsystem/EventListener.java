package com.ruegnerlukas.taskmanager_old.architecture.eventsystem;


import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;

public interface EventListener {

	void onEvent(Event event);
	
}
