package Control;
import Entity.Patient;
import java.util.Scanner;

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
    private Scanner scanner;

    public PatientManager() {
        patientList = new Entity.Patient[MAX_PATIENTS];
        totalPatients = 0;
        scanner = new Scanner(System.in);
    }
    
    public Patient registerNewPatient() {
        System.out.println("\n=== Patient Registration ===");
        
        // Get IC/Phone number first
        System.out.print("Enter IC number: ");
        String patientIC = scanner.nextLine();
        
        // Check if patient exists
        Patient existingPatient = findPatientByIC(patientIC);
        if (existingPatient != null) {
            System.out.println("\nPatient already exists!");
            displayPatientDetails(existingPatient);
            return existingPatient;  // Return existing patient for visit registration
        }
        
        // Continue with new patient registration
        System.out.print("Enter patient name: ");
        String name = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phoneNo = scanner.nextLine();
        
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        
        System.out.print("Enter gender (M/F): ");
        String gender = scanner.nextLine();
        
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        
        // Create and add new patient
        Patient newPatient = new Patient(patientIC, name, phoneNo, age, gender, address);
        addNewPatient(newPatient);
        
        System.out.println("\nPatient registered successfully!");
        return newPatient; 
    }

    public void addNewPatient(Entity.Patient patient) {
        if (totalPatients < MAX_PATIENTS) {
            patientList[totalPatients] = patient;
            totalPatients++;
        } else {
            System.out.println("Patient list is full. Cannot add new patient.");
        }

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
            System.out.println("\n=== Patient Details ===");
            System.out.println("IC: " + patient.getPatientIC());
            System.out.println("Name: " + patient.getPatientName());
            System.out.println("Phone: " + patient.getPatientPhoneNo());
            System.out.println("Age: " + patient.getPatientAge());
            System.out.println("Gender: " + patient.getPatientGender());
            System.out.println("Address: " + patient.getPatientAddress());
        } else {
            System.out.println("Patient not found!");
        }
    }
}