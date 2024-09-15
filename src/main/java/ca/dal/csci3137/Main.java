/**
 * @author Arash Tashakori - B00872075 - ArTash@dal.ca
 */
package ca.dal.csci3137;

import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static ca.dal.csci3137.SchemeCore.*;
import static java.util.Map.*;


public class Main {
    /*
    Global referencing environment for mapping the Scheme symbols to the procedure that they invoke
    This part is from Assignment 3 and the instructions of Assignment 4
     */
    public static final Map<Symbol, Object> GLOBAL_REFERENCING_ENVIRONMENT = Map.ofEntries (
            entry(symbol("quote"), Procedure.QUOTE),
            entry(symbol("define"), Procedure.DEFINE),

            entry(symbol("car"), Procedure.CAR),
            entry(symbol("cdr"), Procedure.CDR),
            entry(symbol("length"), Procedure.LENGTH),

            entry(symbol("cons"), Procedure.CONS),
            entry(symbol("+"), Procedure.ADD),
            entry(symbol("-"), Procedure.SUBTRACT),
            entry(symbol("*"), Procedure.MULTIPLY),
            entry(symbol("quotient"), Procedure.QUOTIENT),
            entry(symbol("remainder"), Procedure.REMAINDER),

            entry(symbol("list"), Procedure.LIST),

            entry(symbol("null?"), Procedure.NULL_P),
            entry(symbol("pair?"), Procedure.PAIR_P),
            entry(symbol("list?"), Procedure.LIST_P),
            entry(symbol("integer?"), Procedure.INTEGER_P),
            entry(symbol("not"), Procedure.NOT),
            entry(symbol("and"), Procedure.AND),
            entry(symbol("or"), Procedure.OR),
            entry(symbol("="), Procedure.COMPARATOR_EQUAL_TO),
            entry(symbol("<"), Procedure.COMPARATOR_LESS_THAN),
            entry(symbol(">"), Procedure.COMPARATOR_GREATER_THAN),
            entry(symbol("<="), Procedure.COMPARATOR_LESS_THAN_OR_EQUAL_TO),
            entry(symbol(">="), Procedure.COMPARATOR_GREATER_THAN_OR_EQUAL_TO),
            entry(symbol("equal?"), Procedure.EQUAL_P),
            entry(symbol("if"), Procedure.IF),
            entry(symbol("let"), Procedure.LET),
            entry(symbol("lambda"), Procedure.LAMBDA)
    );


    /*
    This method implements the prompt and handling of user input and passing them to the tokenizer and parser

    This method was initially written by me but I changed it after Assignment 3. The current method is taken from the
    instructor's solution for Assignment 3.
     */
    private static void repl(Map<Symbol, Object> referencingEnvironment) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        String input = scanner.nextLine();
        List<String> tokens = Tokenizer.tokenize(input);
        Object ast = Parser.parse(tokens);

        if (Objects.equals(ast, SchemeCore.symbol("exit"))) return;
        try {
            Object result = eval(ast, referencingEnvironment);
            System.out.println(result);
        } catch (Throwable t) {
            System.err.println("While evaluating: " + Objects.requireNonNull(ast));
            t.printStackTrace();
        } finally {
            repl(referencingEnvironment);
        }

    }

    /*
    This method loads, tokenizes and evaluates the content of the core.scm file given the referencing environment
     */
    private static void loadTheCoreSCMFile (Map<Symbol, Object> referencingEnvironment) throws IOException {
        // Read the contents of core.scm
        String coreScmFilePath = "src/main/resources/core.scm";
        String coreScmContent = new String(Files.readAllBytes(Paths.get(coreScmFilePath)));

        // Tokenize contents
        List<String> tokens = Tokenizer.tokenize(coreScmContent);

        // Parse and eval each tokenized expression
        while (!tokens.isEmpty()) {
            Object ast = Parser.parse(tokens);
            eval(ast, referencingEnvironment);
        }
    }

    public static void main(String[] args) throws IOException {
        Map<Symbol, Object> environment = new HashMap<>(GLOBAL_REFERENCING_ENVIRONMENT);

        // Load core.scm before starting the REPL
        loadTheCoreSCMFile (environment);

        repl(environment);
    }
}
