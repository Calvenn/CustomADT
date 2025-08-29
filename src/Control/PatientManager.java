package Control;
import Entity.Patient;
import adt.LinkedHashMap;

/**
 *
 * @author NgPuiYin
 */
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

    public Patient registerNewPatient(String ic, String id, String name, String phone, int age, char gender, String address) {
        Patient patient = new Patient(ic, id, name, phone, age, gender, address);

        patientMap.put(patient.getPatientIC(), patient);

        return patient;
    }

    public Patient findPatientByIC(String patientIC) {
        if (patientIC == null || patientIC.isEmpty()) {
            return null;
        }
        return patientMap.get(patientIC);
    }

    public Patient findPatientByStudentID(String studentID) {
        if (studentID == null || studentID.isEmpty()) {
            return null;
        }
        
        return patientMap.get(studentID);
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

    // Returns arrays of patients filtered by gender
    public Patient[] getPatientsByGender(char gender) {
        Object[] allPatients = patientMap.getValues();
        
        // count matching patients
        int count = 0;
        for (Object obj : allPatients) {
            Patient p = (Patient) obj;
            if (Character.toUpperCase(p.getPatientGender()) == Character.toUpperCase(gender)) {
                count++;
            }
        }
        
        // fill the array
        Patient[] result = new Patient[count];
        int index = 0;
        for (Object obj : allPatients) {
            Patient p = (Patient) obj;
            if (Character.toUpperCase(p.getPatientGender()) == Character.toUpperCase(gender)) {
                result[index++] = p;
            }
        }
        
        return result;
    }

    public void sortPatientsByAge(Patient[] patients, boolean oldestFirst) {
        int n = patients.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Patient p1 = patients[j];
                Patient p2 = patients[j + 1];

                boolean shouldSwap = false;

                if (oldestFirst) {
                    // Descending: oldest first
                    if (p1.getPatientAge() < p2.getPatientAge()) {
                        shouldSwap = true;
                    }
                } else {
                    // Ascending: youngest first
                    if (p1.getPatientAge() > p2.getPatientAge()) {
                        shouldSwap = true;
                    }
                }

                if (shouldSwap) {
                    // Swap
                    patients[j] = p2;
                    patients[j + 1] = p1;
                }
            }
        }
    }

    // Returns an array of patients aged 31 and above
    public Patient[] getPatientsAbove30() {
        Object[] allPatients = patientMap.getValues();
        
        // Count how many are 31+
        int count = 0;
        for (int i = 0; i < allPatients.length; i++) {
            Patient p = (Patient) allPatients[i];
            if (p.getPatientAge() > 30) {
                count++;
            }
        }
        
        // Fill the array
        Patient[] result = new Patient[count];
        int index = 0;
        for (int i = 0; i < allPatients.length; i++) {
            Patient p = (Patient) allPatients[i];
            if (p.getPatientAge() > 30) {
                result[index++] = p;
            }
        }
        
        return result;
    }

    // Removes all patients aged 31+
    public void removePatientsAbove30() {
        Patient[] toRemove = getPatientsAbove30();
        for (Patient p : toRemove) {
            patientMap.remove(p.getPatientIC());
        }
    }

}