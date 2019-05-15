package com.ruegnerlukas.taskmanager.utils.uielements;


import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.sort.SortElement;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import javafx.scene.control.ListCell;

public class ComboboxUtils {


	//
//	public static void initFlagColor(ComboBox<TaskFlag> combobox) {
//		combobox.setButtonCell(new ListCell<TaskFlag>() {
//			@Override
//			protected void updateItem(TaskFlag item, boolean empty) {
//				super.updateItem(item, empty);
//				if (item == null || empty) {
//					setText("");
//					setGraphic(null);
//				} else {
//
//					Color flagColor = item.color.color;
//					String strColor = "rgba(" + (int) (255 * flagColor.getRed()) + "," + (int) (255 * flagColor.getGreen()) + "," + (int) (255 * flagColor.getBlue()) + ",255)";
//
//					Label label = new Label(item.name);
//					label.setAlignment(Pos.CENTER);
//					label.minWidthProperty().bind(super.widthProperty());
//					label.maxWidthProperty().bind(super.widthProperty());
//					label.setPadding(new Insets(2, 5, 2, 5));
//					label.setStyle("-fx-background-radius: 9; -fx-background-color: " + strColor + ";");
//
//					setText("");
//					setGraphic(label);
//				}
//			}
//		});
//		combobox.setCellFactory(new Callback<ListView<TaskFlag>, ListCell<TaskFlag>>() {
//			@Override
//			public ListCell<TaskFlag> call(ListView<TaskFlag> p) {
//				return new ListCell<TaskFlag>() {
//					@Override
//					protected void updateItem(TaskFlag item, boolean empty) {
//						super.updateItem(item, empty);
//						if (item == null || empty) {
//							setText("");
//							setGraphic(null);
//						} else {
//
//							Color flagColor = item.color.color;
//							String strColor = "rgba(" + (int) (255 * flagColor.getRed()) + "," + (int) (255 * flagColor.getGreen()) + "," + (int) (255 * flagColor.getBlue()) + ",255)";
//
//							Label label = new Label(item.name);
//							label.setAlignment(Pos.CENTER);
//							label.minWidthProperty().bind(combobox.widthProperty().subtract(40));
//							label.maxWidthProperty().bind(combobox.widthProperty().subtract(40));
//							label.setPadding(new Insets(2, 5, 2, 5));
//							label.setStyle("-fx-background-radius: 9; -fx-background-color: " + strColor + ";");
//
//							setText(null);
//							setGraphic(label);
//
//						}
//					}
//				};
//			}
//		});
//	}
//
//
//
//
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




	public static ListCell<SortElement.SortDir> createListCellSortDir() {
		return new ListCell<SortElement.SortDir>() {
			@Override
			protected void updateItem(SortElement.SortDir item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText("");
				} else {
					setText(item.toString());
				}
			}
		};
	}




	public static ListCell<Task> createListCellTask() {
		return new ListCell<Task>() {
			@Override
			protected void updateItem(Task item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText("");
				} else {
					setText("T-?");
					for (TaskAttribute attribute : item.attributes.keySet()) {
						if (attribute.type.get() == AttributeType.ID) {
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

