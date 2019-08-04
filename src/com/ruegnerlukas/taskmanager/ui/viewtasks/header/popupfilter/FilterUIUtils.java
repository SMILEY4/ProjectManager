package com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupfilter;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.*;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class FilterUIUtils {


	/**
	 * @return a created empty {@link FilterCriteria} with the given {@link FilterCriteria.CriteriaType}.
	 */
	public static FilterCriteria createFilterCriteria(FilterCriteria.CriteriaType type) {
		if (type == FilterCriteria.CriteriaType.AND) {
			return new AndFilterCriteria();
		}
		if (type == FilterCriteria.CriteriaType.OR) {
			return new OrFilterCriteria();
		}
		if (type == FilterCriteria.CriteriaType.TERMINAL) {
			return createDefaultTerminalCriteria();
		}
		return null;
	}




	/**
	 * @return a created {@link TerminalFilterCriteria} in a neutral/default state.
	 */
	private static TerminalFilterCriteria createDefaultTerminalCriteria() {
		final TaskAttribute attribute = AttributeLogic.findAttributeByType(Data.projectProperty.get(), AttributeType.ID);
		final FilterOperation operation = FilterOperation.EQUALS;
		final Object[] values = new Object[]{0};
		return new TerminalFilterCriteria(attribute, operation, values);
	}




	/**
	 * @return a created {@link CriteriaNode} with the given {@link FilterCriteria}.
	 * Adds the givne {@link EventHandler} to the created node.
	 */
	public static CriteriaNode createFilterNode(FilterCriteria criteria, EventHandler<ActionEvent> handerModified) {
		switch (criteria.type) {
			case TERMINAL:
				return new TerminalNode(criteria, handerModified);
			case AND:
				return new AndNode(criteria, handerModified);
			case OR:
				return new OrNode(criteria, handerModified);
			default:
				return null;
		}
	}




	/**
	 * Initializes the given {@link ComboBox} to display and select {@link FilterCriteria.CriteriaType}s.
	 */
	public static void initComboxCriteriaType(ComboBox<FilterCriteria.CriteriaType> combobox) {
		combobox.setPromptText("Create Criteria");
		combobox.setCellFactory(new Callback<ListView<FilterCriteria.CriteriaType>, ListCell<FilterCriteria.CriteriaType>>() {
			@Override
			public ListCell<FilterCriteria.CriteriaType> call(ListView<FilterCriteria.CriteriaType> p) {
				return new ListCell<FilterCriteria.CriteriaType>() {
					@Override
					protected void updateItem(FilterCriteria.CriteriaType item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setText("");
							setGraphic(null);
						} else {
							setText(item.toString());
							setGraphic(null);
						}
					}
				};
			}
		});
	}


}
