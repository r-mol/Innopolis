import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner num = new Scanner(System.in);
        int len;
        System.out.println("Enter rows and columns:");
        len = num.nextInt();
        int[][] arr = new int[len][len];

        System.out.println("Plesse enter " + len + " on " + len + " elements");
        for (int i = 0; i < len; i++){
            for (int j = 0; j < len; j++) {
                arr[i][j] = num.nextInt();
            }
        }

        for(int i = 0; i < len;i++){
            for (int j = 0; j < len; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.print('\n');
        }

    }
}