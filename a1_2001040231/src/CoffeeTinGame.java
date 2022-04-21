import utils.DOpt;
import utils.DomainConstraint;
import utils.OptType;
import java.beans.Beans;
import java.util.Arrays;

/**
 * @overview a program that performs the coffee tin game on a tin of beans and display result on the standard output
 * 
 * @author
 */
public class CoffeeTinGame {
    /** constant value for green bean */
    @DomainConstraint(mutable = false, optional = false)
    private static final char GREEN = 'G';
    /** constant value for blue bean */
    @DomainConstraint(mutable = false, optional = false)
    private static final char BLUE = 'B';
    /** constant for removed beans */
    @DomainConstraint(mutable = false, optional = false)
    private static final char REMOVED = '-';
    /** the null character */
    @DomainConstraint(mutable = false, optional = false)
    private static final char NULL = '\u0000';
    @DomainConstraint(mutable = true, optional = false, min = 30)
    private static final char[] BeansBag = {BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, REMOVED, REMOVED, REMOVED, REMOVED, REMOVED, REMOVED, REMOVED, REMOVED, REMOVED, REMOVED};
    /**
     * the main procedure
     * @effects
     *      initialise a coffee tin
     *      {@link System.out.printf (String, Object...)}: print the tin content
     *      {@link tinGame (char[])}: perforn the coffee tin game on tin
     *      {@link System.out.printf (String, 0Object...)}: print the tin content again
     *      if last bean is correct
     *         {@link System.out.printf (String, Object...)}: print its colour
     *      else
     *         {@link System.out.printf (String, Object...)): print an error message
     *
     */

    public static void main(String[] args) {
        // initialize some beans
        char[] tin = {BLUE, BLUE, BLUE, GREEN, GREEN, GREEN, BLUE, GREEN, GREEN, BLUE, BLUE, GREEN, REMOVED, BLUE, GREEN, GREEN, BLUE, GREEN, GREEN};
        int greensBean = 0;
        // count number of greens
        for (char bean: tin){
            if (bean == GREEN) {
                greensBean++;
            }
        }

        final char last = (greensBean % 2 == 1) ? GREEN : BLUE;
        // p0 = green parity /\
        // (p0=1 -> last=GREEN) /\ (p0=0 -> last=BLUE)

        // print the content of tin before the game
        System.out.printf("tin before: %s %n", Arrays.toString(tin));

        //perform the game
        char lastBean = tinGame(tin);
        // lastBean = last \/ lastBean != last

        // print the content of tin and last bean
        System.out.printf("tin after: %s %n", Arrays.toString(tin));

        // check if last bean as expected and print
        if (lastBean == last){
            System.out.printf("last bean: %c ", lastBean);
        }
        else{
            System.out.printf("Oops, wrong last bean: %c (expected: %c)%n",lastBean,last);
        }

    }


    /**
     * @requires tin is not null /\ tin.length > 1
     * @effects
     * if tin has at least two beans
     *  return true
     * else
     *  return false
     */

    private static boolean hasAtLeastTwoBeans(char[] tin){
        int count = 0;
        for (char bean : tin){
            if (bean != REMOVED){
                count++;
            }
            if (count >= 2) return true; //enough beans
        }
        //not enough beans
        return false;
    }

    /**
     * @requires tin is not null and tin.length > 1
     * @modifiestin
     * @return remove any two beans from tin and return them
     */

    private static char[] takeTwo(char[] tin){
        char bean1 = takeOne(tin);
        char bean2 = takeOne(tin);
        return new char[]{bean1, bean2};
    }

    /**
     *
     * Performs the coffee tin game to determine the color of the last bean
     *
     * @requires tin is not null /\ tin.length > 0
     * @modifies tin
     * @effects
     * take out two beans from tin respectively
     * if same color
     *  throw both away, out one blue bean back
     * else
     *  put green bean back
     * let p0 = initial number of green beans
     * if p0 = 1
     *  res = 'G'
     * else
     *  res = 'B'
     * @
     */

    public static char tinGame(char[] tin){
        while (hasAtLeastTwoBeans(tin)){
            //take two beans from tin
            char[] takeTwo = takeTwo(tin);
            char bean1 = takeTwo[0];
            char bean2 = takeTwo[1];
            // process beans to update tin
            updateTin(tin, bean1, bean2);
        }
        return anyBean(tin);

    }

    /**
     * @requires tin has at least one bean /\ tin is not null
     * @modifies tin
     * @effects
     * remove any bean from tin and return it
     */

    public static char takeOne(char[] tin){
        int random = randInt(tin.length); // choose a random number from [0,tin.length)
        while(tin[random] == REMOVED){
            random = randInt(tin.length);
        }
        char removedBean = tin[random];
        tin[random] = REMOVED;
        return removedBean;
    }
    /**
     * @requires an integer n
     * @effects
     * return a random number from [0,n)
     */

    public static int randInt(int n){
        int radInt = (int) ((Math.random()*n));
        return radInt;
    }
    /**
     * randomly find and return a bean in BEansBag that mathces the bean type
     * @requires beansBag not nu.l /\ bean type not null
     * @modifies BeansBag
     * @effect take out any bean from Beansbag
     * if selected bean matches the bean type
     *  return it
     * else
     *  continue select and compare
     * 
     * @return a bean that match the bean type
     */

    public static char getBean(char[] beansBag, char beanType){
        char selected = takeOne(beansBag);
        while (selected != beanType){
            selected = takeOne(beansBag);
        }
        return selected;
    }

    /**
     * @requires tin has vacant positions for new beans
     * @modifies tin
     * @effects
     *  place bean into any vacant position in tin
     */


    private static void putIn(char[] tin, char bean){
        for (int i = 0; i < tin.length; i++){
            if (tin[i] == REMOVED){ // vacant position
                tin[i] = bean;
                break;
            }
        }
    }

    /**
     * @effects
     * if there are beans in tin
     *  return any such bean
     * else
     *  return '\u0000' (null character)
     */
    private static char anyBean(char[] tin){
        for (char bean: tin){
            if (bean != REMOVED){
                return bean;
            }
        }
        // no beans left
        return NULL;
    }

    /**
     * Updates the tin
     * 
     * @requires tin has at least one bean, bean 1, bean 2
     * @effects
     * update tin with bean 1 and bean 2
     * if two beans are same colot
     *  throw them both away
     *  put a blue bean back in
     * else
     *  throw away the blue one
     *  put the green one back
     */

    public static void updateTin(char[] tin, char bean1, char bean2){
        if (bean1 == bean2){
            putIn(tin, getBean(BeansBag, BLUE));
        }
        else{
            putIn(tin, GREEN);
        }
    }

}
