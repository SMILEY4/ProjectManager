package com.ruegnerlukas.taskmanager.console.commandbuilder;

import com.ruegnerlukas.simpleparser.Variable;
import com.ruegnerlukas.simpleparser.expressions.Expression;
import com.ruegnerlukas.simpleparser.expressions.VariableExpression;

public class VariableCommandToken implements CommandToken {


	private final VariableExpression expression;
	private final String description;




	public VariableCommandToken(String varname, Class<?> datatype) {
		this(varname, datatype, null);
	}




	public VariableCommandToken(String varname, Class<?> datatype, String description) {
		this.expression = new VariableExpression(new Variable(varname, datatype));
		this.description = description;
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
		return "<" + expression.variable.varname + ":" + expression.variable.datatype.getSimpleName() + ">";
	}

}
