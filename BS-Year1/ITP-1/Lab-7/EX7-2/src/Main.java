import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scannerInt = new Scanner(System.in);
        Scanner scannerString = new Scanner(System.in);
        Creature[] name = new Creature[5];

        int count = 0;
        int action = 0;

        while(action != 4 || count != 4){
            System.out.println("Who are u?\n" +
                    "1. Human\n" +
                    "2. Dog\n" +
                    "3. Alien\n" +
                    "4. exit");
            action = scannerInt.nextInt();
            if(action == 1){
                name[count] = new Human();
                System.out.println("Pls enter your name: ");
                String temp = scannerString.nextLine();
                name[count].bear(temp);
                name[count].die();
                count++;
            }
            else if(action == 2){
                name[count] = new Dog();
                System.out.println("Pls enter your name: ");
                String temp = scannerString.nextLine();
                name[count].bear(temp);
                name[count].die();
                count++;
            }
            else if(action == 3){
                name[count] = new Alien();
                System.out.println("Pls enter your name: ");
                String temp = scannerString.nextLine();
                name[count].bear(temp);
                name[count].die();
                count++;
            }
            else if(action == 4){
                System.out.println("Goodbye");
                break;
            }
            else {
                System.out.println("ERROR");
                break;
            }
        }

        for (int i = 0; i < 5; i++) {
            System.out.println(name[i].shoutName());
        }
    }
}
