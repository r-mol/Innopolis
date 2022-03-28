public class Factory {

    enum Shapes {SQUARE, TRIANGLE, RECTANGLE,CIRCLE}

    public Perimeter getShape(Shapes shape) {
        return switch (shape) {
            case SQUARE -> new Square(2);
            case TRIANGLE -> new Triangle(1, 2, 3);
            case RECTANGLE -> new Rectangle(2, 3);
            case CIRCLE -> new Circle(5);
        };
    }

    void printPerimeter(Perimeter perimeter) {
        perimeter.calculate();
    }

    public static void main(String[] args) {
        Factory factory = new Factory();
        factory.printPerimeter(factory.getShape(Shapes.SQUARE));
        factory.printPerimeter(factory.getShape(Shapes.TRIANGLE));
        factory.printPerimeter(factory.getShape(Shapes.RECTANGLE));
    }
}