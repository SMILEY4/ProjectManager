package com.ruegnerlukas.taskmanager.console.commandbuilder;

import com.ruegnerlukas.simpleparser.Token;
import com.ruegnerlukas.simpleparser.Variable;
import com.ruegnerlukas.simpleparser.expressions.*;
import com.ruegnerlukas.taskmanager.console.CommandExecutor;
import com.ruegnerlukas.taskmanager.console.CommandIdentifier;

import java.util.ArrayList;
import java.util.List;

public class CommandBuilder {


	private List<String> stringList = new ArrayList<>();

	private List<Expression> expressions = new ArrayList<>();
	private String description = "";
	private CommandExecutor executor;

	private boolean identifierCompleted = false;
	private List<String> identifier = new ArrayList<>();




	public CommandBuilder text(String text) {
		if (!identifierCompleted) {
			identifier.add(text);
		}
		stringList.add(text);
		TokenExpression expr = new TokenExpression(new Token(text));
		expressions.add(expr);
		return this;
	}




	public CommandBuilder variable(String name, Class<?> type) {
		identifierCompleted = true;
		stringList.add('<' + name + ':' + type.getSimpleName() + '>');
		VariableExpression expr = new VariableExpression(new Variable(name, type));
		expressions.add(expr);
		return this;
	}




	public CommandBuilder optionalVariable(String name, Class<?> type) {
		identifierCompleted = true;
		stringList.add("[<" + name + ':' + type.getSimpleName() + ">]");
		VariableExpression exprVar = new VariableExpression(new Variable(name, type));
		OptionalExpression exprOpt = new OptionalExpression(exprVar);
		expressions.add(exprOpt);
		return this;
	}




	public CommandBuilder optional(String name, String text) {
		identifierCompleted = true;
		stringList.add('[' + name + ':' + text + ']');
		OptionalExpression expr = new OptionalExpression(new TokenExpression(new Token(name, text)));
		expressions.add(expr);
		return this;
	}




	public CommandBuilder alternative(String name, String... choices) {
		identifierCompleted = true;

		List<String> listChoices = new ArrayList<>();
		for (String s : choices) {
			listChoices.add('\"' + s + '\"');
		}
		stringList.add('<' + name + ':' + String.join(",", listChoices) + '>');

		Expression[] exprArray = new Expression[choices.length];
		for (int i = 0; i < choices.length; i++) {
			exprArray[i] = new TokenExpression(new Token(name, choices[i]));
		}

		AlternativeExpression expr = new AlternativeExpression(exprArray);
		expressions.add(expr);
		return this;
	}




	public CommandBuilder optionalAlternative(String name, String... choices) {
		identifierCompleted = true;

		List<String> listChoices = new ArrayList<>();
		for (String s : choices) {
			listChoices.add('\"' + s + '\"');
		}
		stringList.add("[<" + name + ':' + String.join(",", listChoices) + ">]");

		Expression[] exprArray = new Expression[choices.length];
		for (int i = 0; i < choices.length; i++) {
			exprArray[i] = new TokenExpression(new Token(name, choices[i]));
		}

		AlternativeExpression exprAlt = new AlternativeExpression(exprArray);
		OptionalExpression exprOpt = new OptionalExpression(exprAlt);

		expressions.add(exprOpt);
		return this;
	}




	public CommandBuilder setDescription(String description) {
		this.description = description;
		return this;
	}




	public CommandBuilder setExecutor(CommandExecutor executor) {
		this.executor = executor;
		return this;
	}




	public Command create() {

		Expression rootExpression;

		if (expressions.size() == 1) {
			rootExpression = expressions.get(0);
		} else {
			Expression[] exprArray = new Expression[expressions.size()];
			for (int i = 0; i < expressions.size(); i++) {
				exprArray[i] = expressions.get(i);
			}
			rootExpression = new SequenceExpression(exprArray);
		}

		Command cmd = new Command(String.join(" ", stringList), description, new CommandIdentifier(identifier), rootExpression);
		cmd.setExecutor(executor);

		reset();

		return cmd;
	}




	public void reset() {
		expressions.clear();
		description = "";
		executor = null;
		identifierCompleted = false;
		identifier.clear();
	}


}
