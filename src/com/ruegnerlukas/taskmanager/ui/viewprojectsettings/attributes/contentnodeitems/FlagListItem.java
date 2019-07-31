package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.FlagListValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapEntryChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.VBoxOrder;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.EditableLabel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Random;

public class FlagListItem extends ContentNodeItem {


	private VBox boxFlagNodes;

	private FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>> listener;

	public EventHandler<ActionEvent> handlerChangedChoices;




	public FlagListItem(TaskAttribute attribute) {
		super(attribute);


		// create scroll pane
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setMinSize(0, 200);
		scrollPane.setPrefSize(100000, 200);
		scrollPane.setMaxSize(100000, 200);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.setFitToWidth(true);
		this.getChildren().add(scrollPane);

		VBox scrollContent = new VBox();
		scrollContent.setMinSize(0, 0);
		scrollContent.setPrefSize(100000, -1);
		scrollContent.setMaxSize(100000, 10000);
		scrollContent.setSpacing(5);
		scrollContent.setPadding(new Insets(10, 10, 10, 10));
		scrollPane.setContent(scrollContent);


		// create box flags
		boxFlagNodes = new VBox();
		boxFlagNodes.setMinSize(0, 0);
		boxFlagNodes.setPrefSize(100000, -1);
		boxFlagNodes.setMaxSize(100000, 10000);
		boxFlagNodes.setSpacing(5);
		scrollContent.getChildren().add(boxFlagNodes);


		// create btn add flag
		Button btnAddFlag = new Button("Add Flag");
		btnAddFlag.setMinSize(0, 32);
		btnAddFlag.setPrefSize(100000, 32);
		btnAddFlag.setMaxSize(100000, 32);
		scrollContent.getChildren().add(btnAddFlag);


		// create region spacing
		Region regionSpacing = new Region();
		regionSpacing.setMinSize(0, 60);
		regionSpacing.setPrefSize(100000, 60);
		regionSpacing.setMaxSize(100000, 60);
		scrollContent.getChildren().add(regionSpacing);


		// set initial value
		reset();


		// add logic
		btnAddFlag.setOnAction(event -> onAddFlag());

		listener = new FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>>(attribute.values, AttributeValueType.FLAG_LIST) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends AttributeValueType, ? extends AttributeValue<?>> c) {
				checkChanged();
			}
		};

	}




	private void onAddFlag() {
		FlagItem item = buildFlagItem(null);
		boxFlagNodes.getChildren().add(item);
		if (boxFlagNodes.getChildren().size() == 1) {
			((FlagItem) boxFlagNodes.getChildren().get(0)).setRemovable(false);
		} else {
			for (Node node : boxFlagNodes.getChildren()) {
				((FlagItem) node).setRemovable(true);
			}
		}
		checkChanged();
	}




	private FlagItem buildFlagItem(TaskFlag flag) {
		FlagItem item = new FlagItem(flag);
		item.setOnRemove(event -> onRemoveFlagNode(item));
		item.setOnMoveUp(event -> onMoveUpFlagNode(item));
		item.setOnMoveDown(event -> onMoveDownFlagNode(item));
		item.colorProperty().addListener(((observable, oldValue, newValue) -> checkChanged()));
		item.nameProperty().addListener(((observable, oldValue, newValue) -> checkChanged()));
		return item;
	}




	private void onRemoveFlagNode(FlagItem item) {
		if (boxFlagNodes.getChildren().size() != 1) {
			boxFlagNodes.getChildren().remove(item);
			if (boxFlagNodes.getChildren().size() == 1) {
				((FlagItem) boxFlagNodes.getChildren().get(0)).setRemovable(false);
			} else {
				for (Node node : boxFlagNodes.getChildren()) {
					((FlagItem) node).setRemovable(true);
				}
			}
			checkChanged();
		}
	}




	private void onMoveUpFlagNode(FlagItem item) {
		int index = boxFlagNodes.getChildren().indexOf(item);
		if (index > 0) {
			VBoxOrder.moveItem(boxFlagNodes, item, -1);
		}
	}




	private void onMoveDownFlagNode(FlagItem item) {
		final int index = boxFlagNodes.getChildren().indexOf(item);
		if (index < boxFlagNodes.getChildren().size() - 1) {
			VBoxOrder.moveItem(boxFlagNodes, item, +1);
		}
	}




	private TaskFlag[] getMasterFlags() {
		return AttributeLogic.FLAG_LOGIC.getFlagList(attribute);
	}




	public void checkChanged() {
		boolean changed = false;

		if (getMasterFlags().length != boxFlagNodes.getChildren().size()) {
			changed = true;

		} else {
			TaskFlag[] flagsLocal = getTemporaryValue();
			for (TaskFlag flagAttribute : getMasterFlags()) {

				TaskFlag flagLocal = null;
				for (TaskFlag flag : flagsLocal) {
					if (flagAttribute.compare(flag)) {
						flagLocal = flag;
						break;
					}
				}

				if (flagLocal == null) {
					changed = true;
					break;
				}

			}
		}

		if (handlerChangedChoices != null) {
			handlerChangedChoices.handle(new ActionEvent());
		}

		changedProperty.set(changed);
	}




	@Override
	public void reset() {
		TaskFlag[] flags = getMasterFlags();

		// add all flags
		boxFlagNodes.getChildren().clear();
		for (TaskFlag flag : flags) {
			FlagItem item = buildFlagItem(flag);
			boxFlagNodes.getChildren().add(item);
		}

		// prevent removing last flag
		if (flags.length == 1) {
			((FlagItem) boxFlagNodes.getChildren().get(0)).setRemovable(false);
		}

		changedProperty.set(false);
	}




	@Override
	public void save() {
		if (hasChanged()) {
			AttributeLogic.setAttributeValue(Data.projectProperty.get(), attribute, getAttributeValue(), true);
			changedProperty.set(false);
		}
	}




	public TaskFlag[] getTemporaryValue() {
		TaskFlag[] flags = new TaskFlag[boxFlagNodes.getChildren().size()];
		for (int i = 0; i < flags.length; i++) {
			flags[i] = ((FlagItem) boxFlagNodes.getChildren().get(i)).getModifiedFlag();
		}
		return flags;
	}




	public TaskFlag[] getValue() {
		TaskFlag[] flags = new TaskFlag[boxFlagNodes.getChildren().size()];
		for (int i = 0; i < flags.length; i++) {
			flags[i] = ((FlagItem) boxFlagNodes.getChildren().get(i)).getFlag(true, true);
		}
		return flags;
	}




	@Override
	public AttributeValue<?> getAttributeValue() {
		return new FlagListValue(getValue());
	}




	@Override
	public void dispose() {
		listener.removeFromAll();
	}

}






