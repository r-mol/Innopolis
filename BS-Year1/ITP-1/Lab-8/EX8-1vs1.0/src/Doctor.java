
import java.util.ArrayList;

public class Doctor extends User {
    protected ArrayList<Patient> patients;
    static ArrayList<Receptionist> receptionists;

    public Doctor(String name) {
        super(name);
        patients = new ArrayList<>();
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public void getNewPatient(int amountNewPatients) {
        int cntPatients = 0;
        for (Receptionist reset : receptionists) {
            if (cntPatients == amountNewPatients) break;
            for (Patient patient : reset.getPatients()) {
                if (cntPatients == amountNewPatients) break;
                if (!patient.hasDoctor) {
                    patient.setDoctor();
                    patients.add(patient);
                    cntPatients++;
                }
            }
        }
    }

    public void checkPatient() {
        patients.clear();
    }
}
