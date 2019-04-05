package com.ruegnerlukas.taskmanager.utils.uielements;


import com.ruegnerlukas.taskmanager.data.attributes.TaskAttribute;
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
//
//
//
//
//	public static ListCell<TaskFlag> createListCellFlag() {
//		return new ListCell<TaskFlag>() {
//			@Override
//			protected void updateItem(TaskFlag item, boolean empty) {
//				super.updateItem(item, empty);
//				if (item == null || empty) {
//					setText("");
//				} else {
//					setText(item.name);
//				}
//			}
//		};
//	}
//
//
//
//
//
//
//
//	public static ListCell<TaskAttribute> createListCellAttribute() {
//		return new ListCell<TaskAttribute>() {
//			@Override
//			protected void updateItem(TaskAttribute item, boolean empty) {
//				super.updateItem(item, empty);
//				if(item == null || empty) {
//					setText("");
//				} else {
//					setText(item.name);
//				}
//			}
//		};
//	}
//
//
//
//
//	public static ListCell<FilterCriteria.ComparisonOp> createListCellComparisonOp() {
//		return new ListCell<FilterCriteria.ComparisonOp>() {
//			@Override
//			protected void updateItem(FilterCriteria.ComparisonOp item, boolean empty) {
//				super.updateItem(item, empty);
//				if (item == null || empty) {
//					setText("");
//				} else {
//					setText(item.display);
//				}
//			}
//		};
//	}
//
//
//
//
//	public static ListCell<Task> createListCellTask() {
//		return new ListCell<Task>() {
//			@Override
//			protected void updateItem(Task item, boolean empty) {
//				super.updateItem(item, empty);
//				if (item == null || empty) {
//					setText("");
//				} else {
//					setText("T-" + item.getID());
//				}
//			}
//		};
//	}
//
//
//
//
//	public static ListCell<SortElement.Sort> createListCellSortDir() {
//		return new ListCell<SortElement.Sort>() {
//			@Override
//			protected void updateItem(SortElement.Sort item, boolean empty) {
//				super.updateItem(item, empty);
//				if (item == null || empty) {
//					setText("");
//				} else {
//					setText(item.display);
//				}
//			}
//		};
//	}
//
//
//
//
	public static ListCell<TaskAttribute.Type> createListCellAttributeType() {
		return new ListCell<TaskAttribute.Type>() {
			@Override
			protected void updateItem(TaskAttribute.Type item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText("");
				} else {
					setText(item.display);
				}
			}
		};
	}

}

