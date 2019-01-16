package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.GroupByOrderChangedEvent;
import com.ruegnerlukas.taskmanager.logic.data.Project;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;

import java.util.List;

public class GroupByLogic {


	public boolean setGroupByOrder(List<TaskAttribute> attribOrder) {
		if(Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			project.groupByOrder.clear();
			project.groupByOrder.addAll(attribOrder);
			EventManager.fireEvent(new GroupByOrderChangedEvent(project.groupByOrder, this));
			return true;
		} else {
			return false;
		}
	}




	public boolean removeGroupByElement(TaskAttribute attribute) {
		if(Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			if(project.groupByOrder.contains(attribute)) {
				EventManager.fireEvent(new GroupByOrderChangedEvent(project.groupByOrder, this));
			}
			return project.groupByOrder.remove(attribute);
		} else {
			return false;
		}
	}

}
