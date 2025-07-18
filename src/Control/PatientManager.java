package Control;
import Entity.Patient;

/*  1. store patient in an array
    2. search for patient by ic number
    3. register new patient */
/*Flow
    1. Ask for ic number
    2. Check if patient exists
    3. If exists, retrieve patient details and proceed to visit registration
    4. If not exists, add new patient details*/

public class PatientManager {    
    private Entity.Patient[] patientList;
    private int totalPatients;
    private static final int MAX_PATIENTS = 100;

    public PatientManager() {
        patientList = new Entity.Patient[MAX_PATIENTS];
        totalPatients = 0;

        patientList[totalPatients++] = new Patient("050101070101", "Alice Lee", "0123456789", 25, 'F', "123, Jalan ABC");
        patientList[totalPatients++] = new Patient("050202070202", "Bob Tan", "0198765432", 30, 'M', "456, Jalan XYZ");
        patientList[totalPatients++] = new Patient("050303070303", "Charlie Lim", "0112345678", 22, 'M', "789, Jalan 123");
    }
    
    public boolean isPatientExist(String ic) {
        return findPatientByIC(ic) != null;
    }

    public Patient registerNewPatient(String ic, String name, String phone, int age, char gender, String address) {
        Patient patient = new Patient(ic, name, phone, age, gender, address);
        return addNewPatient(patient) ? patient : null;
    }

    public boolean addNewPatient(Patient patient) {
        if (totalPatients < MAX_PATIENTS) {
            patientList[totalPatients++] = patient;
            return true;
        }
        return false;
    }

    public Patient findPatientByIC(String patientIC) {
        for (int i = 0; i < totalPatients; i++) {
            if (patientList[i].getPatientIC().equals(patientIC)) {
                return patientList[i];
            }
        }
        return null;
    }

    public void displayPatientDetails(Patient patient) {
        if (patient != null) {
            System.out.println(patient.toString());
        } else {
            System.out.println("Patient not found!");
        }
    }
}