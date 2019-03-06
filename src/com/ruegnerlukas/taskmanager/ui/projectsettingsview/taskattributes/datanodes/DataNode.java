package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.datanodes;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.AttributeNode;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.AttributeValueItem;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

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
		double sum = 10 + 5 * (Math.max(0, items.size() - 1) * 5);
		for (AttributeValueItem item : items) {
			sum += item.getItemHeight();
		}
		return sum;
	}




	public abstract void writeChanges();


	public abstract void discardChanges();


	public abstract void dispose();

}
