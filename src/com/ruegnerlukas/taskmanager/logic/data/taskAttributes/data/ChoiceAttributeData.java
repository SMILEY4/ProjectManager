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
	public Var[] update(Var var, Object newValue) {

		switch (var) {

			case CHOICE_ATT_VALUES: {
				if(newValue instanceof String[]) {
					values = (String[])newValue;

					boolean foundDefault = false;
					for(String value : values) {
						if(value.equals(defaultValue)) {
							foundDefault = true;
							break;
						}
					}
					if(!foundDefault) {
						this.defaultValue = values.length==0 ? "" : values[0];
						return new Var[] {Var.CHOICE_ATT_VALUES, Var.DEFAULT_VALUE};
					} else {
						return new Var[] {Var.CHOICE_ATT_VALUES};
					}

				} else {
					return null;
				}
			}

			case USE_DEFAULT: {
				if(newValue instanceof Boolean) {
					useDefault = (Boolean)newValue;
					return new Var[] {Var.USE_DEFAULT};
				} else {
					return null;
				}
			}

			case DEFAULT_VALUE: {
				if(newValue instanceof String) {
					defaultValue = (String)newValue;
					return new Var[] {Var.DEFAULT_VALUE};
				} else {
					return null;
				}
			}

			default: {
				return null;
			}
		}
	}

}
