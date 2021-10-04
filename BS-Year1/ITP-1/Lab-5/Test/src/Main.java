import java.util.*;

public class Main {
    public static void main(String[] args){
        Scanner str = new Scanner(System.in);
        String name;
        String surname;
        String date;
        String time;
        String item_name;
        String size;
        String amount;
        String measurements;
        int len = 1;

        Name[] arr = new Name[len];


        for(int i = 0; i <len; i++) {
            System.out.print("Enter Name: ");
            name= str.nextLine();
            System.out.print("Enter Surname: ");
            surname= str.nextLine();
            System.out.print("Enter Date: ");
            date= str.nextLine();
            System.out.print("Enter Time: ");
            time= str.nextLine();
            arr[i] = new Name(name,surname,date,time);
            for(int j = 0; j < 1 ; j++){
                System.out.print("Enter [" + j +"] Item_name: ");
                item_name = str.nextLine();
                System.out.print("Enter [" + j +"] Size: ");
                size = str.nextLine();
                System.out.print("Enter [" + j +"] Amount: ");
                amount = str.nextLine();
                System.out.print("Enter [" + j +"] Measurement: ");
                measurements = str.nextLine();
                arr[i].equipment[j] = new Items(item_name,size,amount,measurements);
            }
        }

        for(int i = 0; i <len; i++) {
            System.out.print(arr[i].name + " " + arr[i].surname + " " + arr[i].date + " " + arr[i].time + " ");
            for(int j = 0; j < 2 ; j++) {
                System.out.println(arr[i].equipment[j].item_name + " " + arr[i].equipment[j].size + " " + arr[i].equipment[j].amount + " " + arr[i].equipment[j].measurements);
            }
        }
    }
}
