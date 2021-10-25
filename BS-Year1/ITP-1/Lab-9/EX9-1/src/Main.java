import java.util.Scanner;
import java.lang.*;

public class Main {

    public enum Planet {
        MERCURY (7,3.303*Math.pow(10,23), 2.4397*Math.pow(10,6), "Mercury"),
        VENUS(6,4.869*Math.pow(10,24),6.0518*Math.pow(10,6), "Venus"),
        EARTH (5,5.976*Math.pow(10,24),6.37814*Math.pow(10,6),"Earth"),
        MARS (4,6.421*Math.pow(10,23),3.3972*Math.pow(10,6), "Mars"),
        JUPITER (3,1.9*Math.pow(10,27),7.1492*Math.pow(10,7),"Jupiter"),
        SATURN (2,5.688*Math.pow(10,26),6.0268*Math.pow(10,7), "Saturn"),
        URANUS (1,8.686*Math.pow(10,25),2.5559*Math.pow(10,7),"Uranus"),
        NEPTUNE (0,1.024*Math.pow(10,26),2.4746*Math.pow(10,7),"Neptune");
        private final int numPlanet;
        private final double mass;
        private final double radius;
        private final String name;
       Planet (int numPlanet, double mass, double radius, String name) {
            this.numPlanet = numPlanet;
            this.mass = mass;
            this.radius = radius;
            this.name = name;
        }
        public double getMass() {
            return this.mass;
        }
        public double getRadius() {
            return this.radius;
        }
        public int getNumPlanet() {
            return this.numPlanet;
        }
        public String getName(){
           return this.name;
        }

    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        double G = 6.67408*Math.pow(10,-11);
        int EarMass;
        Planet name = Planet.EARTH;

        System.out.print("Please enter your weight: ");
        EarMass = scanner.nextInt();

        double EarthSurfaceGravity = G * name.getMass()/Math.pow(name.getRadius(),2);
        double PerMass = EarMass/EarthSurfaceGravity;

        int choosenPlanet;
        Planet choosen = null;

        System.out.println("On which planet do you want get your mass?");
        System.out.println("1. Neptune");
        System.out.println("2. Uranus");
        System.out.println("3. Saturn");
        System.out.println("4. Jupiter");
        System.out.println("5. Mars");
        System.out.println("6. Earth");
        System.out.println("7. Venus");
        System.out.println("8. Mercury");
        choosenPlanet = scanner.nextInt();

        if (choosenPlanet == 1){
            choosen = Planet.NEPTUNE;
        }
        else if (choosenPlanet == 2){
            choosen = Planet.URANUS;
        }
        else if (choosenPlanet == 3){
            choosen = Planet.SATURN;
        }
        else if (choosenPlanet == 4){
            choosen = Planet.JUPITER;
        }
        else if (choosenPlanet == 5){
            choosen = Planet.MARS;
        }
        else if (choosenPlanet == 6){
            choosen = Planet.EARTH;
        }
        else if (choosenPlanet == 7){
            choosen = Planet.VENUS;
        }
        else if (choosenPlanet == 8){
            choosen = Planet.MERCURY;
        }
        else{
            System.out.println("We do not have this planet!!!");
        }

        assert choosen != null;
        double result = PerMass*(G * choosen.getMass()/ Math.pow(choosen.getRadius(),2));

        System.out.println("Your weight on " + choosen.getName() + " is " + result);
    }

}
