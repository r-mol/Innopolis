public abstract class Creature {
    abstract void bear(String name);
    abstract void die();
    String name = "null";
    boolean isAlive = false;
    public String shoutName(){
        return String.format(getClass().getName() + " called " + name);
    };
}
