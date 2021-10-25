import java.lang.*;
import java.util.*;

public class Animal {
    String name;
    int height;
    int weight;
    String color;
    String eat;
    String sleep;
    String animalSound;

    public static void main(String[] args) {
        Scanner inputString = new Scanner(System.in);
        Scanner inputInt = new Scanner(System.in);
        Animal x1 = new Animal();
        String name = inputString.nextLine();
        x1.getName(name);
        if(x1.name.equals("cow")|| x1.name.equals("cat")||x1.name.equals("dog")){
            if(x1.name.equals("cow")){

            }
            if(x1.name.equals("cat")){

            }
            if(x1.name.equals("dog")){

            }
        }

    }

    public void getName(String name){} {
        this.name = name;
    }
    public void setHeight(int height){
        this.height = height;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getisEat() {
        return eat;
    }
    public String getisSleep() {
        return sleep;
    }
    public void setAnimalSound(String animalSound) {
        this.animalSound = animalSound;
    }
    public void Eat(Animal x1){
        if(x1.getisEat() == false){
            System.out.println("It isn't eating now");
            eat = true;
        }
        else{
            System.out.println("It is eating now");
            eat = true;
        }
    }

    public int getHeight(){
        return height;
    }
    public int getWeight() {
        return weight;
    }
    public String getColor() {
        return color;
    }
    public String getEat() {
        return eat;
    }
    public String getSleep() {
        return sleep;
    }
    public String getAnimalSound() {
        return animalSound;
    }
}