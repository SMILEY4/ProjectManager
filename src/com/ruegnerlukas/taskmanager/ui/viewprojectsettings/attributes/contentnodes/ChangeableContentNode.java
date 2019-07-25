package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.AttributeContentNode;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.ContentNodeUtils;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems.ContentNodeItem;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * For {@link TaskAttribute}s that have special values that can be changed.
 */
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

		checkForChange();
	}




	/**
	 * Adds the given {@link ContentNodeItem} to this node.
	 */
	void addItem(ContentNodeItem item) {
		item.changedProperty.addListener((observable -> checkForChange()));
		items.add(item);
		vbox.getChildren().add(vbox.getChildren().size() - 1, item);
	}




	/**
	 * Checks for a change of any {@link ContentNodeItem} of this node and enables/disables the "Save" and "Discard" buttons.
	 */
	private void checkForChange() {

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




	/**
	 * Notifies all {@link ContentNodeItem} to write their changes to the {@link TaskAttribute}.
	 */
	private void onSave() {
		for (ContentNodeItem item : items) {
			item.save();
		}
		checkForChange();
	}




	/**
	 * Notifies all {@link ContentNodeItem} to discard their changes.
	 */
	private void onDiscard() {
		for (ContentNodeItem item : items) {
			item.reset();
		}
		checkForChange();
	}




	@Override
	public void dispose() {
		for (ContentNodeItem item : items) {
			item.dispose();
		}
	}


}
