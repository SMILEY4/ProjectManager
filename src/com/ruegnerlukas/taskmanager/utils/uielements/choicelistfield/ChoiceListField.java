package com.ruegnerlukas.taskmanager.utils.uielements.choicelistfield;

import com.ruegnerlukas.taskmanager.utils.uielements.textfield.AutocompletionTextField;

import java.util.*;

public class ChoiceListField extends AutocompletionTextField {


	private Set<String> choices;
	private String delimiter;




	public ChoiceListField(Set<String> choices, String delimiter) {
		super(choices, true, delimiter);
		this.delimiter = delimiter;
		this.choices = choices;
	}




	public List<String> getChoices() {

		String string = this.getText();
		String[] strSplit = string.split(delimiter);

		List<String> list = new ArrayList<>();

		for (String str : strSplit) {
			String word = str.trim();
			if (choices.contains(word)) {
				list.add(word);
			}
		}

		return list;
	}




	public List<String> getUnqiueChoices() {
		return new ArrayList<>(new HashSet<>(getChoices()));
	}


}
