//package com.ruegnerlukas.taskmanager.logic_v1.services;
//
//import com.ruegnerlukas.simplemath.MathUtils;
//import com.ruegnerlukas.taskmanager.logic_v1.data.TaskCard;
//import com.ruegnerlukas.taskmanager.logic_v1.data.TaskList;
//import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
//import com.ruegnerlukas.taskmanager.eventsystem.events.*;
//import javafx.scene.paint.Color;
//
//import java.util.*;
//
//public class ListService {
//
//
//	public static enum ListDeleteBehavior {
//		ARCHIVE_TASKS,
//		DELETE_TASKS,
//		CANCEL
//	}
//
//
//
//	public boolean createList(String name, int index) {
//		if(DataService.project.getProject() == null) {
//			return false;
//
//		} else {
//			TaskList list = new TaskList(DataService.id.generateListID());
//			list.title = name;
//			int indexList = MathUtils.clamp(index, 0, DataService.project.getProject().lists.size());
//			DataService.project.getProject().lists.add(list);
//			DataService.project.getProject().listOrder.add(indexList, list.id);
//			DataService.project.getProject().listIdCounter  += 1;
//			EventManager.fireEvent(new ListCreatedEvent(list, indexList, this));
//			return true;
//		}
//	}
//
//
//
//
//	public TaskList getListByID(int id) {
//		if(DataService.project.getProject() == null) {
//			return null;
//
//		} else {
//			for(int i=0; i<DataService.project.getProject().lists.size(); i++) {
//				TaskList list = DataService.project.getProject().lists.get(i);
//				if(list.id == id) {
//					return list;
//				}
//			}
//			return null;
//		}
//	}
//
//
//
//
//	public int getNumLists() {
//		if(DataService.project.getProject() == null) {
//			return 0;
//		} else {
//			return DataService.project.getProject().lists.size();
//		}
//	}
//
//
//
//
//	public List<String> getListTitles() {
//		if(DataService.project.getProject() == null) {
//			return Collections.unmodifiableList(new ArrayList<String>(0));
//		} else {
//			List<String> titles = new ArrayList<String>(DataService.project.getProject().lists.size());
//			for(int i=0; i<DataService.project.getProject().lists.size(); i++) {
//				TaskList list = DataService.project.getProject().lists.get(i);
//				titles.add(list.title);
//			}
//			return Collections.unmodifiableList(titles);
//		}
//	}
//
//
//
//
//	public List<TaskList> getLists() {
//		if(DataService.project.getProject() == null) {
//			return new ArrayList<TaskList>(0);
//		} else {
//			return DataService.project.getProject().lists;
//		}
//	}
//
//
//
//
//	public int[] getListIDs() {
//		if(DataService.project.getProject() == null) {
//			return new int[] {};
//		} else {
//			int[] ids = new int[DataService.project.getProject().lists.size()];
//			for(int i=0; i<DataService.project.getProject().lists.size(); i++) {
//				TaskList list = DataService.project.getProject().lists.get(i);
//				ids[i] = list.id;
//			}
//			return ids;
//		}
//	}
//
//
//
//
//	public List<Integer> getListOrder() {
//		if(DataService.project.getProject() == null) {
//			return Collections.unmodifiableList(new ArrayList<Integer>(0));
//		} else {
//			return Collections.unmodifiableList(DataService.project.getProject().listOrder);
//		}
//	}
//
//
//
//
//	public boolean moveListFirst(TaskList list) {
//		int idThis = list.id;
//		List<Integer> oldOrder = DataService.lists.getListOrder();
//		List<Integer> newOrder = new ArrayList<Integer>();
//		newOrder.add(idThis);
//		for(int i=0; i<oldOrder.size(); i++) {
//			int idCurrent = oldOrder.get(i);
//			if(idCurrent == idThis) {
//				continue;
//			} else {
//				newOrder.add(idCurrent);
//			}
//		}
//		return DataService.lists.setListOrder(newOrder, list);
//	}
//
//
//
//
//	public boolean moveListLast(TaskList list) {
//		int idThis = list.id;
//		List<Integer> oldOrder = DataService.lists.getListOrder();
//		List<Integer> newOrder = new ArrayList<Integer>();
//		for(int i=0; i<oldOrder.size(); i++) {
//			int idCurrent = oldOrder.get(i);
//			if(idCurrent == idThis) {
//				continue;
//			} else {
//				newOrder.add(idCurrent);
//			}
//		}
//		newOrder.add(idThis);
//		return DataService.lists.setListOrder(newOrder, list);
//	}
//
//
//
//
//	public boolean moveListBefore(TaskList list, TaskList target) {
//		int idThis = list.id;
//		int idTarget = target.id;
//
//		List<Integer> oldOrder = DataService.lists.getListOrder();
//		List<Integer> newOrder = new ArrayList<Integer>();
//
//		for(int i=0; i<oldOrder.size(); i++) {
//			int idCurrent = oldOrder.get(i);
//			if(idCurrent == idThis) {
//				continue;
//			} else if(idCurrent == idTarget) {
//				newOrder.add(idThis);
//				newOrder.add(idCurrent);
//			} else {
//				newOrder.add(idCurrent);
//			}
//		}
//
//		return DataService.lists.setListOrder(newOrder, list);
//	}
//
//
//
//
//	public boolean moveListAfter(TaskList list, TaskList target) {
//
//		int idThis = list.id;
//		int idTarget = target.id;
//
//		List<Integer> oldOrder = DataService.lists.getListOrder();
//		List<Integer> newOrder = new ArrayList<Integer>();
//
//		for(int i=0; i<oldOrder.size(); i++) {
//			int idCurrent = oldOrder.get(i);
//			if(idCurrent == idThis) {
//				continue;
//			} else if(idCurrent == idTarget) {
//				newOrder.add(idCurrent);
//				newOrder.add(idThis);
//			} else {
//				newOrder.add(idCurrent);
//			}
//		}
//
//		return DataService.lists.setListOrder(newOrder, list);
//	}
//
//
//
//
//	public boolean setListOrder(List<Integer> listOrder) {
//		return setListOrder(listOrder, null);
//	}
//
//
//	private boolean setListOrder(List<Integer> listOrder, TaskList movedList) {
//		if(DataService.project.getProject() == null) {
//			return false;
//		} else {
//
//			// validate
//			Set<Integer> set = new HashSet<Integer>();
//			set.addAll(listOrder);
//			if(set.size() != getListIDs().length) {
//				return false;
//			}
//			for(int i=0; i<listOrder.size(); i++) {
//				int id = listOrder.get(i);
//				if(getListByID(id) == null) {
//					return false;
//				}
//			}
//
//			// set
//			List<Integer> oldOrder = new ArrayList<Integer>(DataService.project.getProject().listOrder);
//			DataService.project.getProject().listOrder.clear();
//			DataService.project.getProject().listOrder.addAll(listOrder);
//			List<Integer> newOrder = new ArrayList<Integer>(DataService.project.getProject().listOrder);
//
//			if(movedList == null) {
//				EventManager.fireEvent(new ListsChangedOrderEvent(oldOrder, newOrder, this));
//			} else {
//				EventManager.fireEvent(new ListMovedEvent(movedList, oldOrder, newOrder, this));
//			}
//			return true;
//		}
//	}
//
//
//
//
//	public boolean deleteList(TaskList list) {
//		return deleteList(list, false);
//	}
//
//	public boolean deleteList(TaskList list, boolean force) {
//		if(list != null) {
//			if(force || list.cards.isEmpty()) {
//				for(TaskCard task : list.cards) {
//					DataService.tasks.deleteTask(task);
//				}
//				DataService.project.getProject().lists.remove(list);
//				DataService.project.getProject().listOrder.remove(new Integer(list.id));
//				EventManager.fireEvent(new ListDeletedEvent(list, this));
//				return true;
//
//			} else {
//				List<ListDeleteBehavior> values = EventManager.fireRequestEvent(new ListDeleteRequest(list, this));
//				if(!values.isEmpty()) {
//					ListDeleteBehavior behaviour = values.get(0);
//					if(behaviour == ListDeleteBehavior.CANCEL) {
//						System.out.println("CANCEL");
//						// do nothing
//					}
//					if(behaviour == ListDeleteBehavior.ARCHIVE_TASKS) {
//						System.out.println("ARCHIVE");
//						// TODO archive tasks
//					}
//					if(behaviour == ListDeleteBehavior.DELETE_TASKS) {
//						System.out.println("DELETE");
//						for(TaskCard task : list.cards) {
//							DataService.tasks.deleteTask(task);
//						}
//						DataService.project.getProject().lists.remove(list);
//						DataService.project.getProject().listOrder.remove(new Integer(list.id));
//						EventManager.fireEvent(new ListDeletedEvent(list, this));
//					}
//					return true;
//				} else {
//					return false;
//				}
//			}
//		} else {
//			return false;
//		}
//	}
//
//
//
//
//	public boolean renameList(TaskList list, String newName) {
//		if(list != null) {
//			String oldName = list.title;
//			if(!oldName.equals(newName)) {
//				list.title = newName;
//				EventManager.fireEvent(new ListChangedNameEvent(list, oldName, list.title, this));
//			}
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//
//
//
//	public boolean recolorList(TaskList list, Color newColor) {
//		if(list != null) {
//			Color oldColor = list.color;
//			if(!oldColor.equals(newColor)) {
//				list.color = newColor;
//				EventManager.fireEvent(new ListChangedColorEvent(list, oldColor, list.color, this));
//			}
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//
//
//
//	public boolean hideList(TaskList list) {
//		if(list != null) {
//			if(!list.hidden) {
//				list.hidden = true;
//				EventManager.fireEvent(new ListChangedVisibilityEvent(list, false, true, this));
//			}
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//
//
//
//	public boolean unhideList(TaskList list) {
//		if(list != null) {
//			if(list.hidden) {
//				list.hidden = false;
//				EventManager.fireEvent(new ListChangedVisibilityEvent(list, true, false, this));
//			}
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//}
