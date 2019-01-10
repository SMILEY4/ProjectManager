package com.ruegnerlukas.taskmanager.utils.uielements.combobox;


import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.logic_v1.data.TaskList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class ComboboxUtils {

	
	public static void initComboboxTaskFlag(ComboBox<TaskFlag> combobox) {
		combobox.setButtonCell(new ListCell<TaskFlag>() {
			@Override protected void updateItem(TaskFlag item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText("");
					setGraphic(null);
				} else {
					
					Color flagColor = item.color.color;
					String strColor = "rgba(" + (int)(255*flagColor.getRed()) +","+ (int)(255*flagColor.getGreen()) +","+ (int)(255*flagColor.getBlue()) + ",255)";

					Label label = new Label(item.name);
					label.setAlignment(Pos.CENTER);
					label.minWidthProperty().bind(super.widthProperty());
					label.maxWidthProperty().bind(super.widthProperty());
					label.setPadding(new Insets(2, 5, 2, 5));
					label.setStyle("-fx-background-radius: 9; -fx-background-color: " + strColor + ";");
					
					setText("");
					setGraphic(label);
				}
			}
		});
		combobox.setCellFactory(new Callback<ListView<TaskFlag>, ListCell<TaskFlag>>() {
			@Override public ListCell<TaskFlag> call(ListView<TaskFlag> p) {
				return new ListCell<TaskFlag>() {
					@Override protected void updateItem(TaskFlag item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setText("");
							setGraphic(null);
						} else {
							
							Color flagColor = item.color.color;
							String strColor = "rgba(" + (int)(255*flagColor.getRed()) +","+ (int)(255*flagColor.getGreen()) +","+ (int)(255*flagColor.getBlue()) + ",255)";

							Label label = new Label(item.name);
							label.setAlignment(Pos.CENTER);
							label.minWidthProperty().bind(combobox.widthProperty().subtract(40));
							label.maxWidthProperty().bind(combobox.widthProperty().subtract(40));
							label.setPadding(new Insets(2, 5, 2, 5));
							label.setStyle("-fx-background-radius: 9; -fx-background-color: " + strColor + ";");
							
							setText(null);
							setGraphic(label);
							
						}
					}
				};
			}
		});
	}
	
	
	
	
	public static void initComboboxTasklist(ComboBox<TaskList> combobox) {
		combobox.setButtonCell(new ListCell<TaskList>() {
			@Override protected void updateItem(TaskList item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText("");
					setGraphic(null);
				} else {
					setText(item.title);
					setGraphic(null);
				}
			}
		});
		combobox.setCellFactory(new Callback<ListView<TaskList>, ListCell<TaskList>>() {
			@Override public ListCell<TaskList> call(ListView<TaskList> p) {
				return new ListCell<TaskList>() {
					@Override protected void updateItem(TaskList item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setText("");
							setGraphic(null);
						} else {
							setText(item.title);
							setGraphic(null);
						}
					}
				};
			}
		});
	}
	
	
	
}
