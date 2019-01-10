package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;

public class AttributeLockEvent extends Event {


	private boolean lockPrev;
	private boolean lockNow;





	public AttributeLockEvent(boolean lockPrev, boolean lockNow, Object source) {
		super(source);
		this.lockPrev = lockPrev;
		this.lockNow = lockNow;
	}




	public boolean getLockPrev() {
		return lockPrev;
	}




	public boolean getLockNow() {
		return lockNow;
	}

}
