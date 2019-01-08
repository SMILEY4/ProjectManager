package com.ruegnerlukas.taskmanager.logic.services;

import java.time.LocalDate;
import java.util.ArrayList;

import com.ruegnerlukas.taskmanager.logic.data.Project;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute.Type;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute.Variable;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomBoolAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomChoiceAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomDateAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomNumberAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomTextAttributeData;
import com.ruegnerlukas.taskmanager.logic.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.CustomAttributeChangedNameEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.CustomAttributeChangedNameRejection;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.CustomAttributeChangedTypeEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.CustomAttributeChangedVariableEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.CustomAttributeCreatedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.CustomAttributeCreatedRejection;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.CustomAttributeDeletedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.EventCause;

public class CustomAttributeService {

	
	public boolean createCustomAttribute(String name) {
		if(DataService.project.getProject() == null) {
			return false;
		} else {
			Project project = DataService.project.getProject();
			CustomAttribute attrib = new CustomAttribute(name, Type.TEXT);
			if(project.customAttributes.containsKey(name)) {
				EventManager.fireEvent(new CustomAttributeCreatedRejection(attrib, EventCause.NAME_EXISTS, this));
				return false;
			} else {
				project.customAttributes.put(attrib.name, attrib);
				EventManager.fireEvent(new CustomAttributeCreatedEvent(attrib, this));
				return true;
			}
		}
	}
	
	
	
	
	public boolean deleteCustomAttribute(CustomAttribute attrib) {
		if(attrib != null && DataService.project.getProject() != null) {
			// TODO remove from all TaskCards
			// TODO ask for confirmation if this attribute of any task was edited
			Project project = DataService.project.getProject();
			project.customAttributes.remove(attrib.name);
			EventManager.fireEvent(new CustomAttributeDeletedEvent(attrib, this));
			return true;
		} else {
			return false;
		}
	}
	
	
	
	
	public int getNumAttributes() {
		if(DataService.project.getProject() != null) {
			return DataService.project.getProject().customAttributes.size();
		} else {
			return 0;
		}
	}
	
	
	
	
	public CustomAttribute getByName(String name) {
		if(DataService.project.getProject() == null) {
			return null;
		} else {
			Project project = DataService.project.getProject();
			return project.customAttributes.get(name);
		}
	}
	
	
	
	
	public boolean renameCustomAttribute(CustomAttribute attrib, String newName) {
		if(attrib != null && DataService.project.getProject() != null) {
			if(attrib.name.equals(newName)) {
				return true;
			} else {
				Project project = DataService.project.getProject();
				if(project.customAttributes.containsKey(newName)) {
					EventManager.fireEvent(new CustomAttributeChangedNameRejection(attrib, attrib.name, newName, EventCause.NAME_EXISTS, this));
					return false;
				} else {
					String oldName = attrib.name;
					project.customAttributes.remove(attrib.name);
					attrib.name = newName;
					project.customAttributes.put(attrib.name, attrib);
					EventManager.fireEvent(new CustomAttributeChangedNameEvent(attrib, oldName, attrib.name, this));
					return true;
				}
			}
		} else {
			return false;
		}
	}
	
	
	
	
	public boolean setCustomAttributeType(CustomAttribute attrib, Type type) {
		if(attrib != null && DataService.project.getProject() != null) {
			if(attrib.data.getType() != type) {
				Type oldType = attrib.data.getType();
				attrib.createNewData(type);
				EventManager.fireEvent(new CustomAttributeChangedTypeEvent(attrib, oldType, type, this));
			}
			return true;
		} else {
			return false;
		}
	}
	
	
	
	
	public boolean setCustomAttributeVariable(CustomAttribute attrib, Variable var, Object value) {
		if(attrib != null && DataService.project.getProject() != null) {
			
			if(attrib.data.getType() == Type.TEXT) {
				CustomTextAttributeData data = (CustomTextAttributeData)attrib.data;
				if(var == Variable.TEXT_CHAR_LIMIT) {
					Object oldValue = new Integer(data.charLimit);
					data.charLimit = (Integer)value;
					EventManager.fireEvent(new CustomAttributeChangedVariableEvent(attrib, var, oldValue, value, this));
					if(data.defaultValue.length() > data.charLimit) {
						setCustomAttributeVariable(attrib, Variable.TEXT_DEFAULT, data.defaultValue);
					}
					return true;
				}
				if(var == Variable.TEXT_USE_DEFAULT) {
					Object oldValue = new Boolean(data.useDefault);
					data.useDefault = (Boolean)value;
					EventManager.fireEvent(new CustomAttributeChangedVariableEvent(attrib, var, oldValue, value, this));
					return true;
				}
				if(var == Variable.TEXT_DEFAULT) {
					Object oldValue = data.defaultValue;
					data.defaultValue = ((String)value).length() > data.charLimit ? ((String)value).substring(0, data.charLimit) : (String)value;
					EventManager.fireEvent(new CustomAttributeChangedVariableEvent(attrib, var, oldValue, value, this));
					return true;
				}
			}
			
			if(attrib.data.getType() == Type.NUMBER) {
				CustomNumberAttributeData data = (CustomNumberAttributeData)attrib.data;
				if(var == Variable.NUMBER_DEC_PLACES) {
					Object oldValue = new Integer(data.decPlaces);
					data.decPlaces = (Integer)value;
					EventManager.fireEvent(new CustomAttributeChangedVariableEvent(attrib, var, oldValue, value, this));
					return true;
				}
				if(var == Variable.NUMBER_MIN) {
					Object oldValue = new Double(data.min);
					data.min = (Double)value;
					EventManager.fireEvent(new CustomAttributeChangedVariableEvent(attrib, var, oldValue, value, this));
					return true;
				}
				if(var == Variable.NUMBER_MAX) {
					Object oldValue = new Double(data.max);
					data.max = (Double)value;
					EventManager.fireEvent(new CustomAttributeChangedVariableEvent(attrib, var, oldValue, value, this));
					return true;
				}
				if(var == Variable.NUMBER_USE_DEFAULT) {
					Object oldValue = new Boolean(data.useDefault);
					data.useDefault = (Boolean)value;
					EventManager.fireEvent(new CustomAttributeChangedVariableEvent(attrib, var, oldValue, value, this));
					return true;
				}
				if(var == Variable.NUMBER_DEFAULT) {
					Object oldValue = data.defaultValue;
					data.defaultValue = (Double)value;
					EventManager.fireEvent(new CustomAttributeChangedVariableEvent(attrib, var, oldValue, value, this));
					return true;
				}
			}
			
			if(attrib.data.getType() == Type.BOOLEAN) {
				CustomBoolAttributeData data = (CustomBoolAttributeData)attrib.data;
				if(var == Variable.BOOLEAN_USE_DEFAULT) {
					Object oldValue = new Boolean(data.useDefault);
					data.useDefault = (Boolean)value;
					EventManager.fireEvent(new CustomAttributeChangedVariableEvent(attrib, var, oldValue, value, this));
					return true;
				}
				if(var == Variable.BOOLEAN_DEFAULT) {
					Object oldValue = data.defaultValue;
					data.defaultValue = (Boolean)value;
					EventManager.fireEvent(new CustomAttributeChangedVariableEvent(attrib, var, oldValue, value, this));
					return true;
				}
			}
			
			if(attrib.data.getType() == Type.DATE) {
				CustomDateAttributeData data = (CustomDateAttributeData)attrib.data;
				if(var == Variable.DATE_USE_DEFAULT) {
					Object oldValue = new Boolean(data.useDefault);
					data.useDefault = (Boolean)value;
					EventManager.fireEvent(new CustomAttributeChangedVariableEvent(attrib, var, oldValue, value, this));
					return true;
				}
				if(var == Variable.DATE_DEFAULT) {
					Object oldValue = data.defaultValue;
					data.defaultValue = (LocalDate)value;
					EventManager.fireEvent(new CustomAttributeChangedVariableEvent(attrib, var, oldValue, value, this));
					return true;
				}
			}
			
			if(attrib.data.getType() == Type.CHOICE) {
				CustomChoiceAttributeData data = (CustomChoiceAttributeData)attrib.data;
				if(var == Variable.CHOICE_VALUES) {
					Object oldValue = data.values;
					data.values = (ArrayList<String>)value;
					EventManager.fireEvent(new CustomAttributeChangedVariableEvent(attrib, var, oldValue, value, this));
					return true;
				}
				if(var == Variable.CHOICE_USE_DEFAULT) {
					Object oldValue = new Boolean(data.useDefault);
					data.useDefault = (Boolean)value;
					EventManager.fireEvent(new CustomAttributeChangedVariableEvent(attrib, var, oldValue, value, this));
					return true;
				}
				if(var == Variable.CHOICE_DEFAULT) {
					Object oldValue = data.defaultValue;
					data.defaultValue = (String)value;
					EventManager.fireEvent(new CustomAttributeChangedVariableEvent(attrib, var, oldValue, value, this));
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}
	
	
}
