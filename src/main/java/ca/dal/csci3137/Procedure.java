
/**
 * @author Arash Tashakori - B00872075 - ArTash@dal.ca
 */
package ca.dal.csci3137;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import static ca.dal.csci3137.SchemeCore.*;

public interface Procedure {

    static Procedure unaryProcedure(UnaryOperator<Object> op) {
        return (arguments, referencingEnvironment) -> {
            if(1 != length(arguments)) throw new RuntimeException("unary procedure received wrong number of arguments.");
            Object argument = eval(car(arguments), referencingEnvironment);

            return op.apply(argument);
        };
    }

    static Procedure binaryProcedure(BinaryOperator<Object> op) {
        return (arguments, referencingEnvironment) -> {
            if(2 != length(arguments)) throw new RuntimeException("binary procedure received wrong number of arguments.");
            Object argument1 = eval(car(arguments), referencingEnvironment);
            Object argument2 = eval(car(cdr(arguments)), referencingEnvironment);

            return op.apply(argument1, argument2);
        };
    }


    Procedure QUOTE = (arguments, referencingEnvironment) -> {
        if(1 != length(arguments)) throw new RuntimeException("quote received wrong number of arguments.");
        return car(arguments);
    };


    Procedure DEFINE = (arguments, referencingEnvironment) -> {
        if(2 != length(arguments)) throw new RuntimeException("define received wrong number of arguments.");
        Object symbol = car(arguments);
        if(!(symbol instanceof Symbol)) throw new RuntimeException(symbol + " is not a symbol");
        if(referencingEnvironment.containsKey(symbol)) throw new RuntimeException("symbol " + symbol + " already defined.");
        Object value = eval(car(cdr(arguments)), referencingEnvironment);
        referencingEnvironment.put((Symbol) symbol, value);
        return null;
    };


    Procedure CAR = unaryProcedure(SchemeCore::car);
    Procedure CDR = unaryProcedure(SchemeCore::cdr);
    Procedure LENGTH = unaryProcedure((a) -> BigInteger.valueOf(length(a)));
    Procedure CONS = binaryProcedure(SchemeCore::cons);
    Procedure ADD = binaryProcedure((a, b) -> ((BigInteger) a).add((BigInteger) b));
    Procedure SUBTRACT = binaryProcedure((a, b) -> ((BigInteger) a).subtract((BigInteger) b));
    Procedure MULTIPLY = binaryProcedure((a, b) -> ((BigInteger) a).multiply((BigInteger) b));
    Procedure QUOTIENT = binaryProcedure((a, b) -> ((BigInteger) a).divide((BigInteger) b));
    Procedure REMAINDER = binaryProcedure((a, b) -> ((BigInteger) a).remainder((BigInteger) b));


    Procedure LIST = new Procedure() {
        Pair evaluateElements(Pair elements, Map<Symbol, Object> referencingEnvironment) {
            return elements == nil ? nil : cons(eval(car(elements), referencingEnvironment), evaluateElements((Pair) cdr(elements), referencingEnvironment));
        }

        @Override
        public Object apply(Pair arguments, Map<Symbol, Object> referencingEnvironment) {
            return evaluateElements(arguments, referencingEnvironment);
        }
    };

    //If the argument is the empty pair
    Procedure NULL_P = unaryProcedure(arg -> arg == SchemeCore.nil);

    //If the argument is a non-empty pair
    Procedure PAIR_P = unaryProcedure(arg -> arg instanceof Pair && arg != SchemeCore.nil);

    //If the argument is a proper list
    Procedure LIST_P = unaryProcedure(SchemeCore::isList);

    //If the argument is an integer (BigInteger) Type
    Procedure INTEGER_P = unaryProcedure(arg -> arg instanceof BigInteger);

    //Return true when given false, returns false otherwise
    Procedure NOT = unaryProcedure(arg -> arg == Boolean.FALSE);

    //return the result of AND operation on arguments given
    Procedure AND = (arguments, referencingEnvironment) ->{
        Object firstArg = eval(car(arguments), referencingEnvironment);
        if (firstArg == Boolean.FALSE) {
            return Boolean.FALSE;
        } else {
            Object secondArg = eval(car(cdr(arguments)), referencingEnvironment);
            return secondArg;
        }
    };

