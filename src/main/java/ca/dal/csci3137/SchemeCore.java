
package ca.dal.csci3137;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.Map;


public class SchemeCore {
    /** Represents the empty pair, also known as the empty list. */
    public static final Pair nil = new Pair(null, null);

    /**
     * Returns an instance of the {@link ca.dal.csci3137.Symbol} class with the given name.
     *
     * @param name The name of the symbol to be returned.
     * @return an instance of the {@link ca.dal.csci3137.Symbol} class with the given name.
     */
    public static Symbol symbol(String name) {
        return new Symbol (name);
    }

    /**
     * Returns an instance of the {@link ca.dal.csci3137.Pair} class with the given ar and dr.
     *
     * @param ar The first element of the pair. (Called the address part of the register for historical reasons.)
     * @param dr The second element of the pair. (Called the decrement part of the register for historical reasons.)
     * @return an instance of the {@link ca.dal.csci3137.Pair} class with the given ar and dr.
     * @see <a href="https://srfi.schemers.org/srfi-1/srfi-1.html#cons">Scheme's cons function</a>
     */
    public static Pair cons(Object ar, Object dr) {
        return new Pair (ar, dr);
    }

    /**
     * Returns the contents of the address part of the register for the given pair.
     * <p>
     * Throws an exception when given the empty pair {@link ca.dal.csci3137.SchemeCore#nil} or an instance of any class
     * other than {@link ca.dal.csci3137.Pair}
     *
     * @param pair the given pair (should be an instance of {@link ca.dal.csci3137.Pair})
     * @return the contents of the address part of the register for the given pair.
     * @see <a href="https://srfi.schemers.org/srfi-1/srfi-1.html#car">Scheme's car function</a>
     */
    public static Object car(Object pair) {
        if (pair == null) {
            throw new IllegalArgumentException("argument is null");
        } else if (!(pair instanceof Pair)) {   //If the given pair is not a proper Pair
            throw new IllegalArgumentException("argument is not a pair");
        }

        if (pair == nil) {  //If given the empty pair
            throw new IllegalArgumentException("argument is nil (empty pair)");
        }

        Pair castedPair = (Pair)pair;
        return castedPair.ar;
    }

    /**
     * Returns the contents of the decrement part of the register for the given pair.
     * <p>
     * Throws an exception when given the empty pair {@link ca.dal.csci3137.SchemeCore#nil} or an instance of any class
     * other than {@link ca.dal.csci3137.Pair}
     *
     * @param pair the given pair (should be an instance of {@link ca.dal.csci3137.Pair})
     * @return the contents of the decrement part of the register for the given pair.
     * @see <a href="https://srfi.schemers.org/srfi-1/srfi-1.html#cdr">Scheme's cdr function</a>
     */
    public static Object cdr(Object pair) {
        //Please note this part could be extracted as a private method, but I didn't extract it to keep the structure
        if (pair == null) {
            throw new IllegalArgumentException("argument is null");
        } else if (!(pair instanceof Pair)) {   //If the given pair is not a proper Pair
            throw new IllegalArgumentException("argument is not a pair");
        }

        if (pair == nil) {  //If given the empty pair
            throw new IllegalArgumentException("argument is nil (empty pair)");
        }

        Pair castedPair = (Pair)pair;
        return castedPair.dr;
    }

    /**
     * Returns a proper list constructed from the given items.
     * <p>
     * A proper list is either the empty pair {@link ca.dal.csci3137.SchemeCore#nil} (sometimes called the empty list)
     * or a pair whose car is the first element of the list (i.e. head) and whose cdr is itself a proper list containing
     * the remaining elements of the list (i.e. the tail).
     *
     * @param items the array of items to be included in the proper list.
     * @return a proper list constructed from the given items.
     * @see <a href="https://srfi.schemers.org/srfi-1/srfi-1.html#list">Scheme's list function</a>
     */
    public static Pair list(Object... items) {
        if (items == null){
            throw new IllegalArgumentException("argument is null");
        } else if (items.length == 0){  //If given empty list
            return nil;
        }

        Pair list = nil;
        //Assembling the list from the "tail" (end) to the "head" (beginning)
        for (int i = items.length - 1; i >= 0; i--){
            list  = new Pair (items[i], list);
        }

        return list;
    }

