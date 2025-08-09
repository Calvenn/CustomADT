package Boundary;
import Control.PatientManager;
import Control.ConsultationManager;
import Control.QueueManager;
import Entity.Visit;
import Entity.Doctor;
import Entity.Severity;
import adt.Heap;
import java.util.Scanner;

public class VisitRegistrationUI {
    private Heap<Visit> visitQueue;
    private Scanner scanner;
    private QueueManager queueManager;
    private PatientManager patientManager;

    public VisitRegistrationUI(Heap<Visit> visitQueue, QueueManager queueManager, PatientManager patientManager) {
        this.visitQueue = visitQueue;
        this.queueManager = queueManager;
        this.patientManager = patientManager;
        this.scanner = new Scanner(System.in);
    }

    public void visitsMenu() {
        int choice;
        do {
            displayVisitsMenu();
            choice = getIntInput("Select an option: ");

            switch (choice) {
                case 1:
                    handleVisitRegistration();
                    break;
                case 2:
                    displayQueue();
                    break;
                case 3:
                    displayQueueComposition();
                    break;
                case 4:
                    searchVisitsFunctionality();
                    break;
                case 5:
                    handleEmergencyOverride();
                    break;
                case 6:
                    // handleSeverityFilter();
                    break;
                case 7:
                    // handlePatientHistory();
                    break;
                case 8:
                    // handleSummaryReports();
                    break;
                case 9:
                    displayLiveQueueStatus();
                    break;
                case 10:
                    handleProcessNextPatient();
                    break;
                case 0:
                    System.out.println("\nGoodbye!");
                    break;
                default:
                    System.out.println("\nInvalid option. Please try again.");
            }

            if (choice != 0) {
                pauseForUser();
            }
        } while (choice != 0);
    }

