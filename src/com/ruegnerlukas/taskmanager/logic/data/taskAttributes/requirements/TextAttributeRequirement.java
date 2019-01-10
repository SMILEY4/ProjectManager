package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.requirements;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;

public class TextAttributeRequirement implements TaskAttributeRequirement {

	public int charLimit = 64;
	public boolean multiline = false;
	public boolean useDefault = false;
	public String defaultValue = "";
	
	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.TEXT;
	}




	@Override
	public boolean update(TaskAttributeRequirement data) {
		if(data instanceof TextAttributeRequirement) {
			TextAttributeRequirement updatedData = (TextAttributeRequirement)data;

			// validate new data
			if(updatedData.charLimit <= 0) {
				return false;
			}
			if(updatedData.defaultValue.length() > updatedData.charLimit) {
				return false;
			}

			// set new data
			this.charLimit = updatedData.charLimit;
			this.multiline = updatedData.multiline;
			this.useDefault = updatedData.useDefault;
			this.defaultValue = updatedData.defaultValue;
			return true;

		} else {
			return false;
		}
	}




	@Override
	public TaskAttributeRequirement copy() {
		TextAttributeRequirement copy = new TextAttributeRequirement();
		copy.charLimit = charLimit;
		copy.multiline = multiline;
		copy.useDefault = useDefault;
		copy.defaultValue = defaultValue;
		return copy;
	}

}
