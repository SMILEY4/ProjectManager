package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.AndFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.OrFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogicManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FilterNode {


	public FilterCriteria criteria;
	public List<FilterNode> children = new ArrayList<>();




	public FilterNode(FilterCriteria criteria) {
		this.criteria = criteria;
	}




	public FilterNode(FilterCriteria criteria, List<FilterNode> children) {
		this.criteria = criteria;
		this.children.addAll(children);
	}




	public FilterNode(FilterCriteria criteria, FilterNode... children) {
		this.criteria = criteria;
		this.children.addAll(Arrays.asList(children));
	}




	public void expand() {
		if (this.criteria.type == FilterCriteria.CriteriaType.OR) {
			for (FilterCriteria crit : ((OrFilterCriteria) this.criteria).subCriteria) {
				FilterNode node = new FilterNode(crit);
				node.expand();
				this.children.add(node);
			}
		}
		if (this.criteria.type == FilterCriteria.CriteriaType.AND) {
			for (FilterCriteria crit : ((AndFilterCriteria) this.criteria).subCriteria) {
				FilterNode node = new FilterNode(crit);
				node.expand();
				this.children.add(node);
			}
		}
	}




	public boolean matches(Task task) {
		if (this.criteria.type == FilterCriteria.CriteriaType.TERMINAL) {
			if(AttributeLogicManager.isValidFilterOperation(task, (TerminalFilterCriteria) criteria)) {
				return AttributeLogicManager.matchesFilter(task, (TerminalFilterCriteria) criteria);
			} else {
				return false;
			}
		}
		if (this.criteria.type == FilterCriteria.CriteriaType.OR) {
			for (FilterNode node : this.children) {
				if (node.matches(task)) {
					return true;
				}
			}
		}
		if (this.criteria.type == FilterCriteria.CriteriaType.AND) {
			for (FilterNode node : this.children) {
				if (!node.matches(task)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}