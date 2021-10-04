import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int len = 2;
        String name;
        double price;
        int qty;
        String name_author;
        String email;
        char gender;
        Book[] books = new Book[len];

        for (int i = 0; i < len; i++){
            name = input.nextLine();
            name_author = input.nextLine();
            email = input.nextLine();
            gender = input.next().charAt(0);
            price = input.nextDouble();
            qty = input.nextInt();
            books[i] = new Book(name,new Author(name_author,email,gender),price,qty);
            input.nextLine();
        }

        for (int i = 0; i < len; i++){
            System.out.println(books[i].getName());
            System.out.println(books[i].getAuthor());
            System.out.println(books[i].getPrice());
            System.out.println(books[i].getQty());
        }

    }

}
