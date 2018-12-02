package com.ruegnerlukas.taskmanager.logic.services;

import com.ruegnerlukas.taskmanager.logic.data.TaskCard;
import com.ruegnerlukas.taskmanager.logic.data.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.data.TaskList;
import com.ruegnerlukas.taskmanager.logic.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.TaskChangedFlagEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.TaskChangedTextEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.TaskCreatedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.TaskDeletedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.TaskMovedEvent;

public class TaskService {

	
	
	
	public boolean createTask(TaskList list) {
		if(list != null) {
			TaskCard card = new TaskCard(DataService.id.generatTaskID(), DataService.flags.getDefaultFlag());
			card.parentList = list;
			list.cards.add(card);
			DataService.project.getProject().taskIdCounter += 1;
			EventManager.fireEvent(new TaskCreatedEvent(list, card, this));
			return true;
		} else {
			return false;
		}
	}
	
	
	
	
	public boolean deleteTask(TaskCard task) {
		return deleteTask(task, false);
	}
	
	protected boolean deleteTask(TaskCard task, boolean force) {
		if(task != null && task.parentList != null) {
			TaskList list = task.parentList;
			task.parentList.cards.remove(task);
			task.parentList = null;
			EventManager.fireEvent(new TaskDeletedEvent(list, task, this));
			return true;
		} else {
			return false;
		}
	}
	
	
	
	
	public boolean moveTask(TaskCard task, TaskList dest) {
		if(task != null && dest != null) {
			if(task.parentList != dest) {
				TaskList oldList = task.parentList;
				task.parentList = dest;
				oldList.cards.remove(task);
				dest.cards.add(task);
				EventManager.fireEvent(new TaskMovedEvent(task, oldList, dest, this));
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	
	
	public boolean moveAllTasks(TaskList list, TaskList dest) {
		if(list != null) {
			if(list != dest) {
				for(TaskCard task : list.cards) {
					task.parentList = dest;
					dest.cards.add(task);
					EventManager.fireEvent(new TaskMovedEvent(task, list, dest, this));
				}
				list.cards.clear();
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	
	
	public boolean setFlag(TaskCard task, TaskFlag newFlag) {
		if(task != null && newFlag != null) {
			if(task.flag != newFlag) {
				TaskFlag oldFlag = task.flag;
				task.flag = newFlag;
				EventManager.fireEvent(new TaskChangedFlagEvent(task, oldFlag, newFlag, this));
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	
	
	public boolean setText(TaskCard task, String newText) {
		if(task != null) {
			if(task.text.equals(newText)) {
				return false;
			} else {
				String oldText = task.text;
				task.text = newText;
				EventManager.fireEvent(new TaskChangedTextEvent(task, oldText, newText, this));
				return true;
			}
		} else {
			return false;
		}
	}
	
}

