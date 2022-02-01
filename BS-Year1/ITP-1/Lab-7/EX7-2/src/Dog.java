public class Dog extends Creature{
    String name;

    @Override
    void bear(String name) {
        this.name = name;

        System.out.println(getClass().getName() + " called " + name + " has born!");
        isAlive = true;
    }

    @Override
    void die() {
        System.out.println(getClass().getName() + " called " + name + " has died!");
        isAlive = false;
    }

    void bark(){
        System.out.println(name + "goes bark...");
    }

    public String shoutName(){
        return String.format(getClass().getName() + " called " + name);
    };
}
