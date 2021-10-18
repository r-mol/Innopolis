public class Patient extends User{
    protected Bill bill;
    protected int ID;

    public Patient(String name, int ID){
        super(name);
        this.ID = ID;
    }



}
