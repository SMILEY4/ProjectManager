package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import com.ruegnerlukas.taskmanager.utils.listeners.FXChangeListener;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;

public abstract class SidebarItem extends AnchorPane {


	/**
	 * @return a created {@link SidebarItem} for the given {@link TaskAttribute} and {@link Task}.
	 */
	public static SidebarItem createItem(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		switch (attribute.type.get()) {
			case ID:
				return new ItemID(sidebar, attribute, task);
			case DESCRIPTION:
				return new ItemDescription(sidebar, attribute, task);
			case CREATED:
				return new ItemCreated(sidebar, attribute, task);
			case LAST_UPDATED:
				return new ItemLastUpdated(sidebar, attribute, task);
			case FLAG:
				return new ItemFlag(sidebar, attribute, task);
			case TEXT:
				return new ItemText(sidebar, attribute, task);
			case NUMBER:
				return new ItemNumber(sidebar, attribute, task);
			case BOOLEAN:
				return new ItemBoolean(sidebar, attribute, task);
			case CHOICE:
				return new ItemChoice(sidebar, attribute, task);
			case DATE:
				return new ItemDate(sidebar, attribute, task);
			case DEPENDENCY:
				return new ItemDependency(sidebar, attribute, task);
			default:
				return null;
		}
	}




	private final TasksSidebar sidebar;

	private final TaskAttribute attribute;
	private final Task task;

	private EventHandler<ActionEvent> handlerAttribNameChanged;
	private EventHandler<ActionEvent> handlerAttribTypeChanged;

	private FXChangeListener<String> listenerName;
	private FXChangeListener<AttributeType> listenerType;
	private FXMapChangeListener<AttributeValueType, AttributeValue<?>> listenerValues;




	public SidebarItem(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		this.sidebar = sidebar;
		this.attribute = attribute;
		this.task = task;

		listenerName = new FXChangeListener<String>(attribute.name) {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (handlerAttribNameChanged != null) {
					handlerAttribNameChanged.handle(new ActionEvent());
				}
			}
		};

		listenerType = new FXChangeListener<AttributeType>(attribute.type) {
			@Override
			public void changed(ObservableValue<? extends AttributeType> observable, AttributeType oldValue, AttributeType newValue) {
				if (handlerAttribTypeChanged != null) {
					handlerAttribTypeChanged.handle(new ActionEvent());
				}
			}
		};

		listenerValues = new FXMapChangeListener<AttributeValueType, AttributeValue<?>>(attribute.values) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends AttributeValueType, ? extends AttributeValue<?>> c) {
				onAttChangedEvent();
			}
		};

	}




	/**
	 * Called when any value of the {@link TaskAttribute} changed.
	 */
	protected abstract void onAttChangedEvent();




	/**
	 * The given {@link EventHandler} will be notified when the name of the {@link TaskAttribute} was changed.
	 */
	public void setOnAttribNameChanged(EventHandler<ActionEvent> handler) {
		handlerAttribNameChanged = handler;
	}




	/**
	 * The given {@link EventHandler} will be notified when the {@link AttributeType} of the {@link TaskAttribute} was changed.
	 */
	public void setOnAttribTypeChanged(EventHandler<ActionEvent> handler) {
		handlerAttribTypeChanged = handler;
	}




	/**
	 * @return the {@link TaskAttribute} handled by this item.
	 */
	public TaskAttribute getAttribute() {
		return attribute;
	}




	/**
	 * @return the {@link Task} handled by this item.
	 */
	public Task getTask() {
		return task;
	}




	public TasksSidebar getSidebar() {
		return sidebar;
	}




	public void dispose() {
		listenerName.removeFromAll();
		listenerType.removeFromAll();
		listenerValues.removeFromAll();
		handlerAttribNameChanged = null;
		handlerAttribTypeChanged = null;
	}

}
