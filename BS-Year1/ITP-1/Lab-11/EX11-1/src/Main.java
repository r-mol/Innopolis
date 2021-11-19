import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Animal> animals = new ArrayList<>();
        Animal cat = new Animal("Cat", "Markiz", 10);
        Animal dog = new Animal("Dog", "Gav", 1);

        animals.add(cat);
        animals.add(dog);

        System.out.println(animals.get(0).toString());

    }

}
