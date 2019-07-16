package com.ruegnerlukas.tests;

import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.ListChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.ruegnerlukas.taskmanager.data.change.ValueChange;

public class TestUtils {


	public static String checkValueChange(DataChange change, String identifier, Object value) {
		if (change instanceof ValueChange) {
			if (!change.getIdentifier().equals(identifier)) {
				return "Wrong identifier. Expected: \"" + identifier + "\", Actual: \"" + change.getIdentifier() + "\"";
			}
			if (((ValueChange) change).getNewValue() != value) {
				return "Wrong newValue. Expected: \"" + value + "\", Actual: \"" + ((ValueChange) change).getNewValue() + "\"";
			}
			return "OK";
		} else {
			return "Wrong Change-type. Expected: \"" + "ValueChange" + "\", Actual: \"" + (change == null ? "null" : change.getClass().getSimpleName()) + "\"";
		}
	}




	public static String checkNestedChange(DataChange change, String identifier) {
		if (change instanceof NestedChange) {
			if (!change.getIdentifier().equals(identifier)) {
				return "Wrong identifier. Expected: \"" + identifier + "\", Actual: \"" + change.getIdentifier() + "\"";
			}
			return "OK";
		} else {
			return "Wrong Change-type. Expected: \"" + "NestedChange" + "\", Actual: \"" + (change == null ? "null" : change.getClass().getSimpleName()) + "\"";
		}
	}




	public static String checkListChange(DataChange change, boolean wasAdded, Object element, String removedIdentifier) {
		if (change instanceof ListChange) {
			ListChange listChange = (ListChange) change;
			if (listChange.wasAdded() != wasAdded) {
				return "Wrong list operation. Expected: \"" + (wasAdded ? "add" : "remove") + "\", Actual: \"" + (listChange.wasAdded() ? "add" : "remove") + "\"";
			}
			if (listChange.wasAdded()) {
				if (listChange.getAdded() != element) {
					return "Wrong added element. Expected: \"" + element + "\", Actual: \"" + listChange.getAdded() + "\"";
				}
			}
			if (listChange.wasRemoved()) {
				if (listChange.removeByIdentifier()) {
					if (!removedIdentifier.equalsIgnoreCase(listChange.getRemovedIdentifier())) {
						return "Wrong removed identifier. Expected: \"" + removedIdentifier + "\", Actual: \"" + listChange.getRemovedIdentifier() + "\"";
					}
				} else {
					if (listChange.getRemoved() != element) {
						return "Wrong removed element. Expected: \"" + element + "\", Actual: \"" + listChange.getRemoved() + "\"";
					}
				}
			}
			return "OK";
		} else {
			return "Wrong Change-type. Expected: \"" + "ListChange" + "\", Actual: \"" + (change == null ? "null" : change.getClass().getSimpleName()) + "\"";
		}
	}

}