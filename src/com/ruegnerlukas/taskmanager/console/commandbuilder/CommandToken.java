package com.ruegnerlukas.taskmanager.console.commandbuilder;

import com.ruegnerlukas.simpleparser.expressions.Expression;

public interface CommandToken {

	Expression getRootExpression();

	boolean hasDescription();

	String getDescription();

	String asString();

}
