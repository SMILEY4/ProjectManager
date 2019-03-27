package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

public class CardAttributeDisplayTypeChangedEvent extends Event {


	private TaskAttribute cardAttribute;
	private String displayType;




	public CardAttributeDisplayTypeChangedEvent(TaskAttribute cardAttribute, String displayType, Object source) {
		super(source);
		this.cardAttribute = cardAttribute;
		this.displayType = displayType;
	}




	public TaskAttribute getCardAttribute() {
		return cardAttribute;
	}




	public String getDisplayType() {
		return displayType;
	}


}
