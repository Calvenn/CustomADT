package Boundary;
import Control.QueueManager;
import Control.PatientManager;
import Control.DoctorManager;
import Entity.Patient;
import Entity.Visit;
import adt.Heap;
import java.util.Scanner;


public class PatientManagementUI {
    private Heap<Visit> visitQueue;
    private DoctorManager docManager;
    private QueueManager queueManager;
    private PatientManager patientManager;
    private Scanner scanner;

    public PatientManagementUI(Heap<Visit> sharedVisitQueue, QueueManager queueManager, DoctorManager docManager) {
        this.visitQueue = sharedVisitQueue;
        this.docManager = docManager;
        this.queueManager = queueManager;
        this.patientManager = new PatientManager();
        this.scanner = new Scanner(System.in);
    }

    public void patientMenu() {
        int choice;
        do {
            displayMenu();
            choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    handleNewPatientRegistration();
                    break;
                case 2:
                    handleVisitRegistration(); // Updated to call the visit registration method
                    break;
                case 3:
                    handleSearchPatient();
                    break;
                case 4:
                    handleUpdatePatient();
                    break;
                case 5:
                    handleDeletePatient();
                    break;
                case 6:
                    handleDisplayAllPatients();
                    break;
                case 7:
                    handlePatientStatistics();
                    break;
                case 8:
                    handleClearAllPatients();
                    break;
                case 9:
                    System.out.println("\nThank you for using Patient Management System!");
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
            
            if (choice != 9) {
                pauseForUser ();
            }
        } while (choice != 9);
    }

