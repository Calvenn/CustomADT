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
        patientManager = new PatientManager();
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
                    handleNewPatientRegistration();
                    break;
                case 2:
                    VisitRegistrationUI visitUI = new VisitRegistrationUI(visitQueue, queueManager, patientManager);
                    visitUI.handleVisitRegistration();
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
        System.out.println("\n=== New Patient Registration ===");

        System.out.print("Enter IC number: ");
        String ic = scanner.nextLine();

        Patient existing = patientManager.findPatientByIC(ic);
        if (existing != null) {
            System.out.println("\nPatient already exists!");
            patientManager.displayPatientDetails(existing);
            return;
        }

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter gender (M/F): ");
        char gender = scanner.nextLine().trim().toUpperCase().charAt(0);

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        Patient newPatient = patientManager.registerNewPatient(ic, name, phone, age, gender, address);
        if (newPatient != null) {
            System.out.println("\nPatient registered successfully!");
        } else {
            System.out.println("Error: Cannot register new patient.");
        }
    }
}
