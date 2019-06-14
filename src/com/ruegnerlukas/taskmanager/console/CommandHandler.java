package com.ruegnerlukas.taskmanager.console;


import com.ruegnerlukas.simpleparser.ErrorNode;
import com.ruegnerlukas.simpleparser.Node;
import com.ruegnerlukas.simpleparser.Token;
import com.ruegnerlukas.simpleparser.Variable;
import com.ruegnerlukas.simpleparser.errors.ErrorType;
import com.ruegnerlukas.simpleparser.expressions.Expression;
import com.ruegnerlukas.simpleparser.expressions.ExpressionType;
import com.ruegnerlukas.simpleparser.expressions.TokenExpression;
import com.ruegnerlukas.simpleparser.expressions.VariableExpression;
import com.ruegnerlukas.simpleparser.grammar.Grammar;
import com.ruegnerlukas.simpleparser.grammar.GrammarBuilder;
import com.ruegnerlukas.simpleparser.parser.ParserResult;
import com.ruegnerlukas.simpleparser.parser.ParserResultError;
import com.ruegnerlukas.simpleparser.parser.ParserResultMatch;
import com.ruegnerlukas.simpleparser.parser.StringParser;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandresults.FailedCommandResult;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.console.commands.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandHandler {


	private static Grammar grammar;
	private static StringParser parser;
	private static Map<CommandIdentifier, Command> commandMap;




	public static void create() {
		List<Command> commands = createCommandList();
		grammar = createGrammar(commands);
		parser = new StringParser(grammar);

		commandMap = new HashMap<>();
		for (Command cmd : commands) {
			commandMap.put(cmd.identifier, cmd);
		}
	}




	private static Grammar createGrammar(List<Command> commands) {

		Expression[] cmdExprArray = new Expression[commands.size()];
		for (int i = 0; i < commands.size(); i++) {
			cmdExprArray[i] = commands.get(i).getRootExpression();
		}

		GrammarBuilder builder = new GrammarBuilder();
		builder.defineRootNonTerminal("ROOT", builder.alternative(cmdExprArray));

		return builder.get();
	}




	private static List<Command> createCommandList() {
		List<Command> commands = new ArrayList<>();

		commands.add(CommandProjectRename.create());
		commands.add(CommandProjectLockAttributes.create());
		commands.add(CommandProjectSetIDCounter.create());
		commands.add(CommandProjectGetName.create());
		commands.add(CommandProjectCreate.create());
		commands.add(CommandProjectClose.create());

		commands.add(CommandAttributeRename.create());
		commands.add(CommandAttributeInfo.create());
		commands.add(CommandAttributeType.create());
		commands.add(CommandAttributeSetValue.create());

		commands.add(CommandAttributesRemove.create());
		commands.add(CommandAttributesCreate.create());
		commands.add(CommandAttributesList.create());

		commands.add(CommandTaskInfo.create());
		commands.add(CommandTaskSetValue.create());

		commands.add(CommandTasksList.create());
		commands.add(CommandTasksRemove.create());
		commands.add(CommandTasksCreate.create());

		commands.add(CommandDataSimExternalValueChange.create());
		commands.add(CommandDataIdentifiers.create());

		commands.add(CommandHelp.create());

		return commands;
	}




	public static List<Command> getCommands() {
		return new ArrayList<>(commandMap.values());
	}




	private static Command findCommand(ParserResult result) {

		List<Node> list = result.getRoot().collectLeafNodes();

		List<String> identifierList = new ArrayList<>();
		for (Node node : list) {
			if (!(node instanceof ErrorNode)) {
				Expression expression = node.expression;
				if (expression.getType() == ExpressionType.TOKEN) {
					TokenExpression tokenExpression = (TokenExpression) expression;
					if(tokenExpression.token.getName().equalsIgnoreCase("cmd_identifier")) {
						identifierList.add(tokenExpression.token.getSymbol());
					}
				} else {
					break;
				}
			} else {
				break;
			}
		}

		CommandIdentifier identifier = new CommandIdentifier(identifierList);
		return commandMap.get(identifier);

	}




	private static Map<String, Object> collectParameters(ParserResultMatch result) {

		Map<String, Object> map = new HashMap<>();

		List<Node> leafs = result.getRoot().collectLeafNodes();
		for (Node node : leafs) {
			if (node.expression.getType() == ExpressionType.TOKEN) {
				Token token = ((TokenExpression) node.expression).token;
				map.put(token.getName(), token.getSymbol());
			}
			if (node.expression.getType() == ExpressionType.VARIABLE) {
				Variable var = ((VariableExpression) node.expression).variable;
				map.put(var.varname, var.value);
			}
		}

		return map;
	}




	private static int getErrorIndex(ParserResultError result) {
		List<List<Node>> buckets = result.getResultList();
		for (List<Node> bucket : buckets) {
			for (Node node : bucket) {
				if (node instanceof ErrorNode) {
					ErrorNode errorNode = (ErrorNode) node;
					return errorNode.index;
				}
			}

		}
		return 0;
	}




	private static List<String> collectErrors(ParserResultError result) {
		List<List<Node>> buckets = result.getResultList();

		List<String> strErrors = new ArrayList<>();

		for (List<Node> bucket : buckets) {

			boolean wasErrorBucket = false;

			for (Node node : bucket) {
				if (node instanceof ErrorNode) {
					wasErrorBucket = true;
					ErrorNode errorNode = (ErrorNode) node;

					String strNode = "?";
					if (errorNode.expression instanceof TokenExpression) {
						strNode = ((TokenExpression) errorNode.expression).token.getSymbol();
					}
					if (errorNode.expression instanceof VariableExpression) {
						Variable variable = ((VariableExpression) errorNode.expression).variable;
						strNode = "<" + variable.varname + ":" + variable.datatype.getSimpleName() + ">";
					}

					if (errorNode.error == ErrorType.ILLEGAL_SYMBOL) {
						strErrors.add("IllegalSymbol@" + errorNode.index + ": Illegal Symbol.");
					}
					if (errorNode.error == ErrorType.UNEXPECTED_SYMBOL) {
						strErrors.add("UnexpectedSymbol@" + errorNode.index + ": Expected: \"" + strNode + "\".");
					}
					if (errorNode.error == ErrorType.UNEXPECTED_END_OF_INPUT) {
						strErrors.add("UnexpectedEndOfInput@" + errorNode.index + ": Expected: \"" + strNode + "\".");
					}
					if (errorNode.error == ErrorType.SYMBOLS_REMAINING) {
						strErrors.add("SymbolsRemaining@" + errorNode.index + ": \"" + strNode + "\".");
					}
					if (errorNode.error == ErrorType.ERROR) {
						strErrors.add("Error@" + errorNode.index + ": Error.");
					}
					if (errorNode.error == ErrorType.INTERNAL_ERROR) {
						strErrors.add("InternalError@" + errorNode.index + ": Internal Error.");
					}

				}
			}

			if (wasErrorBucket) {
				break;
			}

		}

		return strErrors;
	}




	public static boolean onCommand(String str) {

		ParserResult result = parser.parse(str, true, true, true);
		Command cmd = findCommand(result);
		if (cmd != null) {
			if (cmd.getExecutor() != null) {
				if (result.failed()) {
					FailedCommandResult cmdResult = new FailedCommandResult(str, cmd, collectErrors((ParserResultError) result), getErrorIndex((ParserResultError) result));
					cmd.getExecutor().onFailedCommand(cmdResult);
				} else {
					SuccessfulCommandResult cmdResult = new SuccessfulCommandResult(str, cmd, collectParameters((ParserResultMatch) result));
					cmd.getExecutor().onCommand(cmdResult);
				}
				return true;
			}
		} else {
			ConsoleWindowHandler.print(Color.RED, "The command \"" + str + "\" could not be found.");
		}

		return false;
	}






}
