package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


/**
 * Represents a single changable value of a given {@link TaskAttribute}.
 * Consists of a {@link Label} with the name of this value and a {@link Node} to change the value.
 */
public abstract class SimpleContentNodeItem extends ContentNodeItem {


	private String name;
	private HBox boxNode;
	private Node node;




	/**
	 * Creates a new item for the given {@link TaskAttribute} with the given name and {@link Node}.
	 */
	public SimpleContentNodeItem(TaskAttribute attribute, String name, Node node) {
		this(attribute, name);
		setNode(node);
	}




	/**
	 * Creates a new item for the given {@link TaskAttribute} with the given name and node node (yet!).
	 */
	public SimpleContentNodeItem(TaskAttribute attribute, String name) {
		super(attribute);
		this.name = name;

		this.setMinSize(0, 32);
		this.setPrefWidth(10000);
		this.setSpacing(20);

		Label label = new Label(name + ":");
		label.setMinSize(0, 32);
		label.setPrefSize(10000, 32);
		label.setMaxSize(10000, 32);
		label.setAlignment(Pos.CENTER_RIGHT);
		this.getChildren().add(label);

		boxNode = new HBox();
		boxNode.setMinSize(0, 32);
		boxNode.setPrefSize(10000, 32);
		boxNode.setMaxSize(10000, 10000);
		boxNode.setAlignment(Pos.CENTER_LEFT);
		this.getChildren().add(boxNode);

	}




	/**
	 * Sets the node of this item to the given {@link Node}.
	 */
	public void setNode(Node node) {
		this.node = node;
		boxNode.getChildren().setAll(node);
	}




	/**
	 * @return the name of this item.
	 */
	public String getName() {
		return name;
	}




	/**
	 * @return the {@link Node} if this item.
	 */
	public Node getNode() {
		return node;
	}


}
