package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.AttributeContentNode;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.ContentNodeUtils;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems.ContentNodeItem;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public abstract class ChangeableContentNode extends AttributeContentNode {


	private List<ContentNodeItem> items = new ArrayList<>();

	private VBox vbox;

	private Button btnDiscard;
	private Button btnSave;




	public ChangeableContentNode(TaskAttribute attribute) {
		super(attribute);

		vbox = new VBox();
		vbox.setPadding(new Insets(10, 10, 10, 10));
		vbox.setSpacing(5);
		vbox.setMinSize(0, 0);
		AnchorUtils.setAnchors(vbox, 0, 0, 0, 0);
		this.getChildren().add(vbox);

		Button[] buttons = ContentNodeUtils.buildButtons(vbox);
		btnDiscard = buttons[0];
		btnDiscard.setOnAction(event -> onDiscard());
		btnSave = buttons[1];
		btnSave.setOnAction(event -> onSave());

		onChange();
	}




	protected void addItem(ContentNodeItem item) {
		item.changedProperty.addListener((observable -> onChange()));
		items.add(item);
		vbox.getChildren().add(vbox.getChildren().size() - 1, item);
	}




	protected void onChange() {

		boolean changed = false;
		for (ContentNodeItem item : items) {
			if (item.hasChanged()) {
				changed = true;
				break;
			}
		}

		btnDiscard.setDisable(!changed);
		btnSave.setDisable(!changed);
		changedProperty.set(changed);
	}




	private void onSave() {
		for (ContentNodeItem item : items) {
			item.save();
		}
		onChange();
	}




	private void onDiscard() {
		for (ContentNodeItem item : items) {
			item.reset();
		}
		onChange();
	}




	@Override
	public void dispose() {
		for (ContentNodeItem item : items) {
			item.dispose();
		}
	}


}
