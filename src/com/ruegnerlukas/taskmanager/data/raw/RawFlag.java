package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskFlag;

public class RawFlag {


	public String name;
	public TaskFlag.FlagColor color;




	public static RawFlag toRaw(TaskFlag flag) {
		RawFlag raw = new RawFlag();
		raw.name = flag.name.get();
		raw.color = flag.color.get();
		return raw;
	}




	public static TaskFlag fromRaw(RawFlag value) {
		return new TaskFlag(value.name, value.color);
	}

}
