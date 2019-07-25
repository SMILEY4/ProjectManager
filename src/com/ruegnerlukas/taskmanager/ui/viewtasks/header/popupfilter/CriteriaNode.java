package com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupfilter;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;

public abstract class CriteriaNode extends AnchorPane {


	private EventHandler<ActionEvent> handlerOnRemove;
	private EventHandler<ActionEvent> handlerModified;
	private FilterCriteria criteria;




	public CriteriaNode(FilterCriteria criteria, EventHandler<ActionEvent> handlerModified) {
		this.criteria = criteria;
		this.setPadding(new Insets(10, 10, 10, 10));
		this.setMinSize(0, 0);
		this.handlerModified = handlerModified;
	}




	/**
	 * Adds the children of the {@link FilterCriteria} handled by this node as new {@link CriteriaNode}s to the list of children of this node.
	 */
	public abstract void expandTree();




	/**
	 * Notifies the listener that this node was modified in some way.
	 */
	public void onModified() {
		if (handlerModified != null) {
			handlerModified.handle(new ActionEvent());
		}
	}




	public EventHandler<ActionEvent> getOnModified() {
		return this.handlerModified;
	}




	public void setOnRemove(EventHandler<ActionEvent> handler) {
		this.handlerOnRemove = handler;
	}




	/**
	 * Notifies the listener that this node wants to be removed.
	 */
	protected void removeThisCriteria() {
		if (handlerOnRemove != null) {
			handlerOnRemove.handle(new ActionEvent());
		}
	}




	/**
	 * @return the {@link FilterCriteria} handled by this node.
	 */
	public FilterCriteria getCriteria() {
		return this.criteria;
	}




	/**
	 * @return a tree of {@link FilterCriteria} with a new {@link FilterCriteria} of this {@link CriteriaNode} as its root.
	 */
	public abstract FilterCriteria buildCriteriaTree();


}
