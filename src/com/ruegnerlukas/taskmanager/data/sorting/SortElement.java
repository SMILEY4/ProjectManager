package com.ruegnerlukas.taskmanager.data.sorting;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

public class SortElement {

	public enum Sort {
		ASC("Ascending"),
		DESC("Descending");
		public final String display;
		Sort(String display) {
			this.display = display;
		}
	}



	public Sort sortDir;
	public TaskAttribute attribute;



	public SortElement(Sort sortDir, TaskAttribute attribute) {
		this.sortDir = sortDir;
		this.attribute = attribute;
	}


}
