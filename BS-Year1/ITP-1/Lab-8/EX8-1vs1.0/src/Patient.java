
import java.util.HashSet;

public class Patient extends User {
    protected int ID;
    protected Bill bill;
    protected Receptionist receptionist;
    protected boolean hasDoctor;

    public Patient(String name, int ID) {
        super(name);
        this.ID = ID;
        hasDoctor = false;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public Bill getBill() {
        return bill;
    }

    public void setReceptionist(Receptionist receptionist) {
        this.receptionist = receptionist;
    }

    public boolean isHasDoctor() {
        return hasDoctor;
    }

    public void setDoctor() {
        hasDoctor = true;
    }
}
