package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.EditableLabel;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Random;


public class FlagNode extends HBox {


	private EventHandler<ActionEvent> handlerRemove;
	private EventHandler<ActionEvent> handlerMoveUp;
	private EventHandler<ActionEvent> handlerMoveDown;

	private Button btnRemove;
	private Pane paneColor;
	private EditableLabel labelName;

	private final TaskFlag flag;
	private SimpleStringProperty nameProperty = new SimpleStringProperty();
	private CustomProperty<TaskFlag.FlagColor> colorProperty = new CustomProperty<>();




	public FlagNode() {
		this(null);
	}




	public FlagNode(TaskFlag taskFlag) {

		if (taskFlag == null) {
			this.flag = new TaskFlag("Flag " + Integer.toHexString(("Flag-" + (new Random(System.nanoTime()).nextInt())).hashCode()), TaskFlag.FlagColor.GRAY);
		} else {
			this.flag = taskFlag;
		}
		nameProperty.set(this.flag.name.get());
		colorProperty.set(this.flag.color.get());

		// root box
		this.setSpacing(5);
		this.setAlignment(Pos.CENTER_LEFT);
		this.setMinSize(0, 38);
		this.setPrefSize(10000, 38);
		this.setMaxSize(10000, 38);
		this.setStyle("-fx-border-color: #aaaaaa; -fx-border-radius: 5;");

		// button remove
		btnRemove = new Button();
		btnRemove.setMinSize(32, 32);
		btnRemove.setMaxSize(32, 32);
		ButtonUtils.makeIconButton(btnRemove, SVGIcons.CROSS, 0.7f, "black");
		btnRemove.setOnAction(event -> {
			onRemove();
		});
		this.getChildren().add(btnRemove);


		// flag color
		paneColor = new Pane();
		paneColor.setStyle("-fx-background-color: " + flag.color.get().asHex() + ";");
		paneColor.setMinSize(22, 22);
		paneColor.setMaxSize(22, 22);
		paneColor.setOnMouseClicked(event -> {
			// select-color menu
			if (event.getButton().equals(MouseButton.PRIMARY)) {
				if (event.getClickCount() == 2) {
					showColorSelectMenu(paneColor);
				}
			}
		});
		this.getChildren().add(paneColor);


		// flag name
		labelName = new EditableLabel(flag.name.get());
		labelName.setMinWidth(300);
		labelName.setPrefWidth(100000);
		labelName.setMaxWidth(100000);
		labelName.addListener((observable, oldValue, newValue) -> nameProperty.set(newValue));
		this.getChildren().add(labelName);


		// button up
		Button btnUp = new Button();
		ButtonUtils.makeIconButton(btnUp, SVGIcons.LONG_ARROW_UP, 0.7f, "black");
		btnUp.setMinSize(32, 32);
		btnUp.setPrefSize(32, 32);
		btnUp.setMaxSize(32, 32);
		btnUp.setOnAction(event -> {
			onMoveUp();
		});
		this.getChildren().add(btnUp);


		// button down
		Button btnDown = new Button();
		ButtonUtils.makeIconButton(btnDown, SVGIcons.LONG_ARROW_DOWN, 0.7f, "black");
		btnDown.setMinSize(32, 32);
		btnDown.setPrefSize(32, 32);
		btnDown.setMaxSize(32, 32);
		btnDown.setOnAction(event -> {
			onMoveDown();
		});
		this.getChildren().add(btnDown);


	}




	private void showColorSelectMenu(Node node) {
		ContextMenu menu = new ContextMenu();
		for (int i = 0; i < TaskFlag.FlagColor.values().length; i++) {
			final TaskFlag.FlagColor flagColor = TaskFlag.FlagColor.values()[i];
			Color color = flagColor.color;

			Pane colorItem = new Pane();
			colorItem.setMinSize(60, 30);
			colorItem.setPrefSize(60, 30);
			colorItem.setMaxSize(60, 30);
			colorItem.setStyle("-fx-background-color: rgba(" + (int) (255 * color.getRed()) + "," + (int) (255 * color.getGreen()) + "," + (int) (255 * color.getBlue()) + ",255);");

			CustomMenuItem item = new CustomMenuItem();
			item.setContent(colorItem);
			item.setOnAction(event1 -> {
				colorProperty.set(flagColor);
				paneColor.setStyle("-fx-background-color: " + flagColor.asHex() + ";");
			});
			menu.getItems().add(item);
		}
		menu.show(node, Side.RIGHT, 0, 0);
	}




	public void commitValues() {
		this.flag.color.set(this.colorProperty.get());
		this.flag.name.set(this.nameProperty.get());
	}




	private void onRemove() {
		if (handlerRemove != null) {
			handlerRemove.handle(new ActionEvent());
		}
	}




	private void onMoveUp() {
		if (handlerMoveUp != null) {
			handlerMoveUp.handle(new ActionEvent());
		}
	}




	private void onMoveDown() {
		if (handlerMoveDown != null) {
			handlerMoveDown.handle(new ActionEvent());
		}
	}




	public void setOnRemove(EventHandler<ActionEvent> handler) {
		this.handlerRemove = handler;
	}




	public void setOnMoveUp(EventHandler<ActionEvent> handler) {
		this.handlerMoveUp = handler;
	}




	public void setOnMoveDown(EventHandler<ActionEvent> handler) {
		this.handlerMoveDown = handler;
	}




	public SimpleStringProperty nameProperty() {
		return this.nameProperty;
	}




	public CustomProperty<TaskFlag.FlagColor> colorProperty() {
		return this.colorProperty;
	}




	public TaskFlag getFlag() {
		return this.flag;
	}




	public void enableRemove(boolean enable) {
		btnRemove.setDisable(!enable);
	}


}
