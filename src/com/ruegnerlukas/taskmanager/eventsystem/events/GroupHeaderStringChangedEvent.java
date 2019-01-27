package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;

public class GroupHeaderStringChangedEvent extends Event {

	private boolean useCustomPrev;
	private boolean useCustomNew;


	public GroupHeaderStringChangedEvent(boolean useCustomPrev, boolean useCustomNew, Object source) {
		super(source);
		this.useCustomPrev = useCustomPrev;
		this.useCustomNew = useCustomNew;
	}

	
	
	
	public boolean getUseCustomPrev() {
		return useCustomPrev;
	}


	public boolean getUseCustomNew() {
		return useCustomNew;
	}
	
}
