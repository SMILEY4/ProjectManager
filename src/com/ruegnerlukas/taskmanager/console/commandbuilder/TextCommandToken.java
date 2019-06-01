package com.ruegnerlukas.taskmanager.console.commandbuilder;

import com.ruegnerlukas.simpleparser.Token;
import com.ruegnerlukas.simpleparser.expressions.Expression;
import com.ruegnerlukas.simpleparser.expressions.TokenExpression;

public class TextCommandToken implements CommandToken {


	public final String text;
	private final TokenExpression expression;




	public TextCommandToken(String text) {
		this.text = text;
		expression = new TokenExpression(new Token(text));
	}




	@Override
	public Expression getRootExpression() {
		return expression;
	}




	@Override
	public boolean hasDescription() {
		return false;
	}




	@Override
	public String getDescription() {
		return "";
	}




	@Override
	public String asString() {
		return "";
	}

}
