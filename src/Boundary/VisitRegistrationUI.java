package Boundary;
import Control.PatientManager;
import Control.QueueManager;
import Control.VisitHistoryManager;
import Entity.Doctor;
import Entity.Visit;
import Entity.Severity;

import exception.TryCatchThrowFromFile;
import exception.ValidationHelper;

/**
 *
 * @author NgPuiYin
 */
public class VisitRegistrationUI {
    private QueueManager queueManager;
    private PatientManager patientManager;
    private VisitsHistoryUI visitsHistoryUI;

    public VisitRegistrationUI(QueueManager queueManager, PatientManager patientManager, VisitHistoryManager historyManager) {
        this.queueManager = queueManager;
        this.patientManager = patientManager;
        this.visitsHistoryUI = new VisitsHistoryUI(historyManager);
    }

    public void visitsMenu() {
        int choice;
        do {
            displayVisitsMenu();
            choice = ValidationHelper.inputValidatedChoice(0, 7, "your choice");

            switch (choice) {
                case 1 -> handleVisitRegistration();
                case 2 -> displayQueue();
                case 3 -> searchVisitsFunctionality();
                case 4 -> handleEmergencyOverride();
                case 5 -> visitsHistoryUI.displayHistoricalVisits();
                case 6 -> handleSummaryReports();
                case 7 -> displayLiveQueueStatus();
            }

        } while (choice != 0);
    }

    private void displayVisitsMenu() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("      QUEUE MANAGEMENT MENU");
        System.out.println("=".repeat(35));
        System.out.println("1. Register New Visit");
        System.out.println("2. Display Current Queue");
        System.out.println("3. Search Today's Visits");
        System.out.println("4. Emergency Override");
        System.out.println("5. Display Historical Visits");
        System.out.println("6. Reports");
        System.out.println("7. Live Queue Status");
        System.out.println("0. Exit");
        System.out.println("=".repeat(35));
    }

    public void visitsTableHeader() {
        System.out.println("-".repeat(107));
        System.out.printf("| %-10s | %-14s | %-20s | %-17s | %-30s |%n","Visit ID", "Severity", "Doctor", "Register Time", "Symptoms");
        System.out.println("-".repeat(107));
    }

    public void handleVisitRegistration() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("        Walkin Visit Registration");
        System.out.println("=".repeat(40));

        String ic = ValidationHelper.inputValidatedIC("Enter patient IC number");
        var patient = patientManager.findPatientByIC(ic);
        if (patient == null) {
            System.out.println("\nPatient not found. Please register the patient first.");
            return;
        }

        System.out.println("\nPatient found: \n" + patient);
        boolean isLifeThreatening = askLifeThreatening();
        String symptoms = ValidationHelper.inputValidatedString("\nPlease describe the symptoms: ");
        Doctor doctor = queueManager.findMinWorkloadDoc();
        Visit visit = queueManager.createVisit(doctor,patient, symptoms, isLifeThreatening, false);
        System.out.println("\nVisit Registration Successful!");
        visitsTableHeader();
        System.out.println(visit);

        displayLiveQueueStatus();
    }

    private boolean askLifeThreatening() {
        char input = ValidationHelper.inputValidateYesOrNo("Is this symptom potentially life-threatening?");
        return input == 'Y';
    }   

    public void displayQueue() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("        Current Visit Queue");
        System.out.println("=".repeat(35));
        if (queueManager.isEmpty()) {
            System.out.println("\nQueue is empty.");
            return;
        }
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
            String input = ValidationHelper.inputValidatedString("Enter Visit/Appointment ID (e.g., V1000, A1000, or just 1000) or 0 to cancel: ").trim();
            if (input.equals("0")) {
                return null;
            }
            String visitId;
            if (input.matches("^[VA]\\d+$")) {
                visitId = input.toUpperCase();
            } else if (input.matches("^\\d+$")) {
                // Default to V if only number is entered
                visitId = "V" + input;
            } else {
                System.out.println("Invalid format. Please try again.");
                continue;
            }
            try {
                return TryCatchThrowFromFile.findObjectOrThrow(queueManager.getAllVisits(), Visit::getVisitId, visitId, "Visit", "ID");
            } catch (Exception e) {
                System.out.println("No visit found with ID " + visitId + ". Please try again.");
            }
        }
    }

    private void searchVisitsFunctionality() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("       Search Today's Visits");
        System.out.println("=".repeat(35));

        Visit result = promptForVisitById();

        if (result == null) {
            System.out.println("Search canceled.");
            return;
        }

        System.out.println("\nVisit found:");
        visitsTableHeader();
        System.out.println(result + "\n");
        System.out.println(result.getPatient());
    }

    private void handleEmergencyOverride() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("        Emergency Override");
        System.out.println("=".repeat(35));
        System.out.println("WARNING: This will move a patient to emergency priority!\n");

        Visit visit = promptForVisitById();
        if (visit == null) {   // user chose to cancel inside prompt
            System.out.println("\nEmergency override cancelled.");
            return;
        }
        String visitId = visit.getVisitId();
        String reasons = ValidationHelper.inputValidatedString("Enter reason for emergency override (additional symptoms): ");

        if (reasons.equals("0")) {
            System.out.println("\nEmergency override cancelled.");
            return;
        }

        boolean success = queueManager.emergencyOverride(visitId, reasons);
        if (success) {
            System.out.println("\nEmergency override successful for visit: " + visitId);
            displayLiveQueueStatus();
        } else {
            System.out.println("\nEmergency override failed for visit: " + visitId);
        }
    }

    private void displayLiveQueueStatus() {
        Visit next = queueManager.getNextPatient();
        Visit[] waiting = queueManager.getWaitingPatients();
        
        System.out.println("\n" + "-".repeat(57));
        System.out.println("| LIVE QUEUE STATUS" + " ".repeat(37) + "|");
        System.out.println("-".repeat(57));

        System.out.printf("| %-53s |\n", "Total patients in queue: " + queueManager.getQueueSize());
        System.out.println("-".repeat(57));

        System.out.printf("| %-25s | %-25s |\n", "Next Patient", "Waiting");
        System.out.println("-".repeat(57));

        int maxRows = Math.max(1, waiting.length);
        for (int i = 0; i < maxRows; i++) {
            String col1 = "";
            String col2 = "";
            if (i == 0 && next != null) col1 = next.getVisitId();
            if (i < waiting.length) col2 = waiting[i].getVisitId();
            System.out.printf("| %-25s | %-25s |\n", col1, col2);
        }
        System.out.println("-".repeat(57));

    }

   private void handleSummaryReports() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("        Summary Reports");
        System.out.println("=".repeat(35));
        System.out.println("1. Yearly Report");
        System.out.println("2. Severity Report");
        System.out.println("0. Exit");
        System.out.println("=".repeat(35));

        int choice = ValidationHelper.inputValidatedChoice(0, 2, "your choice");

        switch (choice) {
            case 1 -> visitsHistoryUI.displayYearlyReport();
            case 2 -> visitsHistoryUI.displaySeverityReport();
            case 0 -> System.out.println("Returning to main menu...");
        }
    }
}