package Control;
import Entity.Patient;
import adt.LinkedHashMap;
import exception.TryCatchThrowFromFile;

/*  1. store patient in an array
    2. search for patient by ic number
    3. register new patient */
/*Flow
    1. Ask for ic number
    2. Check if patient exists
    3. If exists, retrieve patient details and proceed to visit registration
    4. If not exists, add new patient details*/

public class PatientManager {    
    private LinkedHashMap<String, Patient> patientMap;

    public PatientManager() {
        patientMap = new LinkedHashMap<>();
    }

    public boolean isPatientExist(String ic) {
        if (ic == null || ic.trim().isEmpty()) {
            return false;
        }
        return patientMap.containsKey(ic);
    }

    public Patient registerNewPatient(String ic, String name, String phone, int age, char gender, String address) {
        Patient patient = new Patient(ic, name, phone, age, gender, address);

        patientMap.put(patient.getPatientIC(), patient);

        return patient;
    }

    public Patient findPatientByIC(String patientIC) {
        if (patientIC == null || patientIC.isEmpty()) {
            return null;
        }
        return patientMap.get(patientIC);
    }

    public void displayPatientDetails(Patient patient) {
        patientMap.display();
    }
    
    //update existing patient information
    public boolean updatePatient(String ic, Patient updatedPatient) {
        if (ic == null || ic.trim().isEmpty() || updatedPatient == null) {
            return false;
        }
        
        if (!isPatientExist(ic)) {
            return false;
        }
        
        patientMap.put(ic, updatedPatient);
        return true;
    }
    
    //remove patient from the system
    public Patient removePatient(String ic) {
        if (ic == null || ic.trim().isEmpty()) {
            return null;
        }
        
        return patientMap.remove(ic);
    }
    
    //get total number of patients
    public int getTotalPatients() {
        return patientMap.size();
    }
    
    //check if patient database is empty, return true if no patients, false otherwise
    public boolean isEmpty() {
        return patientMap.isEmpty();
    }
    
    // Return all patients in insertion order without printing
    public Patient[] getAllPatients() {
        Object[] values = patientMap.getValues();
        Patient[] patients = new Patient[values.length];
        for (int i = 0; i < values.length; i++) {
            patients[i] = (Patient) values[i];
        }
        return patients;
    }

    public void clearAllPatients() {
        patientMap.clear();
    }
}