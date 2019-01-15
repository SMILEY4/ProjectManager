package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;

public class ChoiceAttributeData implements TaskAttributeData {

	public String[] values = new String[0];
	public boolean useDefault = false;
	public String defaultValue = "";




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.CHOICE;
	}




	@Override
	public boolean update(Var var, Object newValue) {

		switch (var) {

			case CHOICE_ATT_VALUES: {
				if(newValue instanceof String[]) {
					values = (String[])newValue;

					boolean foundDefault = false;
					for(String value : values) {
						if(values.equals(defaultValue)) {
							foundDefault = true;
							break;
						}
					}
					if(!foundDefault) {
						this.defaultValue = values.length==0 ? "" : values[0];
					}

					return true;
				} else {
					return false;
				}
			}

			case USE_DEFAULT: {
				if(newValue instanceof Boolean) {
					useDefault = (Boolean)newValue;
					return true;
				} else {
					return false;
				}
			}

			case DEFAULT_VALUE: {
				if(newValue instanceof String) {
					defaultValue = (String)newValue;
					return true;
				} else {
					return false;
				}
			}

			default: {
				return false;
			}
		}
	}

}
