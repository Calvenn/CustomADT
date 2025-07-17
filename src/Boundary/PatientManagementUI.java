package Boundary;
import Control.PatientManager;
import Control.QueueManager;
import Entity.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PatientManagementUI {
    private PatientManager patientManager;
    private QueueManager queueManager;
    private Scanner scanner;

    public PatientManagementUI() {
        patientManager = new PatientManager();
        queueManager = new QueueManager();
        scanner = new Scanner(System.in);
    }

    public void patientMenu() {
        int choice;
        do {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            switch (choice) {
                case 1:
                    // Add new patient
                    handleNewPatientRegistration();
                    break;
                case 2:
                    // Register Visit
                    handleVisitRegistration();
                    break;
                case 3:
                    System.out.println("\nThank you for using Patient Management System!");
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        } while (choice != 3);
    }

    private void displayMenu() {
        System.out.println("\n=== Patient Management System ===");
        System.out.println("1. Add New Patient");
        System.out.println("2. Register Visit");
        System.out.println("3. Exit");
        System.out.print("\nEnter your choice: ");
    }

    private void handleNewPatientRegistration() {
        Patient newPatient = patientManager.registerNewPatient();
        if (newPatient != null) {
            // Automatically proceed to visit registration for new patient
            System.out.println("\nProceeding to visit registration...");
            registerVisit(newPatient);
        }
    }

    private void handleVisitRegistration() {
        System.out.print("\nEnter patient IC number: ");
        String ic = scanner.nextLine();

        Patient patient = patientManager.findPatientByIC(ic);
        if (patient != null) {
            patientManager.displayPatientDetails(patient);
            registerVisit(patient);
        } else {
            System.out.println("\nPatient not found! Please register as new patient first.");
        }
    }

    private void registerVisit(Patient patient) {
        // Call QueueManager to handle visit registration
        queueManager.addVisit(patient.getPatientPhoneNo());  // QueueManager will handle symptoms input
        queueManager.displayQueue();  // Show current queue status
    }
    
}