    //return the result of OR operation on arguments given
    Procedure OR = (arguments, referencingEnvironment) ->{
        Object firstArg = eval(car(arguments), referencingEnvironment);
        //If first argument is true it should always return true
        if (firstArg != Boolean.FALSE) {
            return Boolean.TRUE;
        } else {    //Otherwise the answer would be the value of the second argument
            Object secondArg = eval(car(cdr(arguments)), referencingEnvironment);
            return secondArg;
        }
    };

    //Returns the result of Object a == Object b. Throws exception if either of the given objects is not an integer
    Procedure COMPARATOR_EQUAL_TO = binaryProcedure((a, b) -> {
        makeSureItIsBigInteger(a, b);
        return ((BigInteger) a).compareTo((BigInteger) b) == 0;
    });

    //Returns the result of Object a < Object b. Throws exception if either of the given objects is not an integer
    Procedure COMPARATOR_LESS_THAN = binaryProcedure((a, b) -> {
        makeSureItIsBigInteger (a, b);
        return ((BigInteger) a).compareTo((BigInteger) b) < 0;
    });

    //Returns the result of Object a > Object b. Throws exception if either of the given objects is not an integer
    Procedure COMPARATOR_GREATER_THAN = binaryProcedure((a, b) -> {
        makeSureItIsBigInteger(a, b);
        return ((BigInteger) a).compareTo((BigInteger) b) > 0;
    });

    //Returns the result of Object a <= Object b. Throws exception if either of the given objects is not an integer
    Procedure COMPARATOR_LESS_THAN_OR_EQUAL_TO = binaryProcedure((a, b) -> {
        makeSureItIsBigInteger (a, b);
        return ((BigInteger) a).compareTo((BigInteger) b) <= 0;
    });

    //Returns the result of Object a >= Object b. Throws exception if either of the given objects is not an integer
    Procedure COMPARATOR_GREATER_THAN_OR_EQUAL_TO = binaryProcedure((a, b) -> {
        makeSureItIsBigInteger (a, b);
        return ((BigInteger) a).compareTo((BigInteger) b) >= 0;
    });

    //Returns true if the given objects are the same and false otherwise
    Procedure EQUAL_P = binaryProcedure((a, b) -> a.equals(b));

    /*
    This procedure handles the conditional expressions. It receives exactly 3 arguments  (throws exception if not
    exactly 3 arguments provided) and evaluates the condition and based on the value of the condition, only one of the
    if-true or if-false expressions is evaluated further.
     */
    Procedure IF = (arguments, referencingEnvironment) -> {
        //Make sure there are exactly 3 arguments
        if (length(arguments) != 3) {
            throw new IllegalArgumentException("if requires exactly three arguments");
        }

        Object condition = eval(car(arguments), referencingEnvironment);

        //If the condition (argument number 1) is NOT false (everything other than false is true)
        if (condition != Boolean.FALSE) {   //If-true
            return eval(car(cdr(arguments)), referencingEnvironment); // evaluate argument number 2
        } else {    //If-false - if the condition is evaluated to false
            return eval(car(cdr(cdr(arguments))), referencingEnvironment); // eval argument number 3
        }
    };

    /*
    This procedure allows local allocation of bindings to expression values. Usage example is to simplify
    compound expressions.
     */
    Procedure LET = (arguments, referencingEnvironment) -> {
        //Make sure correct number of arguments is given
        if (length(arguments) != 2) {
            throw new IllegalArgumentException("let needs exactly two arguments");
        }

        // The first argument is "a list of bindings between symbols and sub-expressions"
        Object listOfBindings = car(arguments);

        // Make sure the first argument is a list of lists
        if (!isList(listOfBindings)) {
            throw new IllegalArgumentException("First argument of let must be a list of bindings");
        }

        // Create a new local referencing environment that copies the existing one to have the scope of the bindings
        // This will then be unallocated by java's garbage collection mechanism
        Map<Symbol, Object> localEnvironment = new HashMap<>(referencingEnvironment);

        // Iterate through each given binding in the bindings list to assign them to variables
        while (listOfBindings != SchemeCore.nil) {
            Pair binding = (Pair) car(listOfBindings);

            //If a binding is not a list consisting of a symbol and an expression
            if (length(binding) != 2 || !(car(binding) instanceof Symbol)) {
                throw new IllegalArgumentException("Each binding must be a list of exactly a symbol and a expression");
            }

            Symbol variable = (Symbol) car(binding);
            Object expression = car(cdr(binding));

            //calculating the value of the given expression and put it in the local environment
            Object value = eval(expression, localEnvironment);
            localEnvironment.put(variable, value);

            listOfBindings = cdr(listOfBindings);
        }

        // Second argument given is the expression that needs to be evaluated given new bindings
        Object body = car(cdr(arguments));
        return eval(body, localEnvironment);
    };

