package com.ruegnerlukas.taskmanager.data.projectdata.attributevalues;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ChoiceListValue extends AttributeValue<String[]> {


	public ChoiceListValue(String... choiceList) {
		super(choiceList, AttributeValueType.CHOICE_VALUES);
	}




	@Override
	public int compare(AttributeValue<?> other) {
		if (other == null) {
			return 1;
		} else {
			if (other.getType() != this.getType()) {
				return Integer.compare(this.getType().ordinal(), other.getType().ordinal());
			} else {
				Set<String> setT = new HashSet<>(Arrays.asList(this.getValue()));
				Set<String> setO = new HashSet<>(Arrays.asList(((ChoiceListValue) other).getValue()));
				if (setT.containsAll(setO) && setO.containsAll(setT)) {
					return 0;
				} else {
					return Integer.compare(setT.size(), setO.size());
				}
			}
		}
	}

}
