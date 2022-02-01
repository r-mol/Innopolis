import java.io.*;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        File fin = new File("input.txt");
        FileWriter fout = new FileWriter("output.txt");

        Scanner in  = new Scanner(fin);
        String line = in.nextLine();
        System.out.println(line);

        fout.write(line);
        fout.close();
        if(fin.exists()){
            System.out.println("The directory or file exists.");
            System.out.println(fin.getAbsolutePath());
            System.out.println("File Name: "+ fin.getName());
            System.out.println("File Size: "+ fin.length() + " bytes");
        }
        else{
            System.out.println("The directory or file does not exist.");
        }
    }
}
