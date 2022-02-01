import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//  a) Write a Java program to convert temperature from Fahrenheit to Celsius degree.

//        Scanner temperature = new Scanner(System.in);
//
//        int Fahrenheit = temperature.nextInt();
//        int Celsius = (Fahrenheit - 32)*5/9;
//
//        System.out.println(Celsius);

        Scanner time = new Scanner(System.in);
        int hours = time.nextInt();
        int minutes = time.nextInt();
        int seconds = time.nextInt();
        Time start = new Time(hours, minutes, seconds);

        hours = time.nextInt();
        minutes = time.nextInt();
        seconds = time.nextInt();

        Time end  = new Time(hours, minutes, seconds);
        Time diff = Time.difference(start, end);

        System.out.printf("TIME DIFFERENCE: %d:%d:%d - ", end.hours_main, end.minutes_main,end.seconds_main);
        System.out.printf("%d:%d:%d ", start.hours_main, start.minutes_main, start.seconds_main);
        System.out.printf("= %d:%d:%d\n", diff.hours_main, diff.minutes_main, diff.seconds_main);


//  c) Write a Java program to convert binary, decimal and hexadecimal numbers
//        Scanner num = new Scanner(System.in);
//        int n = num.nextInt();
//        String binary = Integer.toBinaryString(n);
//        String hexadecimal = Integer.toHexString(n);
//
//        System.out.println(n);
//        System.out.println(binary +"\n" + hexadecimal);



    }
}

