package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.attributes.TaskFlagAttributeLogic;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.AttributeContentNode;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.ContentNodeUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ComboboxUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.VBoxOrder;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class FlagContentNode extends AttributeContentNode {


	private VBox boxFlagNodes;
	private ComboBox<TaskFlag> choiceDefaultValue;
	private Button btnDiscard;
	private Button btnSave;

	private Map<String, Object> values = new HashMap<>();




	public FlagContentNode(TaskAttribute attribute) {
		super(attribute);

		// set values
		values.put(TaskFlagAttributeLogic.FLAG_DEFAULT_VALUE, TaskFlagAttributeLogic.getDefaultValue(attribute));
		values.put(TaskFlagAttributeLogic.FLAG_FLAG_LIST, TaskFlagAttributeLogic.getFlagList(attribute));


		// root box
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10, 10, 10, 10));
		vbox.setSpacing(15);
		vbox.setMinSize(0, 0);
		AnchorUtils.setAnchors(vbox, 0, 0, 0, 0);
		this.getChildren().add(vbox);


		buildFlagList(vbox);
		buildDefaultValue(vbox);
		buildButtons(vbox);

		checkChanges();
	}




	private void buildDefaultValue(VBox root) {

		// default value
		HBox boxAlignDefault = ContentNodeUtils.buildEntryWithAlignment(root, "Default Flag:");

		choiceDefaultValue = new ComboBox<>();
		choiceDefaultValue.setButtonCell(ComboboxUtils.createListCellFlag());
		choiceDefaultValue.setCellFactory(param -> ComboboxUtils.createListCellFlag());
		choiceDefaultValue.getItems().addAll(getLocalFlagList());
		choiceDefaultValue.getSelectionModel().select(getLocalDefaultValue());
		choiceDefaultValue.setMinSize(60, 32);
		choiceDefaultValue.setPrefSize(150, 32);
		choiceDefaultValue.setMaxSize(150, 32);
		boxAlignDefault.getChildren().add(choiceDefaultValue);
		choiceDefaultValue.getSelectionModel().select(getLocalDefaultValue());
		choiceDefaultValue.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onDefaultValue(newValue);
		});
	}




	private void buildButtons(VBox root) {
		Button[] buttons = ContentNodeUtils.buildButtons(root);
		btnDiscard = buttons[0];
		btnDiscard.setOnAction(event -> onDiscard());
		btnSave = buttons[1];
		btnSave.setOnAction(event -> onSave());
	}




	private void buildFlagList(VBox root) {

		// scroll pane
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setMinSize(0, 200);
		scrollPane.setPrefSize(100000, 200);
		scrollPane.setMaxSize(100000, 200);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.setFitToWidth(true);
		root.getChildren().add(scrollPane);

		VBox scrollContent = new VBox();
		scrollContent.setMinSize(0, 0);
		scrollContent.setPrefSize(100000, -1);
		scrollContent.setMaxSize(100000, 10000);
		scrollContent.setSpacing(5);
		scrollContent.setPadding(new Insets(10, 10, 10, 10));
		scrollPane.setContent(scrollContent);

		// box flags
		boxFlagNodes = new VBox();
		boxFlagNodes.setMinSize(0, 0);
		boxFlagNodes.setPrefSize(100000, -1);
		boxFlagNodes.setMaxSize(100000, 10000);
		boxFlagNodes.setSpacing(5);
		scrollContent.getChildren().add(boxFlagNodes);

		for (TaskFlag flag : getLocalFlagList()) {
			boxFlagNodes.getChildren().add(buildFlagNode(flag));
		}

		// btn add flag
		Button btnAddFlag = new Button("Add Flag");
		btnAddFlag.setMinSize(0, 32);
		btnAddFlag.setPrefSize(100000, 32);
		btnAddFlag.setMaxSize(100000, 32);
		scrollContent.getChildren().add(btnAddFlag);
		btnAddFlag.setOnAction(event -> onAddFlag());


		// region spacing
		Region regionSpacing = new Region();
		regionSpacing.setMinSize(0, 60);
		regionSpacing.setPrefSize(100000, 60);
		regionSpacing.setMaxSize(100000, 60);
		scrollContent.getChildren().add(regionSpacing);

		if (getLocalFlagList().length <= 1) {
			for (Node node : boxFlagNodes.getChildren()) {
				((FlagNode) node).enableRemove(false);
			}
		} else {
			for (Node node : boxFlagNodes.getChildren()) {
				((FlagNode) node).enableRemove(true);
			}
		}

	}




	private void onAddFlag() {
		FlagNode flagNode = buildFlagNode(null);
		boxFlagNodes.getChildren().add(flagNode);
		choiceDefaultValue.getItems().add(flagNode.getFlag());
		onFlagList();
		checkChanges();
	}




	private FlagNode buildFlagNode(TaskFlag flag) {
		FlagNode flagNode = new FlagNode(flag);
		flagNode.setOnRemove(event -> onRemoveFlagNode(flagNode));
		flagNode.setOnMoveUp(event -> onMoveUpFlagNode(flagNode));
		flagNode.setOnMoveDown(event -> onMoveDownFlagNode(flagNode));
		flagNode.colorProperty().addListener(((observable, oldValue, newValue) -> onFlagList()));
		flagNode.nameProperty().addListener(((observable, oldValue, newValue) -> onFlagList()));
		return flagNode;
	}




	private void onRemoveFlagNode(FlagNode flagNode) {
		if (getLocalFlagList().length <= 1) {
			return;
		}
		boxFlagNodes.getChildren().remove(flagNode);
		if (getLocalDefaultValue() == flagNode.getFlag()) {
			choiceDefaultValue.getItems().remove(flagNode.getFlag());
			choiceDefaultValue.getSelectionModel().select(choiceDefaultValue.getItems().get(0));
		} else {
			choiceDefaultValue.getItems().remove(flagNode.getFlag());
		}
		onFlagList();
	}




	private void onMoveUpFlagNode(FlagNode flagNode) {
		int index = boxFlagNodes.getChildren().indexOf(flagNode);
		if (index > 0) {
			VBoxOrder.moveItem(boxFlagNodes, flagNode, -1);
			onFlagList();
		}
	}




	private void onMoveDownFlagNode(FlagNode flagNode) {
		final int index = boxFlagNodes.getChildren().indexOf(flagNode);
		if (index < boxFlagNodes.getChildren().size() - 1) {
			VBoxOrder.moveItem(boxFlagNodes, flagNode, +1);
			onFlagList();
		}
	}




	private void onFlagList() {
		values.put(TaskFlagAttributeLogic.FLAG_FLAG_LIST, collectLocalFlagList());
		TaskFlag defaultFlag = getLocalDefaultValue();
		choiceDefaultValue.getItems().clear();
		choiceDefaultValue.getItems().addAll(getLocalFlagList());
		choiceDefaultValue.getSelectionModel().select(defaultFlag);
		if (getLocalFlagList().length <= 1) {
			for (Node node : boxFlagNodes.getChildren()) {
				((FlagNode) node).enableRemove(false);
			}
		} else {
			for (Node node : boxFlagNodes.getChildren()) {
				((FlagNode) node).enableRemove(true);
			}
		}
		checkChanges();
	}




	private void onDefaultValue(TaskFlag defaultValue) {
		values.put(TaskFlagAttributeLogic.FLAG_DEFAULT_VALUE, defaultValue);
		checkChanges();
	}




	private void checkChanges() {
		boolean hasChanged = compareValues(values);
		changedProperty.set(!hasChanged);
		btnDiscard.setDisable(hasChanged);
		btnSave.setDisable(hasChanged);
	}




	@Override
	protected boolean compareValues(Map<String, Object> values) {

		if (getLocalDefaultValue() != TaskFlagAttributeLogic.getDefaultValue(attribute)) {
			return false;
		}

		TaskFlag[] localFlags = collectTrueLocalFlagList();
		TaskFlag[] attribFlags = TaskFlagAttributeLogic.getFlagList(attribute);

		if (localFlags.length != attribFlags.length) {
			return false;
		}

		for (int i = 0; i < localFlags.length; i++) {
			TaskFlag flagLocal = localFlags[i];
			TaskFlag flagAttrib = attribFlags[i];
			if (!flagLocal.compare(flagAttrib)) {
				return false;
			}
		}

		return true;
	}




	private void onSave() {
		for (Node node : boxFlagNodes.getChildren()) {
			((FlagNode) node).commitValues();
		}
		values.put(TaskFlagAttributeLogic.FLAG_FLAG_LIST, getLocalFlagList());
		saveValues(values);
		checkChanges();
	}




	private void onDiscard() {
		discardValues(values);

		boxFlagNodes.getChildren().clear();
		for (TaskFlag flag : TaskFlagAttributeLogic.getFlagList(attribute)) {
			boxFlagNodes.getChildren().add(buildFlagNode(flag));
		}

		TaskFlag defaultFlag = getLocalDefaultValue();
		choiceDefaultValue.getItems().clear();
		choiceDefaultValue.getItems().addAll(TaskFlagAttributeLogic.getFlagList(attribute));
		choiceDefaultValue.getSelectionModel().select(defaultFlag);

		onFlagList();
		checkChanges();
	}




	private TaskFlag[] collectLocalFlagList() {
		TaskFlag[] flags = new TaskFlag[boxFlagNodes.getChildren().size()];
		int i = 0;
		for (Node node : boxFlagNodes.getChildren()) {
			FlagNode flagNode = (FlagNode) node;
			flags[i++] = flagNode.getFlag();
		}
		return flags;
	}




	private TaskFlag[] collectTrueLocalFlagList() {
		TaskFlag[] flags = new TaskFlag[boxFlagNodes.getChildren().size()];
		int i = 0;
		for (Node node : boxFlagNodes.getChildren()) {
			FlagNode flagNode = (FlagNode) node;
			TaskFlag flag = new TaskFlag(flagNode.nameProperty().get(), flagNode.colorProperty().get());
			flags[i++] = flag;
		}
		return flags;
	}




	private TaskFlag[] getLocalFlagList() {
		return (TaskFlag[]) values.get(TaskFlagAttributeLogic.FLAG_FLAG_LIST);
	}




	private TaskFlag getLocalDefaultValue() {
		return (TaskFlag) values.get(TaskFlagAttributeLogic.FLAG_DEFAULT_VALUE);
	}




	@Override
	public double getContentHeight() {
		return 330;
	}

}
