import java.util.ArrayList;

public class Main {
    public static void main(String[] args){

        /*
        Create a list of integer numbers and fill
        it with random positive and negative values.
        By using lambda expressions display all of them
        which are divisible by 3 and remove “-” sign if present
         */

        ArrayList<Integer> numbers = new ArrayList<Integer>();
        numbers.add(5);
        numbers.add(-9);
        numbers.add(3);
        numbers.add(1);
        numbers.add(-5);
        numbers.add(9);
        numbers.add(-12);
        numbers.add(1);
        numbers.forEach(i -> {if (i%3 == 0){System.out.println(Math.abs(i));}});
    }
}
