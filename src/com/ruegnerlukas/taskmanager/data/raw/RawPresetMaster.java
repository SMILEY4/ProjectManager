package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.MasterPreset;

public class RawPresetMaster {


	public String presetName;
	public String filterPreset;
	public String groupPreset;
	public String sortPreset;




	public static RawPresetMaster toRaw(String presetName, MasterPreset data) {
		RawPresetMaster raw = new RawPresetMaster();
		raw.presetName = presetName;
		raw.filterPreset = data.filterPreset;
		raw.groupPreset = data.groupPreset;
		raw.sortPreset = data.sortPreset;
		return raw;
	}


}
