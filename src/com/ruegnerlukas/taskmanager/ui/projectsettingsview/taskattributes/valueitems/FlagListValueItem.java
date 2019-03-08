package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems;

import com.ruegnerlukas.simpleutils.arrays.ArrayUtils;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.vbox.VBoxOrder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public abstract class FlagListValueItem extends AttributeValueItem {


	private List<FlagValueItem> flagItems = new ArrayList<>();
	private VBox boxFlags;
	private Button btnAdd;




	public FlagListValueItem(String name, TaskFlag[] flags, TaskFlag defaultFlag) {
		super();
		this.mute();

		AnchorPane pane = new AnchorPane();
		pane.setMinSize(0, 240);
		pane.setPrefSize(10000, 240);
		pane.setMaxSize(10000, 240);
		AnchorUtils.setAnchors(pane, 0, 0, 0, 0);
		this.getChildren().add(pane);

		Label label = new Label(name);
		label.setAlignment(Pos.CENTER_LEFT);
		label.setMinHeight(32);
		label.setMaxHeight(32);
		AnchorPane.setLeftAnchor(label, 10.0);
		AnchorPane.setTopAnchor(label, 10.0);
		pane.getChildren().add(label);

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
		AnchorUtils.setAnchors(scrollPane, 42, 10, 10, 10);
		pane.getChildren().add(scrollPane);

		VBox boxContent = new VBox();
		boxContent.setSpacing(5);
		boxContent.setFillWidth(true);
		boxContent.setPadding(new Insets(10, 10, 10, 10));
		scrollPane.setContent(boxContent);

		boxFlags = new VBox();
		boxFlags.setSpacing(5);
		boxFlags.setFillWidth(true);
		boxFlags.getChildren().setAll(flagItems);
		boxContent.getChildren().add(boxFlags);

		btnAdd = new Button("Add Flag");
		btnAdd.setMinSize(0, 32);
		btnAdd.setPrefSize(10000, 32);
		btnAdd.setMaxSize(10000, 32);
		btnAdd.setOnAction(event -> {
			createNewFlag();
		});
		boxContent.getChildren().add(btnAdd);

		AnchorUtils.setAnchors(this, 0, 0, 0, 0);
		this.setPrefSize(10000, 240);

		setValue(flags);
		unlockAll();
		lockFlag(defaultFlag);

		this.setChanged(false);
		this.unmute();
	}




	private void createNewFlag() {
		TaskFlag flag = new TaskFlag(TaskFlag.FlagColor.GRAY, "Flag " + Integer.toHexString(new Object().hashCode()) );
		FlagValueItem item = new FlagValueItem(flag) {

			@Override
			public void onChanged() {
			}




			@Override
			public void onRemove() {
				removeFlagItem(this);
			}




			@Override
			public void onMoveUp() {
				moveItemUp(this);
			}




			@Override
			public void onMoveDown() {
				moveItemDown(this);
			}




			@Override
			protected void onSetValue() {
				setChanged(true);
			}

		};
		flagItems.add(item);
		setValue(getValue());
	}




	private void removeFlagItem(FlagValueItem item) {
		flagItems.remove(item);
		boxFlags.getChildren().remove(item);
		setChanged(true);
	}




	private void moveItemUp(FlagValueItem item) {
		int index = boxFlags.getChildren().indexOf(item);
		if (index > 0) {
			VBoxOrder.moveItem(boxFlags, item, -1);
			ArrayUtils.moveElement(flagItems, item, false);
			setChanged(true);
		}
	}




	private void moveItemDown(FlagValueItem item) {
		final int index = boxFlags.getChildren().indexOf(item);
		if (index < boxFlags.getChildren().size() - 1) {
			VBoxOrder.moveItem(boxFlags, item, +1);
			ArrayUtils.moveElement(flagItems, item, true);
			setChanged(true);
		}
	}




	public void unlockFlag(TaskFlag flag) {
		for (FlagValueItem item : flagItems) {
			if (item.getValue() == flag) {
				item.setLocked(false);
			}
		}
	}




	public void unlockAll() {
		for (FlagValueItem item : flagItems) {
			item.setLocked(false);
		}
	}




	public void lockFlag(TaskFlag flag) {
		for (FlagValueItem item : flagItems) {
			if (item.getValue().name.equals(flag.name)) {
				item.setLocked(true);
			}
		}
	}




	public void setValue(TaskFlag[] flags) {

		flagItems.clear();
		for (TaskFlag flag : flags) {

			flagItems.add(new FlagValueItem(flag) {

				@Override
				public void onChanged() {
				}




				@Override
				public void onRemove() {
					removeFlagItem(this);
				}




				@Override
				public void onMoveUp() {
					moveItemUp(this);
				}




				@Override
				public void onMoveDown() {
					moveItemDown(this);
				}




				@Override
				protected void onSetValue() {
					setChanged(true);
				}

			});

		}
		boxFlags.getChildren().setAll(flagItems);

		setChanged(true);
	}




	public TaskFlag[] getValue() {
		TaskFlag[] flags = new TaskFlag[flagItems.size()];
		for (int i = 0; i < flagItems.size(); i++) {
			flags[i] = flagItems.get(i).getValue();
		}
		return flags;
	}




	@Override
	public double getItemHeight() {
		return 240;
	}

}
