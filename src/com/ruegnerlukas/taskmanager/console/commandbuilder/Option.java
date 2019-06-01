package com.ruegnerlukas.taskmanager.console.commandbuilder;

public class Option {


	public final String optionName;
	public final String[] variants;
	public final String description;




	public Option(String optionName, String description, String... variants) {
		this.optionName = optionName;
		this.description = description;
		this.variants = variants;
	}


}
