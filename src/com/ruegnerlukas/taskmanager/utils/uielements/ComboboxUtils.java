package com.ruegnerlukas.taskmanager.utils.uielements;


import com.ruegnerlukas.taskmanager.data.localdata.projectdata.*;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortElement;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import javafx.scene.control.ListCell;

public class ComboboxUtils {


	/**
	 * @return a {@link ListCell} for Boolean-values. Values will be displayed as "True" or "False".
	 */
	public static ListCell<Boolean> createListCellBoolean() {
		return new ListCell<Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText("");
				} else {
					setText(item ? "True" : "False");
				}
			}
		};
	}




	/**
	 * @return a {@link ListCell} for {@link TaskFlag}s. Flags will be displayed as their name.
	 */
	public static ListCell<TaskFlag> createListCellFlag() {
		return new ListCell<TaskFlag>() {
			@Override
			protected void updateItem(TaskFlag item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText("");
				} else {
					setText(item.name.get());
				}
			}
		};
	}




	/**
	 * @return a {@link ListCell} for {@link TaskAttribute}s. Attributes will be displayed as their name.
	 */
	public static ListCell<TaskAttribute> createListCellAttribute() {
		return new ListCell<TaskAttribute>() {
			@Override
			protected void updateItem(TaskAttribute item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText("");
				} else {
					setText(item.name.get());
				}
			}
		};
	}




	/**
	 * @return a {@link ListCell} for {@link AttributeType}s. Attributes will be displayed as their display-name.
	 */
	public static ListCell<AttributeType> createListCellAttributeType() {
		return new ListCell<AttributeType>() {
			@Override
			protected void updateItem(AttributeType item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText("");
				} else {
					setText(item.display);
				}
			}
		};
	}




	/**
	 * @return a {@link ListCell} for {@link FilterOperation}s. Operations will be displayed as their enum-name
	 */
	public static ListCell<FilterOperation> createListCellFilterOperation() {
		return new ListCell<FilterOperation>() {
			@Override
			protected void updateItem(FilterOperation item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText("");
				} else {
					setText(item.toString());
				}
			}
		};
	}




	/**
	 * @return a {@link ListCell} for {@link SortElement.SortDir}s. Values will be displayed as "Ascending" or "Descending"
	 */
	public static ListCell<SortElement.SortDir> createListCellSortDir() {
		return new ListCell<SortElement.SortDir>() {
			@Override
			protected void updateItem(SortElement.SortDir item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText("");
				} else {
					setText(item == SortElement.SortDir.ASC ? "Ascending" : "Descending");
				}
			}
		};
	}




	/**
	 * @return a {@link ListCell} for {@link Task}s. Tasks will be displayed as "T-" and their id (e.g. T-17)
	 */
	public static ListCell<Task> createListCellTask() {
		return new ListCell<Task>() {
			@Override
			protected void updateItem(Task item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText("");
				} else {
					setText("T-?");
					for (TaskAttributeData attribute : item.getValues().keySet()) {
						if (attribute.getType().get() == AttributeType.ID) {
							int id = -1;
							TaskValue<?> valueID = item.getValue(attribute);
							if (valueID != null && valueID.getAttType() != null) {
								id = ((IDValue) valueID).getValue();
							}
							setText("T-" + id);
							break;
						}
					}
				}
			}
		};
	}


}

