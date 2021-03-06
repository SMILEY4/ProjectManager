package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.logic.utils.SetAttributeValueEffect;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.AttributeContentNode;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.ContentNodeUtils;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems.ContentNodeItem;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.popupconfirmchange.PopupConfirmChanges;
import com.ruegnerlukas.taskmanager.utils.PopupBase;
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
	 * Notifies all {@link ContentNodeItem} to write their changes to the {@link TaskAttribute} after confirmation of the user.
	 */
	private void onSave() {

		boolean applyChanges = openConfirmationPopup(getValueEffects());

		if (applyChanges) {
			for (ContentNodeItem item : items) {
				item.save();
			}
			checkForChange();
		}
	}




	/**
	 * @return a list of all effects resulting from saving the current changes
	 */
	private List<SetAttributeValueEffect> getValueEffects() {
		List<SetAttributeValueEffect> effects = new ArrayList<>();

		// apply all changes to attribute-copy
		TaskAttributeData copyAttribute = AttributeLogic.copyAttribute(getAttribute());
		for (ContentNodeItem item : items) {
			AttributeValue<?> nextAttValue = item.getAttributeValue();
			copyAttribute.getValues().put(nextAttValue.getType(), nextAttValue);
		}

		// find all affected tasks
		for (Task task : Data.projectProperty.get().data.tasks) {
			SetAttributeValueEffect effect = getValueEffects(task, getAttribute(), copyAttribute);
			if (effect != null) {
				effects.add(effect);
			}
		}

		return effects;
	}




	/**
	 * @return an effect to the given {@link Task} resulting from saving the changes to the given {@link TaskAttribute}
	 */
	private SetAttributeValueEffect getValueEffects(Task task, TaskAttribute attribute, TaskAttributeData copyAttribute) {

		TaskData copyTask = TaskLogic.copyTask(task, Data.projectProperty.get());

		boolean isAffected = false;
		TaskValue<?> prevTaskValue = TaskLogic.getValueOrDefault(task, attribute);
		TaskValue<?> nextTaskValue = null;

		boolean isPrevDefault = !task.getValues().containsKey(attribute) && AttributeLogic.getUsesDefault(attribute);
		boolean isNextDefault = false;


		if (isPrevDefault) {
			nextTaskValue = TaskLogic.getValueOrDefault(task, copyAttribute);
			if (prevTaskValue.compare(nextTaskValue) != 0) {
				isAffected = true;
				isNextDefault = !task.getValues().containsKey(attribute) && AttributeLogic.getUsesDefault(copyAttribute);
			}

		} else {
			// has own value
			if (!AttributeLogic.LOGIC_MODULES.get(copyAttribute.getType().get()).isValidTaskValue(copyAttribute, prevTaskValue)) {
				// value is invalid -> get new valid value
				TaskValue<?> validValue = AttributeLogic.LOGIC_MODULES.get(copyAttribute.getType().get()).generateValidTaskValue(prevTaskValue, copyAttribute, true);
				if (validValue == null || validValue instanceof NoValue) {
					copyTask.getValues().remove(copyAttribute);
				} else {
					copyTask.getValues().put(copyAttribute, validValue);
				}
				nextTaskValue = TaskLogic.getValueOrDefault(copyTask, copyAttribute);
				isAffected = true;
				isNextDefault = !copyTask.getValues().containsKey(copyAttribute) && AttributeLogic.getUsesDefault(copyAttribute);
			}
		}


		if (isAffected) {
			return new SetAttributeValueEffect(
					getAttribute(),
					task,
					isPrevDefault,
					isNextDefault,
					prevTaskValue,
					nextTaskValue
			);
		} else {
			return null;
		}

	}




	/**
	 * Opens a popup ({@link PopupConfirmChanges}) showing the tasks affected by the change and asking the user to confirm the changes.
	 */
	private boolean openConfirmationPopup(List<SetAttributeValueEffect> effects) {
		if (effects.isEmpty()) {
			return true;
		}
		PopupConfirmChanges popup = new PopupConfirmChanges(effects);
		PopupBase.openPopup(popup, true);
		return popup.getResult();
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
