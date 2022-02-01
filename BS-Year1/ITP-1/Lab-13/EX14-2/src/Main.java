import java.util.ArrayList;

/*
Create a list of strings and fill it with random strings of different length containing
English letters and numbers. Duplicate all values and add them to the same list.
By using lambda expressions display the sorted list containing non-empty unique
strings without numbers
 */

public class Main {
    public static boolean isAlpha(String s) {
        return s != null && s.matches("^[a-zA-Z]*$");
    }

    public static void main(String[] args){
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("roma1");
        strings.add("romaaa");
        strings.add("roma");
        strings.add("romaaa");
        strings.add("romaa");

        strings.stream().filter(Main::isAlpha).distinct().sorted().forEach(System.out::println);
    }
}
