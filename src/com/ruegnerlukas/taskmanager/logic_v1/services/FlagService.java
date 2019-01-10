//package com.ruegnerlukas.taskmanager.logic_v1.services;
//
//import java.util.List;
//
//import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskFlag;
//import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskFlag.FlagColor;
//import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
//import com.ruegnerlukas.taskmanager.eventsystem.events.EventCause;
//import com.ruegnerlukas.taskmanager.eventsystem.events.FlagAddedEvent;
//import com.ruegnerlukas.taskmanager.eventsystem.events.FlagAddedRejection;
//import com.ruegnerlukas.taskmanager.eventsystem.events.FlagChangedColorEvent;
//import com.ruegnerlukas.taskmanager.eventsystem.events.FlagChangedNameEvent;
//import com.ruegnerlukas.taskmanager.eventsystem.events.FlagChangedNameRejection;
//import com.ruegnerlukas.taskmanager.eventsystem.events.FlagRemovedEvent;
//
//public class FlagService {
//
//
//
//
//	public List<TaskFlag> getFlags() {
//		if(DataService.project.getProject() == null) {
//			return null;
//		} else {
//			return DataService.project.getProject().flags;
//		}
//	}
//
//
//
//	public TaskFlag getFlag(String name) {
//		if(DataService.project.getProject() == null) {
//			return null;
//		} else {
//			TaskFlag flag = null;
//			for(TaskFlag f : DataService.project.getProject().flags) {
//				if(f.name.equals(name)) {
//					flag = f;
//					break;
//				}
//			}
//			return flag;
//		}
//	}
//
//
//
//	public TaskFlag getDefaultFlag() {
//		if(DataService.project.getProject() == null) {
//			return null;
//		} else {
//			return DataService.project.getProject().defaultFlag;
//		}
//	}
//
//
//
//	public boolean addNewFlag(String name, FlagColor color) {
//		if(DataService.project.getProject() == null) {
//			return false;
//
//		} else {
//			for(TaskFlag flag : getFlags()) {
//				if(flag.name.equalsIgnoreCase(name)) {
//					EventManager.fireEvent(new FlagAddedRejection(name, EventCause.NAME_EXISTS, this));
//					return false;
//				}
//			}
//			TaskFlag newFlag = new TaskFlag(color, name, false);
//			DataService.project.getProject().flags.add(newFlag);
//			EventManager.fireEvent(new FlagAddedEvent(newFlag, this));
//			return true;
//		}
//	}
//
//
//
//
//	public boolean removeFlag(TaskFlag flag, boolean force) {
//		if(DataService.project.getProject() == null) {
//			return false;
//
//		} else {
//			if(flag == null) {
//				return false;
//			} else {
//				// TODO
//				//   check if flag is in use -> no -> remove flag
//				//    						   yes -> force -> yes -> remove flag
//				//												no -> ask for force-remove
//				DataService.project.getProject().flags.remove(flag);
//				EventManager.fireEvent(new FlagRemovedEvent(flag, this));
//				return true;
//			}
//
//		}
//	}
//
//
//
//
//	public boolean renameFlag(TaskFlag flag, String newName) {
//		if(DataService.project.getProject() == null || flag == null) {
//			return false;
//
//		} else {
//			if(flag.name.equals(newName)) {
//				return true;
//			}
//			for(TaskFlag f : getFlags()) {
//				if(f.name.equalsIgnoreCase(newName)) {
//					EventManager.fireEvent(new FlagChangedNameRejection(flag, flag.name, newName, EventCause.NAME_EXISTS, this));
//					return false;
//				}
//			}
//			flag.name = newName;
//			EventManager.fireEvent(new FlagChangedNameEvent(flag, flag.name, newName, this));
//			return true;
//		}
//	}
//
//
//
//
//	public boolean recolorFlag(TaskFlag flag, FlagColor newColor) {
//		if(DataService.project.getProject() == null || flag == null) {
//			return false;
//
//		} else {
//			FlagColor oldColor = flag.color;
//			if(oldColor != newColor) {
//				flag.color = newColor;
//				EventManager.fireEvent(new FlagChangedColorEvent(flag, oldColor, newColor, this));
//				return true;
//			} else {
//				return false;
//			}
//		}
//	}
//
//
//}
