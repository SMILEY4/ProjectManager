package com.ruegnerlukas.taskmanager_old.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;

public class ChoiceAttributeData implements TaskAttributeData {


	public String[] values = new String[0];
	public boolean useDefault = false;
	public String defaultValue = "";




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.CHOICE;
	}




	@Override
	public boolean usesDefault() {
		return useDefault;
	}




	@Override
	public TextValue getDefault() {
		return new TextValue(defaultValue);
	}




	@Override
	public TaskAttributeValue getValue(Var var) {
		if (var == Var.CHOICE_ATT_VALUES) {
			return new TextArrayValue(values);
		}
		if (var == Var.USE_DEFAULT) {
			return new BoolValue(useDefault);
		}
		if (var == Var.DEFAULT_VALUE) {
			return getDefault();
		}
		return null;
	}




	@Override
	public ChoiceAttributeData copy() {
		ChoiceAttributeData copy = new ChoiceAttributeData();
		copy.values = this.values;
		copy.useDefault = this.useDefault;
		copy.defaultValue = this.defaultValue;
		return copy;
	}

}
