package com.ruegnerlukas.taskmanager.console.commandbuilder;

import com.ruegnerlukas.simpleparser.Variable;
import com.ruegnerlukas.simpleparser.expressions.Expression;
import com.ruegnerlukas.simpleparser.expressions.OptionalExpression;
import com.ruegnerlukas.simpleparser.expressions.VariableExpression;

public class OptionalVariableCommandToken implements CommandToken {


	private final Variable variable;
	private final OptionalExpression expression;
	private final String description;




	public OptionalVariableCommandToken(String varname, Class<?> datatype) {
		this(varname, datatype, null);
	}




	public OptionalVariableCommandToken(String varname, Class<?> datatype, String description) {
		this.variable = new Variable(varname, datatype);
		this.expression = new OptionalExpression(new VariableExpression(variable));
		this.description = description;
	}




	public Variable getVariable() {
		return variable;
	}




	@Override
	public Expression getRootExpression() {
		return expression;
	}




	@Override
	public boolean hasDescription() {
		return description != null;
	}




	@Override
	public String getDescription() {
		return description != null ? description : "";
	}




	@Override
	public String asString() {
		return "[<" + variable.varname + ":" + variable.datatype.getSimpleName() + ">]";
	}

}
