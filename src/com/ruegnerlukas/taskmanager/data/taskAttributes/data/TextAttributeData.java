package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;

public class TextAttributeData implements TaskAttributeData {


	public int charLimit = 64;
	public boolean multiline = false;
	public int nLinesExpected = 2;
	public boolean useDefault = false;
	public String defaultValue = "";




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.TEXT;
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
		if (var == Var.TEXT_CHAR_LIMIT) {
			return new NumberValue(charLimit);
		}
		if (var == Var.TEXT_MULTILINE) {
			return new BoolValue(multiline);
		}
		if (var == Var.TEXT_N_LINES_EXP) {
			return new NumberValue(nLinesExpected);
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
	public TextAttributeData copy() {
		TextAttributeData copy = new TextAttributeData();
		copy.charLimit = this.charLimit;
		copy.multiline = this.multiline;
		copy.nLinesExpected = this.nLinesExpected;
		copy.useDefault = this.useDefault;
		copy.defaultValue = this.defaultValue;
		return copy;
	}


}
