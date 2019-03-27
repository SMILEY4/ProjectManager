package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;

public class AppearanceLockEvent extends Event {


	private boolean lockPrev;
	private boolean lockNow;




	public AppearanceLockEvent(boolean lockPrev, boolean lockNow, Object source) {
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
