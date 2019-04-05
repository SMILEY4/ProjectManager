package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.Preset;

public class PresetSavedEvent extends Event {


	private Preset preset;




	public PresetSavedEvent(Preset preset, Object source) {
		super(source);
		this.preset = preset;
	}




	public Preset getPreset() {
		return preset;
	}

}
