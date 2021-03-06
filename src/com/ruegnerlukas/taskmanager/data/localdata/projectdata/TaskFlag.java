package com.ruegnerlukas.taskmanager.data.localdata.projectdata;


import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import javafx.beans.property.SimpleStringProperty;
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
		GRAY("#6c757d");

		public final Color color;




		FlagColor(String color) {
			this.color = Color.web(color);
		}




		public String asHex() {
			return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
		}
	}






	public final CustomProperty<FlagColor> color = new CustomProperty<>();
	public final SimpleStringProperty name = new SimpleStringProperty();




	public TaskFlag(String name, FlagColor color) {
		this.color.set(color);
		this.name.set(name);
	}




	public boolean compare(TaskFlag flag) {
		return flag != null && flag.name.get().equals(this.name.get()) && flag.color.get() == this.color.get();
	}




	@Override
	public int hashCode() {
		int result = color.get().hashCode();
		result = 31 * result + name.get().hashCode();
		return result;
	}




	@Override
	public String toString() {
		return "TaskFlag@" + Integer.toHexString(this.hashCode()) + "-" + name;
	}


}
