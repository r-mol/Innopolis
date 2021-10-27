import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    static void sortRowWise(int[][] boxes)
    {
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[i].length; j++) {
                for (int k = 0; k < boxes[i].length - j - 1; k++) {
                    if (boxes[i][k] > boxes[i][k + 1]) {
                        // swapping of elements
                        int t = boxes[i][k];
                        boxes[i][k] = boxes[i][k + 1];
                        boxes[i][k + 1] = t;
                    }
                }
            }
        }

    }

    static void sortByVolumeOfDimensions(int[][] boxes, int rows){

        int[] volume = new int[rows];

        for(int i = 0; i < rows; i++){
            volume[i] = boxes[i][0] * boxes[i][1] * boxes[i][2];
        }

        for (int i = 0; i < rows; i++) {
            for (int j = i+1 ; j < rows; j++) {
                if(volume[i] > volume[j]) {
                    int t1 = boxes[i][0];
                    int t2 = boxes[i][1];
                    int t3 = boxes[i][2];
                    boxes[i][0] = boxes[j][0];
                    boxes[i][1] = boxes[j][1];
                    boxes[i][2] = boxes[j][2];
                    boxes[j][0] = t1;
                    boxes[j][1] = t2;
                    boxes[j][2] = t3;
                }
            }
        }
    }

    public static void main(String[] args) {
        // input from file 
        File inputfile = new File("input.txt");
        int[][] boxes = new int[99][3];
        String temp = "";
        try{
            Scanner input = new Scanner(inputfile);
            temp = input.nextLine();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        char[] chars = temp.toCharArray();
        int rows = 0;
        int colums = 0;

        // move from array chars to array boxes 
        for (char aChar : chars) {
            if (aChar > 47 && aChar < 58) {
                boxes[rows][colums] = aChar - 48;
                colums++;
                if (colums == 3) {
                    colums = 0;
                    rows++;
                }
            }
        }
        
        // sort dimensions in array of box
        sortRowWise(boxes);
        // sort boxes from the smallest to largest
        sortByVolumeOfDimensions(boxes,rows);

        int [] check =new int[rows];
        int count = 1;
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < rows; j++){
                if(boxes[i][0] < boxes[j][0] && boxes[i][1] < boxes[j][1] && boxes[i][2] < boxes[j][2] ) {
                    if(check[j] != 1) {
                        count++;
                    }
                    check[j] = 1;
                    break;
                }
            }
        }

        // output to file
        try{
            FileWriter writer = new FileWriter("output.txt");
            writer.write(String.valueOf(count));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}