    /*
    Lambda special form creates closures that takes a list of parameters and an expression, making a procedure that can
    be called with arguments to evaluate the expression with respect to the given parameters.
     */
    Procedure LAMBDA = (arguments, referencingEnvironment) -> {
        //Make sure correct number of arguments is given
        if (length(arguments) != 2) {
            throw new IllegalArgumentException("let needs exactly two arguments");
        }

        Object params = car(arguments);
        Object procExpressionBody = car(cdr(arguments));

        // Make sure the parameters list is valid
        if (!isList(params)) {
            throw new IllegalArgumentException("First argument of lambda must be a list (of params)");
        }

        // Make sure each element in the parameters list is a Symbol
        Object currParam = params;
        while (currParam != SchemeCore.nil) { //while we haven't reached the end of params
            Object param = car(currParam);
            if (!(param instanceof Symbol)) {
                throw new IllegalArgumentException("Each parameter must be a symbol. This is not: " + param);
            }

            currParam = cdr(currParam); //Get next param
        }

        // Return a new Procedure that represents a closure
        return new Procedure() {
            @Override
            public Object apply(Pair args, Map<Symbol, Object> currentEnvironment) {
                //Make sure the number of arguments given is correct (= number of params)
                if (length(args) != length(params)) {
                    throw new IllegalArgumentException("The number of arguments given to the lambda function is not " +
                            "correct");
                }

                // Create a new environment for the closure / clone the referencing environment
                Map<Symbol, Object> closureEnvironment = new HashMap<>(referencingEnvironment);

                // Pair params with arguments using zip function
                Pair zippedParamsAndArgs = SchemeCore.zip(params, args);
                while (zippedParamsAndArgs != SchemeCore.nil) { //While we haven't reached the end of list
                    Pair binding = (Pair) car(zippedParamsAndArgs);
                    Symbol param = (Symbol) car(binding);
                    Object argValue = car(cdr(binding));

                    closureEnvironment.put(param, eval(argValue, currentEnvironment));

                    zippedParamsAndArgs = (Pair) cdr(zippedParamsAndArgs); //Get next
                }

                // Evaluate the body of the lambda expression according to the closure environment
                return eval(procExpressionBody, closureEnvironment);
            }
        };
    };



    /*
     This method receives two Object and checks whether they are integers - instance of BigInteger. It throws an
     exception otherwise.
     */
    private static void makeSureItIsBigInteger (Object a, Object b) {
        if (!(a instanceof BigInteger & b instanceof BigInteger)) {
            throw new IllegalArgumentException("One or both arguments is a not an integer");

        }
    }

    //From Assignment 3 - Instructor's Solution
    Object apply(Pair arguments, Map<Symbol, Object> referencingEnvironment);
}

/**
 * Example of the usage of Lambda special form:
 * > (define factorial (lambda (n) (if (= n 0) 1 (* n (factorial (- n 1))))))
 * null
 * > (factorial 0)
 * 1
 * > (factorial 1)
 * 1
 * > (factorial 2)
 * 2
 * > (factorial 3)
 * 6
 * > (factorial 10)
 * 3628800
 * > (factorial 100)
 * 93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000
 * > (factorial 200)
 * 788657867364790503552363213932185062295135977687173263294742533244359449963403342920304284011984623904177212138919638830257642790242637105061926624952829931113462857270763317237396988943922445621451664240254033291864131227428294853277524242407573903240321257405579568660226031904170324062351700858796178922222789623703897374720000000000000000000000000000000000000000000000000
 * > (factorial 300)
 * 306057512216440636035370461297268629388588804173576999416776741259476533176716867465515291422477573349939147888701726368864263907759003154226842927906974559841225476930271954604008012215776252176854255965356903506788725264321896264299365204576448830388909753943489625436053225980776521270822437639449120128678675368305712293681943649956460498166450227716500185176546469340112226034729724066333258583506870150169794168850353752137554910289126407157154830282284937952636580145235233156936482233436799254594095276820608062232812387383880817049600000000000000000000000000000000000000000000000000000000000000000000000000
 * > exit
 */