    private void displayMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("    PATIENT MANAGEMENT SYSTEM");
        System.out.println("=".repeat(40));
        System.out.println("1. Add New Patient");
        System.out.println("2. Register Visit");
        System.out.println("3. Search Patient");
        System.out.println("4. Update Patient Information");
        System.out.println("5. Delete Patient");
        System.out.println("6. Display All Patients");
        System.out.println("7. Patient Statistics");
        System.out.println("8. Clear All Patients");
        System.out.println("9. Exit");
        System.out.println("=".repeat(40));
    }

    public void patientTableHeader() {
        System.out.println(String.format("| %-15s | %-20s | %-15s | %-5s | %-8s | %-40s |\n", "IC", "Name", "Phone Number", "Age", "Gender", "Address"));
        System.out.print("-".repeat(111));
    }

    private void handleNewPatientRegistration() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("    NEW PATIENT REGISTRATION");
        System.out.println("=".repeat(35));

        String ic = getStringInput("Enter IC number: ");
        
        if (ic.trim().isEmpty()) {
            System.out.println("\nIC number cannot be empty!");
            return;
        }

        Patient existing = patientManager.findPatientByIC(ic);
        if (existing != null) {
            System.out.println("\nPatient already exists!");
            patientManager.displayPatientDetails(existing);
            return;
        }

        String name = getStringInput("Enter name: ");
        String phone = getStringInput("Enter phone number: ");
        int age = getIntInput("Enter age: ");
        char gender = getGenderInput();
        String address = getStringInput("Enter address: ");

        Patient newPatient = patientManager.registerNewPatient(ic, name, phone, age, gender, address);
        if (newPatient != null) {
            System.out.println("\nPatient registered successfully!");
            patientManager.displayPatientDetails(newPatient);
        } else {
            System.out.println("\nCannot register new patient.");
        }
    }

    private void handleVisitRegistration() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("    VISIT REGISTRATION");
        System.out.println("=".repeat(35));

        VisitRegistrationUI visitUI = new VisitRegistrationUI(visitQueue, queueManager, patientManager);
        visitUI.visitsMenu();
    }

    private void handleSearchPatient() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("      SEARCH PATIENT");
        System.out.println("=".repeat(30));
        
        String ic = getStringInput("Enter IC number to search: ");
        
        if (ic.trim().isEmpty()) {
            System.out.println("\nIC number cannot be empty!");
            return;
        }
        
        Patient patient = patientManager.findPatientByIC(ic);
        if (patient != null) {
            System.out.println("\nPatient found!");
            patientManager.displayPatientDetails(patient);
        } else {
            System.out.println("\nPatient not found with IC: " + ic);
        }
    }

    private void handleUpdatePatient() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("    UPDATE PATIENT INFORMATION");
        System.out.println("=".repeat(35));
        
        String ic = getStringInput("Enter IC number of patient to update: ");
        
        if (ic.trim().isEmpty()) {
            System.out.println("\nIC number cannot be empty!");
            return;
        }
        
        Patient existingPatient = patientManager.findPatientByIC(ic);
        if (existingPatient == null) {
            System.out.println("\nPatient not found with IC: " + ic);
            return;
        }
        
        System.out.println("\nCurrent patient details:");
        patientManager.displayPatientDetails(existingPatient);
        
        System.out.println("\nEnter new information (press Enter to keep current value):");
        
        String newName = getOptionalStringInput("New name [" + existingPatient.getPatientName() + "]: ");
        String newPhone = getOptionalStringInput("New phone [" + existingPatient.getPatientPhoneNo() + "]: ");
        String ageStr = getOptionalStringInput("New age [" + existingPatient.getPatientAge() + "]: ");
        String genderStr = getOptionalStringInput("New gender [" + existingPatient.getPatientGender() + "]: ");
        String newAddress = getOptionalStringInput("New address [" + existingPatient.getPatientAddress() + "]: ");
        
        // Use existing values if no new input provided
        String finalName = newName.isEmpty() ? existingPatient.getPatientName() : newName;
        String finalPhone = newPhone.isEmpty() ? existingPatient.getPatientPhoneNo() : newPhone;
        int finalAge = ageStr.isEmpty() ? existingPatient.getPatientAge() : Integer.parseInt(ageStr);
        char finalGender = genderStr.isEmpty() ? existingPatient.getPatientGender() : genderStr.toUpperCase().charAt(0);
        String finalAddress = newAddress.isEmpty() ? existingPatient.getPatientAddress() : newAddress;
        
        Patient updatedPatient = new Patient(ic, finalName, finalPhone, finalAge, finalGender, finalAddress);
        
        if (patientManager.updatePatient(ic, updatedPatient)) {
            System.out.println("\nPatient information updated successfully!");
            patientManager.displayPatientDetails(updatedPatient);
        } else {
            System.out.println("\nFailed to update patient information.");
        }
    }

    private void handleDeletePatient() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("      DELETE PATIENT");
        System.out.println("=".repeat(30));
        
        String ic = getStringInput("Enter IC number of patient to delete: ");
        
        if (ic.trim().isEmpty()) {
            System.out.println("\nIC number cannot be empty!");
            return;
        }
        
        Patient patient = patientManager.findPatientByIC(ic);
        if (patient == null) {
            System.out.println("\nPatient not found with IC: " + ic);
            return;
        }
        
        System.out.println("\nPatient to be deleted:");
        patientManager.displayPatientDetails(patient);
        
        String confirmation = getStringInput("\nAre you sure you want to delete this patient? (YES/no): ");
        
        if (confirmation.equalsIgnoreCase("YES")) {
            Patient removedPatient = patientManager.removePatient(ic);
            if (removedPatient != null) {
                System.out.println("\nPatient deleted successfully!");
            } else {
                System.out.println("\nFailed to delete patient.");
            }
        } else {
            System.out.println("\nPatient deletion cancelled.");
        }
    }

    private void handleDisplayAllPatients() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("        ALL PATIENTS");
        System.out.println("=".repeat(35));
        
        if (patientManager.isEmpty()) {
            System.out.println("\nNo patients in the system.");
        } else {
            patientManager.displayAllPatients();
        }
    }

    private void handlePatientStatistics() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("      PATIENT STATISTICS");
        System.out.println("=".repeat(35));
        
        int totalPatients = patientManager.getTotalPatients();
        System.out.println("Total Patients: " + totalPatients);
        
        if (totalPatients == 0) {
            System.out.println("No patients in the system.");
            return;
        }
    }

    private void handleClearAllPatients() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("      CLEAR ALL PATIENTS");
        System.out.println("=".repeat(35));
        
        if (patientManager.isEmpty()) {
            System.out.println("\nNo patients to clear.");
            return;
        }
        
        System.out.println("Current total patients: " + patientManager.getTotalPatients());
        String confirmation = getStringInput("\nAre you sure you want to clear ALL patients? This cannot be undone! (Y/N): ");
        
        if (confirmation.equalsIgnoreCase("Y")) {
            patientManager.clearAllPatients();
            System.out.println("\nAll patients have been cleared from the system.");
        } else {
            System.out.println("\nClear operation cancelled.");
        }
    }

    // Helper methods for input handling
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private String getOptionalStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine(); // Clear buffer
                return value;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private char getGenderInput() {
        while (true) {
            String input = getStringInput("Enter gender (M/F): ");
            if (!input.isEmpty()) {
                char gender = input.toUpperCase().charAt(0);
                if (gender == 'M' || gender == 'F') {
                    return gender;
                }
            }
            System.out.println("Invalid input. Please enter 'M' for Male or 'F' for Female.");
        }
    }

    private void pauseForUser () {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