    private void displayVisitsMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("         QUEUE MANAGEMENT SYSTEM");
        System.out.println("=".repeat(50));
        System.out.println("1.  Register New Visit");
        System.out.println("2.  Display Current Queue");
        System.out.println("3.  Queue Composition by Severity");
        System.out.println("4.  Search Functionality");
        System.out.println("5.  Emergency Override");
        System.out.println("6.  Filter by Severity");
        System.out.println("7.  Patient Visit History");
        System.out.println("8.  Daily/Weekly Summaries");
        System.out.println("9.  Live Queue Status");
        System.out.println("10. Process Next Patient");
        System.out.println("0.  Exit");
        System.out.println("=".repeat(50));
    }

    public void visitsTableHeader(){
        System.out.println("-".repeat(101));
        System.out.println(String.format("| %-10s | %-14s | %-14s | %-17s | %-30s |", "Visit ID", "Severity", "Doctor", "Register Time", "Symptoms"));
        System.out.println("-".repeat(101));
    }

    public void handleVisitRegistration() {
        System.out.println("\n=== Visit Registration ===");
        String ic = getStringInput("Enter patient IC number: ");

        var patient = patientManager.findPatientByIC(ic);
        if (patient == null) {
            System.out.println("\nPatient not found. Please register the patient first.");
            return;
        }
        System.out.println("\nPatient found: \n" + patient);

        boolean isLifeThreatening = askLifeThreatening();
        String symptoms = getStringInput("\nPlease describe the symptoms:");

        Visit visit = queueManager.createVisit(patient, symptoms, isLifeThreatening);

        System.out.println("\nVisit Registration Successful!");
        visitsTableHeader();
        System.out.println(visit);

        displayLiveQueueStatus();
    }

    private boolean askLifeThreatening() {
        while (true) {
            System.out.print("Is this symptom potentially life-threatening? (Y/N): ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("Y")) return true;
            else if (input.equals("N")) return false;
            else System.out.println("Please enter Y or N only.");
        }
    }

    public void displayQueue() {
        if (queueManager.isEmpty()) {
            System.out.println("\nQueue is empty.");
            return;
        }
        System.out.println("\n=== Current Visit Queue ===");
        System.out.println("Total patients in queue: " + queueManager.getQueueSize());
        visitsTableHeader();
        queueManager.displayQueueDetails();
        displayQueueComposition();
    }

    private void displayQueueComposition() {
        int[] counts = queueManager.handleQueueComposition();

        boolean allZero = true;
        for (int count : counts) {
            if (count > 0) {
                allZero = false;
                break;
            }
        }

        if (allZero) {
            System.out.println("Queue is empty.");
        } else {
            Severity[] severities = Severity.values();
            for (int i = 0; i < severities.length; i++) {
                System.out.printf("%-10s: %2d patients\n", severities[i].name(), counts[i]);
            }
        }
    }

    private Visit promptForVisitById() {
        while (true) {
            String numberPart = getStringInput("Enter Visit Number (e.g., 1000): ");

            if (!numberPart.matches("\\d+")) {
                System.out.println("Invalid input. Please enter only numbers.\n");
                continue;
            }

            String visitId = "V" + numberPart;
            Visit result = queueManager.searchByVisitId(visitId);

            if (result != null) {
                return result; // Found
            } else {
                System.out.println("No visit found with ID: " + visitId + "\n");
            }
        }
    }

    private void searchVisitsFunctionality() {
        Visit result = promptForVisitById();
        System.out.println("\nVisit found:");
        visitsTableHeader();
        System.out.println(result + "\n");
        patientManager.displayPatientDetails(result.getPatient());
    }

    private void handleEmergencyOverride() {
        System.out.println("\n=== Emergency Override ===");
        System.out.println("WARNING: This will move a patient to emergency priority!");

        Visit visit = promptForVisitById();
        String visitId = visit.getVisitId();

        String reasons = getStringInput("Enter reason for emergency override (additional symptoms): ");
        boolean success = queueManager.emergencyOverride(visitId, reasons);

        if (success) {
            System.out.println("\nEmergency override successful for visit: " + visitId);
            displayLiveQueueStatus();
        } else {
            System.out.println("\nEmergency override failed for visit: " + visitId);
        }

        pauseForUser();
    }

    /*private void handleSeverityFilter() {
        System.out.println("\n=== Filter by Severity ===");
        System.out.println("Available severity levels:");
        
        Severity[] severities = Severity.values();
        for (int i = 0; i < severities.length; i++) {
            System.out.println((i + 1) + ". " + severities[i]);
        }
        
        System.out.print("Select severity level (1-" + severities.length + "): ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= severities.length) {
                Severity selectedSeverity = severities[choice - 1];
                queueManager.displayFilteredResults(selectedSeverity);
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
        
        pauseForUser();
    }*/

    /*private void handlePatientHistory() {
        System.out.println("\n=== Patient Visit History ===");
        System.out.print("Enter Patient IC: ");
        String patientIC = scanner.nextLine().trim();
        
        queueManager.displayPatientVisits(patientIC);
        
        pauseForUser();
    }*/

    private void displayLiveQueueStatus() {
        Visit processing = queueManager.getCurrentlyProcessing();
        Visit next = queueManager.getNextPatient();
        Visit[] waiting = queueManager.getWaitingPatients();

        System.out.println("\n=== Live Queue Status ===");
        System.out.println("Total patients in queue: " + queueManager.getQueueSize());
        System.out.println("-".repeat(58));
        System.out.printf("| %-16s | %-16s | %-16s |%n", "Processing", "Next Patient", "Waiting");
        System.out.println("-".repeat(58));

        int maxRows = Math.max(1, Math.max(1, waiting.length));
        for (int i = 0; i < maxRows; i++) {
            String col1 = (i == 0 && processing != null) ? processing.getVisitId() : "";
            String col2 = (i == 0 && next != null) ? next.getVisitId() : "";
            String col3 = (i < waiting.length) ? waiting[i].getVisitId() : "";
            System.out.printf("| %-16s | %-16s | %-16s |%n", col1, col2, col3);
        }

        System.out.println("-".repeat(58));
    }

    
    private void handleProcessNextPatient() {
        System.out.println("\n=== Process Next Patient ===");

        Visit nextVisit = queueManager.peekRootVisit();
        if (nextVisit == null) {
            System.out.println("No patients in queue.");
            return;
        }

        System.out.println("Next patient in queue: " + nextVisit.getVisitId());
        System.out.print("Process this patient? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("Y")) {
            Visit processedVisit = queueManager.processNextPatient(); // no doctor argument
            if (processedVisit != null) {
                System.out.println("Visit " + processedVisit.getVisitId() + " is now being processed.");
            } else {
                System.out.println("Unable to process patient.");
            }
        } else {
            System.out.println("Processing cancelled.");
        }

        displayLiveQueueStatus();
    }

    // Helper methods for input handling
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private void pauseForUser() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}