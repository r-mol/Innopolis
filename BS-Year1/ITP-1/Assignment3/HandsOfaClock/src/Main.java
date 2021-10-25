import java.io.*;
import java.lang.*;

public class Main {

    public static void main(String[] args) {
        int hour = 0;
        int minutes = 0;
        int angle;

        try {
            FileReader reader = new FileReader("input.txt");
            hour = reader.read() - 48;
            hour = (hour*10) + (reader.read() - 48);
            minutes = reader.read();
            minutes = reader.read() - 48;
            minutes = (minutes*10) + (reader.read() - 48);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(hour < 0 || hour > 23 || minutes < 0 || minutes > 59){
            angle = -1;
        }
        else{
            if(hour > 11){
                hour -= 12;
            }

            hour *= 30;
            minutes *= 6;

            angle = Math.abs(hour - minutes);

        }

        String data;

        if(angle == -1){
            data = "Wrong format";
        }
        else{
            data = String.valueOf(angle);
        }


        try{
            FileWriter writer = new FileWriter("output.txt");
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
