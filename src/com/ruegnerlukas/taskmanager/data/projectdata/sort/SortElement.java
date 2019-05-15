package com.ruegnerlukas.taskmanager.data.projectdata.sort;

import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;

public class SortElement {


	public enum SortDir {
		ASC, DESC
	}






	public final CustomProperty<TaskAttribute> attribute = new CustomProperty<>();
	public final CustomProperty<SortDir> dir = new CustomProperty<>();




	public SortElement(TaskAttribute attribute, SortDir dir) {
		this.attribute.set(attribute);
		this.dir.set(dir);
	}


}
