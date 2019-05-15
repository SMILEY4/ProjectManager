package com.ruegnerlukas.taskmanager_old.ui.projectsettingsview.taskattributes.datanodes;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.AttributeNode;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.AttributeValueItem;
import com.ruegnerlukas.taskmanager.utils.uielements.alert.Alerts;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class DataNode extends VBox {


	private final AttributeNode parent;
	private final TaskAttribute attribute;
	private List<AttributeValueItem> items = new ArrayList<>();




	public DataNode(AttributeNode parent, TaskAttribute attribute) {
		this.parent = parent;
		this.attribute = attribute;
		this.setMinSize(0, 0);
		this.setPrefSize(100000, 100000);
		this.setMaxSize(100000, 100000);
		this.setSpacing(5);
		this.setPadding(new Insets(10, 0, 0, 0));
	}




	void addVaueItem(AttributeValueItem item) {
		this.items.add(item);
		this.getChildren().add(item);
	}




	public boolean hasChanges() {
		for (AttributeValueItem item : items) {
			if (item.hasChanges()) {
				return true;
			}
		}
		return false;
	}




	public AttributeNode getAttributeNode() {
		return parent;
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public double getNodeHeight() {
		double sum = 10 + (Math.max(0, items.size() - 1) * 5);
		for (AttributeValueItem item : items) {
			sum += item.getItemHeight();
		}
		return sum;
	}




	protected boolean warningOnSave(Map<TaskAttributeData.Var, TaskAttributeValue> valuesMap) {
		Boolean requiresWarning = Logic.attributeWarning.requiresWarning(getAttribute(), valuesMap).getValue();
		if (requiresWarning != null && requiresWarning) {
			ButtonType result = Alerts.confirmation("Some Tasks are affected by the changes to this Attribute.", "Continue ?", ButtonType.CANCEL, ButtonType.APPLY);
			return result == ButtonType.APPLY;
		} else {
			return true;
		}
	}




	public abstract void writeChanges();


	public abstract void discardChanges();


	public abstract void dispose();

}
