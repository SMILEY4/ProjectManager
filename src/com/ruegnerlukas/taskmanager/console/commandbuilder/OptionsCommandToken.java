package com.ruegnerlukas.taskmanager.console.commandbuilder;

import com.ruegnerlukas.simpleparser.Token;
import com.ruegnerlukas.simpleparser.expressions.AlternativeExpression;
import com.ruegnerlukas.simpleparser.expressions.Expression;
import com.ruegnerlukas.simpleparser.expressions.OptionalExpression;
import com.ruegnerlukas.simpleparser.expressions.TokenExpression;

import java.util.ArrayList;
import java.util.List;

public class OptionsCommandToken implements CommandToken {


	private final Option[] options;
	private final String name;
	private final OptionalExpression expression;




	public OptionsCommandToken(String name, Option... options) {
		this.name = name;
		this.options = options;

		if (options.length == 1 && options[0].variants.length == 1) {
			// build as ["A"]
			this.expression = new OptionalExpression(new TokenExpression(new Token(name, options[0].variants[0])));

		} else {
			// build as [ ( "A" | "B" | "C" | ... ) ]
			List<TokenExpression> tokenExprList = new ArrayList<>();
			for (Option option : options) {
				for (String variant : option.variants) {
					tokenExprList.add(new TokenExpression(new Token(name, variant)));
				}
			}

			TokenExpression[] tokenExprArray = new TokenExpression[tokenExprList.size()];
			for (int i = 0; i < tokenExprList.size(); i++) {
				tokenExprArray[i] = tokenExprList.get(i);
			}

			this.expression = new OptionalExpression(new AlternativeExpression(tokenExprArray));
		}

	}




	@Override
	public Expression getRootExpression() {
		return expression;
	}




	@Override
	public boolean hasDescription() {
		return false; // TODO
	}




	@Override
	public String getDescription() {
		return ""; // TODO from options-list
	}




	@Override
	public String asString() {
		return "[" + name + "]";
	}




	public Option[] getOptions() {
		return this.options;
	}

}
