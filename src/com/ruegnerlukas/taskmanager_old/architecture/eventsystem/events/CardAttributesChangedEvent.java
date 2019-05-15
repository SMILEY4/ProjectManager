package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

import java.util.List;

public class CardAttributesChangedEvent extends Event {


	private List<TaskAttribute> cardAttributes;




	public CardAttributesChangedEvent(List<TaskAttribute> cardAttributes, Object source) {
		super(source);
		this.cardAttributes = cardAttributes;
	}




	public List<TaskAttribute> getCardAttributes() {
		return this.cardAttributes;
	}


}
