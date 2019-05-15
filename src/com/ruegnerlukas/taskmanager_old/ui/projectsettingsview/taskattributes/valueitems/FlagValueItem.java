package com.ruegnerlukas.taskmanager_old.ui.projectsettingsview.taskattributes.valueitems;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.AttributeValueItem;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.editablelabel.EditableLabel;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public abstract class FlagValueItem extends AttributeValueItem {


	private TaskFlag.FlagColor color;
	private String flagName;

	private Button btnRemove;
	private Pane paneColor;
	private EditableLabel labelName;
	private Button btnUp;
	private Button btnDown;




	public FlagValueItem(TaskFlag flag) {
		super();
		this.color = flag.color;
		this.flagName = flag.name;
		this.setId("item_flag");

		HBox box = new HBox();
		box.setSpacing(5);
		box.setAlignment(Pos.CENTER_LEFT);
		box.setMinSize(0, 32);
		box.setPrefSize(10000, 32);
		box.setMaxSize(10000, 32);
		AnchorUtils.setAnchors(box, 0, 0, 0, 0);
		this.getChildren().add(box);

		// button remove
		btnRemove = new Button();
		btnRemove.setMinSize(32, 32);
		btnRemove.setMaxSize(32, 32);
		ButtonUtils.makeIconButton(btnRemove, SVGIcons.CROSS, 0.7f, "black");
		btnRemove.setOnAction(event -> {
			onRemove();
		});
		box.getChildren().add(btnRemove);

		// flag color
		paneColor = new Pane();
		box.getChildren().add(paneColor);
		paneColor.setMinSize(22, 22);
		paneColor.setMaxSize(22, 22);
		paneColor.setOnMouseClicked(event -> {
			// select-color menu
			if (event.getButton().equals(MouseButton.PRIMARY)) {
				if (event.getClickCount() == 2) {
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
							setColor(flagColor);
						});
						menu.getItems().add(item);
					}
					menu.show(paneColor, Side.RIGHT, 0, 0);
				}
			}
		});
		setColor(color);


		// flag name
		labelName = new EditableLabel();
		box.getChildren().add(labelName);
		labelName.setMinWidth(300);
		labelName.setPrefWidth(100000);
		labelName.setMaxWidth(100000);
		labelName.addListener((observable, oldValue, newValue) -> {
			setName(newValue);
		});
		setName(flag.name);

		// button up
		btnUp = new Button();
		ButtonUtils.makeIconButton(btnUp, SVGIcons.LONG_ARROW_UP, 0.7f, "black");
		btnUp.setMinSize(32, 32);
		btnUp.setPrefSize(32, 32);
		btnUp.setMaxSize(32, 32);
		btnUp.setOnAction(event -> {
			onMoveUp();

		});
		box.getChildren().add(btnUp);


		// button down
		btnDown = new Button();
		ButtonUtils.makeIconButton(btnDown, SVGIcons.LONG_ARROW_DOWN, 0.7f, "black");
		btnDown.setMinSize(32, 32);
		btnDown.setPrefSize(32, 32);
		btnDown.setMaxSize(32, 32);
		btnDown.setOnAction(event -> {
			onMoveDown();
		});
		box.getChildren().add(btnDown);


		this.setPrefSize(10000, 32);

		paneColor.setStyle("-fx-background-color: " + color.asHex() + ";");
		labelName.setText(flagName);

	}




	protected void onRemove() {
	}




	protected void onMoveUp() {
	}




	protected void onMoveDown() {
	}




	protected void onSetValue() {
	}




	private void setColor(TaskFlag.FlagColor color) {
		setValue(this.flagName, color);
	}




	private void setName(String name) {
		setValue(name, this.color);
	}




	public void setLocked(boolean locked) {
		labelName.setDisable(locked);
		btnRemove.setDisable(locked);
		paneColor.setDisable(locked);
	}




	public void setValue(String name, TaskFlag.FlagColor color) {

		if (!flagName.equals(name) || this.color != color) {
			this.flagName = name;
			this.color = color;
			paneColor.setStyle("-fx-background-color: " + color.asHex() + ";");
			labelName.setText(name);
			setChanged(true);
			onSetValue();
		}

	}




	public TaskFlag getValue() {
		return new TaskFlag(color, flagName);
	}




	@Override
	public double getItemHeight() {
		return 32;
	}


}
