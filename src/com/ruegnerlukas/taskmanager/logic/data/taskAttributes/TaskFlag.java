package com.ruegnerlukas.taskmanager.logic.data.taskAttributes;

import javafx.scene.paint.Color;

public class TaskFlag {

	public static enum FlagColor {

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
		
		private FlagColor(Color color) {
			this.color = color;
		}
		
		private FlagColor(String color) {
			this.color = Color.web(color);
		}
	}
	
	
	
	public final boolean isDefaultFlag;
	public FlagColor color;
	public String name;
	
	
	public TaskFlag(FlagColor color, String name, boolean isDefaultFlag) {
		this.color = color;
		this.name = name;
		this.isDefaultFlag = isDefaultFlag;
	}
	
}
