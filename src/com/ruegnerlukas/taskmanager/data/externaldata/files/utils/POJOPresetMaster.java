package com.ruegnerlukas.taskmanager.data.externaldata.files.utils;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.MasterPreset;

public class POJOPresetMaster {


	public String presetName;
	public String filterPreset;
	public String groupPreset;
	public String sortPreset;




	public POJOPresetMaster(String presetName, MasterPreset data) {
		this.presetName = presetName;
		this.filterPreset = data.filterPreset;
		this.groupPreset = data.groupPreset;
		this.sortPreset = data.sortPreset;
	}

}
