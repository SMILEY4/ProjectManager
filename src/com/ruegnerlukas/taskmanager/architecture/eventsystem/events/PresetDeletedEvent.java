package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.Preset;

public class PresetDeletedEvent extends Event {


	private Preset preset;




	public PresetDeletedEvent(Preset preset, Object source) {
		super(source);
		this.preset = preset;
	}




	public Preset getPreset() {
		return preset;
	}

}
