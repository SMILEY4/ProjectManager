package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.*;
import com.ruegnerlukas.taskmanager.data.Preset;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.groups.AttributeGroupData;

import java.util.ArrayList;
import java.util.List;

public class PresetLogic {


	public void getPresets(Request<List<Preset>> request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			request.respond(new Response<>(Response.State.SUCCESS, new ArrayList<>(project.presets.values())));
		}
	}




	public void getPreset(String name, Request<Preset> request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (project.presets.containsKey(name)) {
				request.respond(new Response<>(Response.State.SUCCESS, project.presets.get(name)));
			} else {
				request.respond(new Response<>(Response.State.FAIL, "No preset with name '" + name + "' found"));
			}
		}
	}




	public void saveAsPreset(String name) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (project.presets.containsKey(name)) {
				EventManager.fireEvent(new PresetSaveRejection(name, EventCause.NOT_UNIQUE, this));
			} else {
				Preset preset = new Preset(name);
				preset.filterCriteria.addAll(project.filterCriteria);
				preset.groupData.attributes.addAll(project.attribGroupData.attributes);
				preset.groupData.useCustomHeader = project.attribGroupData.useCustomHeader;
				preset.groupData.customHeader = project.attribGroupData.customHeader;
				preset.sortElements.addAll(project.sortElements);
				project.presets.put(name, preset);
				EventManager.fireEvent(new PresetSavedEvent(preset, this));
			}
		}
	}




	public void deletePreset(String name) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (project.presets.containsKey(name)) {
				Preset preset = project.presets.get(name);
				project.presets.remove(name);
				EventManager.fireEvent(new PresetDeletedEvent(preset, this));
			} else {
				EventManager.fireEvent(new PresetDeleteRejection(name, EventCause.NOT_EXISTS, this));
			}
		}
	}




	public void loadPreset(String nameFilter, String nameGroup, String nameSort) {
		Project project = Logic.project.getProject();
		if (project != null) {

			project.filterCriteria.clear();
			if(nameFilter != null) {
				project.filterCriteria.addAll(project.savedFilters.get(nameFilter));
			}

			if(nameGroup != null) {
				project.attribGroupData = project.savedGroupOrders.get(nameGroup);
			} else {
				project.attribGroupData = new AttributeGroupData();
			}

			project.sortElements.clear();
			if(nameSort != null) {
				project.sortElements.addAll(project.savedSortElements.get(nameSort));
			}

			EventManager.fireEvent(new PresetLoadEvent(new Preset("noname", true), this));
		}
	}




	public void loadPreset(String name) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (project.presets.containsKey(name)) {
				Preset preset = project.presets.get(name);
				project.filterCriteria.clear();
				project.filterCriteria.addAll(preset.filterCriteria);
				project.attribGroupData.attributes.clear();
				project.attribGroupData.attributes.addAll(preset.groupData.attributes);
				project.attribGroupData.useCustomHeader = preset.groupData.useCustomHeader;
				project.attribGroupData.customHeader = preset.groupData.customHeader;
				project.sortElements.clear();
				project.sortElements.addAll(preset.sortElements);
				EventManager.fireEvent(new PresetLoadEvent(preset, this));
			} else {
				EventManager.fireEvent(new PresetLoadRejection(name, EventCause.NOT_EXISTS, this));
			}
		}
	}


}
