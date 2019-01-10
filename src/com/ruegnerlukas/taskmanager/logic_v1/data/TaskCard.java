package com.ruegnerlukas.taskmanager.logic_v1.data;

import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskFlag;

import java.util.ArrayList;
import java.util.List;

public class TaskCard {

	public final int id;
	public TaskList parentList = null;
	
	public TaskFlag flag;
	public String text = "notext";
	public List<String> tags = new ArrayList<String>();
	
	
	
	public TaskCard(int id, TaskFlag flag) {
		this.id = id;
		this.flag = flag;
	}
	
}
