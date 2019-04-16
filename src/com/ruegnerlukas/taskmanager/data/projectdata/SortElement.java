package com.ruegnerlukas.taskmanager.data.projectdata;

public class SortElement {


	public enum SortDir {
		ASC, DESC;
	}






	public final TaskAttribute attribute;
	public final SortDir dir;




	public SortElement(TaskAttribute attribute, SortDir dir) {
		this.attribute = attribute;
		this.dir = dir;
	}


}
