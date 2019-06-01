package com.ruegnerlukas.taskmanager.console.commandbuilder;

import com.ruegnerlukas.simpleparser.expressions.Expression;
import com.ruegnerlukas.taskmanager.console.CommandExecutor;
import com.ruegnerlukas.taskmanager.console.CommandIdentifier;

public class Command {


	public final String string;
	public final String description;
	public final CommandIdentifier identifier;

	private CommandExecutor executor;
	private final Expression expression;




	public Command(String string, String description, CommandIdentifier identifier, Expression expression) {
		this.string = string;
		this.description = description;
		this.expression = expression;
		this.identifier = identifier;
	}




	public Expression getRootExpression() {
		return expression;
	}




	public Command setExecutor(CommandExecutor executor) {
		this.executor = executor;
		return this;
	}




	public CommandExecutor getExecutor() {
		return this.executor;
	}


}
