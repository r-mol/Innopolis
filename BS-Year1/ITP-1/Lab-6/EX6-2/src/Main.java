import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int len = 1;
        String name;
        double price;
        int qty;
        String name_author;
        String email;
        char gender;
        Book[] books = new Book[len];

        for (int i = 0; i < len; i++){
            System.out.print("Enter name of book: ");
            name = input.nextLine();
            System.out.print("Enter Author's name: ");
            name_author = input.nextLine();
            System.out.print("Enter Author's Email: ");
            email = input.nextLine();
            System.out.print("Enter Author's gender: ");
            gender = input.next().charAt(0);
            System.out.print("Enter Book's price: ");
            price = input.nextDouble();
            System.out.print("Enter Book's quantity: ");
            qty = input.nextInt();
            books[i] = new Book(name,new Author(name_author,email,gender),price,qty);
            input.nextLine();
        }

        for (int i = 0; i < len; i++){
            System.out.println(books[i]);
        }

        System.out.print("PLZ rewrite price from " + books[0].getPrice() + " to new one: ");
        books[0].setPrice(input.nextDouble());

        System.out.print("New price: ");
        System.out.println(books[0].getPrice());

    }

}
