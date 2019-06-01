package com.ruegnerlukas.taskmanager.console;

import java.util.ArrayList;
import java.util.List;

public class CommandIdentifier {


	public final List<String> identifier;




	public CommandIdentifier(List<String> identifier) {
		this.identifier = new ArrayList<>();
		this.identifier.addAll(identifier);
	}




	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CommandIdentifier that = (CommandIdentifier) o;
		return identifier != null ? identifier.equals(that.identifier) : that.identifier == null;

	}




	@Override
	public int hashCode() {
		return identifier != null ? identifier.hashCode() : 0;
	}

}
