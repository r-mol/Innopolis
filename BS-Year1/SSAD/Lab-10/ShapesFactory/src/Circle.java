public class Circle implements Perimeter{
    int radius;

    public Circle(int radius) {
        this.radius = radius;
    }

    @Override
    public void calculate() {
        System.out.println("Drawing Circle with Perimeter : ");
        System.out.println(radius*radius*3.14);
    }
}
