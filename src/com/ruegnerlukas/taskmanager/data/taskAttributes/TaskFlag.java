package com.ruegnerlukas.taskmanager.data.taskAttributes;

import javafx.scene.paint.Color;

public class TaskFlag {


	public enum FlagColor {

		BLUE("#007bff"),
		INDIGO("6610f2"),
		PURPLE("#6f42c1"),
		PINK("#e83e8c"),
		RED("#dc3545"),
		ORANGE("#fd7e14"),
		YELLOW("#ffc107"),
		GREEN("#28a745"),
		TEAL("#20c997"),
		CYAN("#17a2b8"),
		GRAY("#6c757d"),
		;

		public final Color color;




		FlagColor(String color) {
			this.color = Color.web(color);
		}




		public String asHex() {
			return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
		}
	}






	public FlagColor color;
	public String name;




	public TaskFlag(FlagColor color, String name) {
		this.color = color;
		this.name = name;
	}




	public static TaskFlag findFlag(String name, TaskFlag... flags) {
		for (TaskFlag flag : flags) {
			if (flag.name.equals(name)) {
				return flag;
			}
		}
		return null;
	}




	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TaskFlag) {
			TaskFlag other = (TaskFlag) obj;
			if (!other.name.equals(this.name)) {
				return false;
			}
			if (other.color != this.color) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}




	@Override
	public String toString() {
		return "TaskFlag@" + Integer.toHexString(this.hashCode()) + ":(" + this.name + ", " + this.color.toString() + ")";
	}

}
