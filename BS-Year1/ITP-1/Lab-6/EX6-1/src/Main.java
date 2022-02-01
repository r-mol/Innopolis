import java.util.*;


public class Main {
    public static void main(String[] args) {
        Scanner num = new Scanner(System.in);
        Scanner oper = new Scanner(System.in);
        System.out.print("Enter first number: ");
        double argument1 = num.nextDouble();
        System.out.print("Enter operator: ");
        char operator = oper.next().charAt(0);
        System.out.print("Enter second number: ");
        double argument2 = num.nextDouble();
        Calculatr res = new Calculatr(argument1, argument2, operator);

        if(operator == '/' && argument2 == 0){
            System.out.println("Please try again!");
        }
        else if(argument1 < 0 || argument2 < 0){
            System.out.println("Please try again!");
        }
        else {
            switch (res.operator) {
                case '+' -> res.result = res.argument1 + res.argument2;
                case '-' -> res.result = res.argument1 - res.argument2;
                case '*' -> res.result = res.argument1 * res.argument2;
                case '/' -> res.result = res.argument1 / res.argument2;
            }
            System.out.print("Your result: ");
            System.out.println(res.result);
        }
    }

}
