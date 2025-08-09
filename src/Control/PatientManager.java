package Control;
import Entity.Patient;
import adt.LinkedHashMap;

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
        
        // Pre-load sample data
        Patient alice = new Patient("050101070101", "Alice Lee", "0123456789", 25, 'F', "123, Jalan ABC");
        Patient bob = new Patient("050202070202", "Bob Tan", "0198765432", 30, 'M', "456, Jalan XYZ");
        Patient charlie = new Patient("050303070303", "Charlie Lim", "0112345678", 22, 'M', "789, Jalan 123");
        
        patientMap.put(alice.getPatientIC(), alice);
        patientMap.put(bob.getPatientIC(), bob);
        patientMap.put(charlie.getPatientIC(), charlie);
    }

    public boolean isPatientExist(String ic) {
        if (ic == null || ic.trim().isEmpty()) {
            return false;
        }
        return patientMap.containsKey(ic);
    }

    public Patient registerNewPatient(String ic, String name, String phone, int age, char gender, String address) {
        if (ic == null || ic.trim().isEmpty()) {
            System.out.println("IC number cannot be empty!");
            return null;
        }
        
        if (isPatientExist(ic)) {
            System.out.println("Patient with IC " + ic + " already exists!");
            return null;
        }
        
        Patient patient = new Patient(ic, name, phone, age, gender, address);
        return addNewPatient(patient) ? patient : null;
    }

    public boolean addNewPatient(Patient patient) {
        if (patient == null) {
            System.out.println("Patient object cannot be null!");
            return false;
        }
        
        String ic = patient.getPatientIC();
        if (ic == null || ic.trim().isEmpty()) {
            System.out.println("Patient IC cannot be empty!");
            return false;
        }
        
        if (isPatientExist(ic)) {
            System.out.println("Patient with IC " + ic + " already exists!");
            return false;
        }
        
        patientMap.put(ic, patient);
        return true;
    }

    public Patient findPatientByIC(String patientIC) {
        if (patientIC == null || patientIC.trim().isEmpty()) {
            return null;
        }
        return patientMap.get(patientIC);
    }

    public void displayPatientDetails(Patient patient) {
        if (patient != null) {
            System.out.println("=== Patient Details ===");
            System.out.println(patient.toString());
        } else {
            System.out.println("Patient not found!");
        }
    }
    
    //update existing patient information
    public boolean updatePatient(String ic, Patient updatedPatient) {
        if (ic == null || ic.trim().isEmpty() || updatedPatient == null) {
            return false;
        }
        
        if (!isPatientExist(ic)) {
            System.out.println("Patient with IC " + ic + " does not exist!");
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
    
    //display all patients in insertion order
    public void displayAllPatients() {
        if (isEmpty()) {
            System.out.println("No patients in the system.");
            return;
        }
        
        System.out.println("=== All Patients (Insertion Order) ===");
        System.out.println("Total Patients: " + getTotalPatients());
        System.out.println("======================================");
        patientMap.display();
        System.out.println("======================================");
    }
    
    public void clearAllPatients() {
        patientMap.clear();
        System.out.println("All patients have been cleared from the system.");
    }
}