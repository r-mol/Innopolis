
public class Bill {
    static int MAXID = 0;
    protected int ID;
    protected int amount;
    protected String name;

    public Bill(String name, int ID, int amount) {
        this.name = name;
        this.ID = ID;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("Bill name: %s, ID: %d, amount: %d",
                name, ID, amount);
    }
}