    /**
     * A predicate that determines if its argument is a proper list (see {@link ca.dal.csci3137.SchemeCore#list(Object...)}.
     *
     * @param o the object to be tested for proper list-hood.
     * @return true if o is a proper list, false otherwise.
     * @see <a href="https://srfi.schemers.org/srfi-1/srfi-1.html#proper-list-p">Scheme's proper-list? function</a>
     */
    public static boolean isList(Object o) {
        if (o == null){
            throw new IllegalArgumentException("argument is null");
        }
        if (o == nil){  //A null list is one case of proper lists
            return true;
        }

        //Checking if the all elements from o itself and the objects in each dr is a Pair (proper list condition)
        if (o instanceof Pair){
            Object next = ((Pair) o).dr;
            while (next instanceof Pair){
                if (next == nil){
                    return true;
                }
                next = ((Pair) next).dr;
            }
        } else {    //If the very first element is not a Pair
            return false;
        }
        //If not every single Pair reached is a Pair
        return false;
    }

    /**
     * Returns the number of elements in a proper list (see {@link ca.dal.csci3137.SchemeCore#list(Object...)}.
     * <p>
     * Throws an exception if the given list is not a proper list.
     *
     * @param list the given list.
     * @return the number of elements in the given list.
     * @see <a href="https://srfi.schemers.org/srfi-1/srfi-1.html#length">Scheme's length function</a>
     */
    public static int length(Object list) {
        if (!isList(list)){ //Check if list is valid
            throw new IllegalArgumentException("the list provided is not a proper list");
        }

        int length = 0;
        if (list instanceof Pair){
            Pair curr = (Pair) list;
            while (curr != nil && curr.dr instanceof Pair){
                length++;
                curr = (Pair)(curr.dr); //Moving the "head" in the list to the next level
            }
        }

        return length;
    }

    /**
     * Performs the zip operation on a given array of proper lists.
     * <p>
     * If zip is given <i>n</i> lists, it returns a list as long as the shortest of these lists, each element of which
     * is itself, an <i>n</i>-element list comprised of the corresponding elements from the given <i>n</i> lists.
     *
     * @param lists the array of lists to be zipped.
     * @return the result of the zip operation.
     * @see <a href="https://srfi.schemers.org/srfi-1/srfi-1.html#zip">Scheme's zip function</a>
     */
    public static Pair zip(Object... lists) {
        if (lists == null){
            throw new IllegalArgumentException("no list was provided to method");
        } else if (lists.length == 0){  //"lists" should have at least one element
            throw new IllegalArgumentException("no list was provided to method");
        }

        //Making sure if all the given lists are proper lists
        for (int i = 0; i < lists.length; i++) {
            if (!isList(lists[i])) {
                throw new IllegalArgumentException("all arguments must be proper lists.");
            }
        }

        //Finding the minimum length in the given lists
        int minLength = length(lists[0]);
        for (int i = 1; i < lists.length; i++){
            if (minLength > length(lists[i])){
                minLength = length(lists[i]);
            }
        }

        //Creating and returning the zipped list
        Object [] resultArray = new Object[minLength];
        for (int i = 0; i < minLength; i++){
            Object [] elements = new Object[lists.length];

            //Making up the inner lists by concatenating the jth element of each list inside "element" array.
            for (int j = 0; j < lists.length; j++){
                Pair currPair = (Pair) lists[j];
                //Add the current element (at the "head") to the array and move the "head"
                elements[j] = currPair.ar;
                lists[j] = currPair.dr;
            }

            //Adding the inner lists to the final result array as pairs
            Pair newList = list(elements);
            resultArray[i] = newList;
        }

        Pair result = list(resultArray);
        return result;
    }

    public static Object eval(Object expression, Map<Symbol, Object> referencingEnvironment) {
        if (expression instanceof Symbol) {
            if(!referencingEnvironment.containsKey(expression)) throw new IllegalArgumentException(expression + "is not a defined symbol.");
            return referencingEnvironment.get(expression);
        }

        if (expression instanceof Pair) {
            Procedure procedure = (Procedure) eval(car(expression), referencingEnvironment);
            return procedure.apply((Pair) cdr(expression), referencingEnvironment);
        }

        return expression;
    }

}
