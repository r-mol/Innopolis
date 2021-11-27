import java.util.ArrayList;
import java.util.Scanner;

interface StringFunction {
    String run(String str);
}

interface Predicate<T>{
    boolean test(T t);
}

interface UnaryOperator <T>{
    T apply(T t);
}

interface BinaryOperator<T>{
    T apply(T t1,T t2);
}

interface Function<T,R> {
    R apply(T t);
}

interface Supplier<T> {
    T get();
}


interface Consumer<T> {
    void accept(T t);
}

public class Main {
    public static void main(String[] args) {
        StringFunction exclaim = (s) -> s + "!";
        printFormatted("Hello", exclaim);

        int five = 5;
        int ten = 10;
        Predicate<Integer> isPositive = x -> x > 0;
        Predicate<Integer> isNegative = x -> x < 0;
        System.out.println(isPositive.test(five));
        System.out.println(isNegative.test(five));

        UnaryOperator<Integer> square = x -> x*x;
        UnaryOperator<Integer> cube = x -> x*x*x;
        System.out.println(square.apply(five));
        System.out.println(cube.apply(five));


        BinaryOperator<Integer> multiply = (x,y) -> x*y;
        BinaryOperator<Integer> dividing = (x,y) -> y/x;
        System.out.println(multiply.apply(five,ten));
        System.out.println(dividing.apply(five, ten));

        Function<Integer, String> convert = x -> String.valueOf(x) + " dollars";
        System.out.println(convert.apply(five));

        Consumer<Integer> printer = x -> System.out.printf("%d dollars \n", x);
        printer.accept(multiply.apply(five,ten));

        Supplier<User> userFactory = () -> {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter the name: ");
            String name = in.nextLine();
            return new User(name);
        };
        User user1 = userFactory.get();
        User user2 = userFactory.get();
        System.out.println("user1 name: " + user1.getName());
        System.out.println("user2 name: " + user2.getName());


        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(5);
        numbers.add(9);
        numbers.add(8);
        numbers.add(8);
        numbers.add(1);
        numbers.stream().forEach((n)-> System.out.print(n));
        System.out.println();
        numbers.stream()
                .filter(n -> n > 5)
                .distinct()
                .sorted()
                .limit(1)
                .forEach((n)-> System.out.print(n));
        System.out.println("\n" + numbers);

    }

    public static void printFormatted(String str, StringFunction format) {
        String result = format.run(str);
        System.out.println(result);
    }
}

class User {
    private String name;
    String getName(){
        return name;
    }

    User(String n){
        this.name = n;
    }
}
