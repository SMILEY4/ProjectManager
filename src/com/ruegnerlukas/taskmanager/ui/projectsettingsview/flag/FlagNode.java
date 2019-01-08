package com.ruegnerlukas.taskmanager.ui.projectsettingsview.flag;

import com.ruegnerlukas.taskmanager.logic.data.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.data.TaskFlag.FlagColor;
import com.ruegnerlukas.taskmanager.logic.services.DataService;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.editablelabel.EditableLabel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class FlagNode extends HBox {
	
	public TaskFlag flag;
	private Pane pane;
	private EditableLabel label;

	
	
	
	public FlagNode(TaskFlag flag) {
		this.flag = flag;
		
		this.setSpacing(5);
		this.setPrefSize(-1, -1);
		this.setAlignment(Pos.CENTER_LEFT);
	
		// remove flag button
		Button btnRemoveFlag = new Button();
		if(flag.isDefaultFlag) {
			btnRemoveFlag.setDisable(true);
			btnRemoveFlag.setVisible(false);
		}
		btnRemoveFlag.setMinSize(32, 32);
		btnRemoveFlag.setMaxSize(32, 32);
		ButtonUtils.makeIconButton(btnRemoveFlag, SVGIcons.getCross(), 40, 40, "black");
		btnRemoveFlag.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				DataService.flags.removeFlag(FlagNode.this.flag, false);
			}			
		});
		this.getChildren().add(btnRemoveFlag);
		
		// flag color
		pane = new Pane();
		this.getChildren().add(pane);
		pane.setMinSize(32, 32);
		pane.setMaxSize(32, 32);
		pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent event) {
				
				// select-color menu
				if (event.getButton().equals(MouseButton.PRIMARY)) {
					if (event.getClickCount() == 2) {
						ContextMenu menu = new ContextMenu();
						for(int i=0; i<FlagColor.values().length; i++) {
							final FlagColor flagColor = FlagColor.values()[i];
							Color color = flagColor.color;
							
							Pane colorPane = new Pane();
							colorPane.setMinSize(60, 30);
							colorPane.setPrefSize(60, 30);
							colorPane.setMaxSize(60, 30);
							colorPane.setStyle("-fx-background-radius: 5; -fx-background-color: rgba(" + (int)(255*color.getRed()) +","+ (int)(255*color.getGreen()) +","+ (int)(255*color.getBlue()) + ",255);");
							
							CustomMenuItem item = new CustomMenuItem();
							item.setContent(colorPane);
							item.setOnAction(new EventHandler<ActionEvent>() {
								@Override public void handle(ActionEvent event) {
									DataService.flags.recolorFlag(FlagNode.this.flag, flagColor);
								}
							});
							menu.getItems().add(item);
						}
						menu.show(pane, Side.RIGHT, 0, 0);
					}	
				}
				
			}
		});
		
		// flag name
		label = new EditableLabel();
		if(flag.isDefaultFlag) {
			label.setEditable(false);
		}
		this.getChildren().add(label);
		label.setMinWidth(300);
		label.setMaxWidth(300);
		label.addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				label.setText(FlagNode.this.flag.name);
				DataService.flags.renameFlag(FlagNode.this.flag, newValue);
			}
		});
		
		update();
	}
	
	
	
	
	public void update() {
		Color color = flag.color.color;
		String hexColor = String.format( "#%02X%02X%02X", (int)(color.getRed()*255), (int)(color.getGreen()*255), (int)(color.getBlue()*255) );
		pane.setStyle("-fx-background-color: " + hexColor + "; -fx-background-radius: 5;");
		this.label.setText(flag.name);
	}
	
	
}