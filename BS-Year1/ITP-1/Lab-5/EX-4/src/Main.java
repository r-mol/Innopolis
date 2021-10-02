import java.io.File;


public class Main {
    public static void main(String[] args){
        File fin = new File("input.txt");

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