class FlagItem extends HBox {


	private EventHandler<ActionEvent> handlerRemove;
	private EventHandler<ActionEvent> handlerMoveUp;
	private EventHandler<ActionEvent> handlerMoveDown;

	private Button btnRemove;
	private Pane paneColor;

	private final TaskFlag flag;
	private SimpleStringProperty nameProperty = new SimpleStringProperty();
	private CustomProperty<TaskFlag.FlagColor> colorProperty = new CustomProperty<>();




	public FlagItem(TaskFlag taskFlag) {

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
		ButtonUtils.makeIconButton(btnRemove, SVGIcons.CROSS, 0.7f);
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
		EditableLabel labelName = new EditableLabel(flag.name.get());
		labelName.setMinWidth(300);
		labelName.setPrefWidth(100000);
		labelName.setMaxWidth(100000);
		labelName.addListener((observable, oldValue, newValue) -> nameProperty.set(newValue));
		this.getChildren().add(labelName);


		// button up
		Button btnUp = new Button();
		ButtonUtils.makeIconButton(btnUp, SVGIcons.LONG_ARROW_UP, 0.7f);
		btnUp.setMinSize(32, 32);
		btnUp.setPrefSize(32, 32);
		btnUp.setMaxSize(32, 32);
		btnUp.setOnAction(event -> {
			onMoveUp();
		});
		this.getChildren().add(btnUp);


		// button down
		Button btnDown = new Button();
		ButtonUtils.makeIconButton(btnDown, SVGIcons.LONG_ARROW_DOWN, 0.7f);
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




	public void setRemovable(boolean enable) {
		btnRemove.setDisable(!enable);
	}




	public TaskFlag getFlag(boolean createNew, boolean applyModifications) {
		if (createNew) {
			if (applyModifications) {
				return new TaskFlag(this.nameProperty.get(), this.colorProperty.get());
			} else {
				return new TaskFlag(this.flag.name.get(), this.flag.color.get());
			}
		} else {
			if (applyModifications) {
				this.flag.name.set(nameProperty.get());
				this.flag.color.set(colorProperty.get());
			}
			return this.flag;
		}
	}




	public TaskFlag getModifiedFlag() {
		return new TaskFlag(nameProperty.get(), colorProperty.get());
	}


}
