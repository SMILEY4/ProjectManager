package com.ruegnerlukas.taskmanager.data.taskAttributes.values;

import com.ruegnerlukas.simplemath.MathUtils;

import java.util.Arrays;
import java.util.Collection;

public class TextArrayValue implements TaskAttributeValue {


	private String[] text;




	public TextArrayValue(String... text) {
		this.text = text;
	}




	public TextArrayValue(Collection<String> text) {
		this.text = new String[text.size()];
		int i = 0;
		for (String str : text) {
			this.text[i++] = str;
		}
	}




	public String[] getText() {
		return text;
	}




	@Override
	public Object getValue() {
		return text;
	}




	@Override
	public String toString() {
		return Arrays.toString(text);
	}




	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TextArrayValue that = (TextArrayValue) o;

		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		return Arrays.equals(text, that.text);
	}




	@Override
	public int hashCode() {
		return Arrays.hashCode(text);
	}




	@Override
	public int compareTo(TaskAttributeValue o) {
		if (o instanceof TextArrayValue) {
			final String[] oValue = ((TextArrayValue) o).getText();

			for (int i = 0; i < Math.min(text.length, oValue.length); i++) {
				String tt = text[i];
				String ot = oValue[i];
				int cmp = tt.compareTo(ot);
				if (cmp != 0) {
					return MathUtils.clamp(cmp, -1, +1);
				}
			}

			return MathUtils.clamp(text.length - oValue.length, -1, -2);

		} else {
			return -2;
		}
	}

}
