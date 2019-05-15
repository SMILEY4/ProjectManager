package com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupfilter;

import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterCriteria;
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




	public abstract void expandTree();




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




	protected void removeThisCriteria() {
		if (handlerOnRemove != null) {
			handlerOnRemove.handle(new ActionEvent());
		}
	}




	public FilterCriteria getCriteria() {
		return this.criteria;
	}







	public abstract FilterCriteria buildCriteriaTree();


}
