import java.util.ArrayList;
import java.util.TreeMap;

public class Receptionist extends User {
    protected ArrayList<Patient> patients;
    private TreeMap<String, Integer> diseaseToPayment;

    public Receptionist(String name) {
        super(name);
        patients = new ArrayList<>();
        diseaseToPayment = new TreeMap<>();
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public void setDisease(String disease, int amount) {
        diseaseToPayment.put(disease, amount);
    }

    public Bill generateBill(String name, int amount) {
        Bill.MAXID++;
        return new Bill(name, Bill.MAXID, amount);
    }

    public void addPatient(Patient patient, String disease) {
        patients.add(patient);
        patient.setReceptionist(this);
        if (!diseaseToPayment.containsKey(disease)) {
            diseaseToPayment.put(disease, 100);
        }
        patient.setBill(generateBill("Pay for it: " + disease,
                diseaseToPayment.get(disease)));
    }
}
