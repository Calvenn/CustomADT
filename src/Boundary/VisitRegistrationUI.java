package Boundary;
import Control.PatientManager;
import Control.QueueManager;
import Control.VisitHistoryManager;
import Entity.Visit;
import Entity.Severity;

import exception.InvalidInputException;
import exception.TryCatchThrowFromFile;
import exception.ValidationHelper;
import exception.ValidationUtility;

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
            choice = ValidationHelper.inputValidatedChoice(0, 9, "your choice");

            switch (choice) {
                case 1 -> handleVisitRegistration();
                case 2 -> displayQueue();
                case 3 -> displayQueueComposition();
                case 4 -> searchVisitsFunctionality();
                case 5 -> handleEmergencyOverride();
                case 6 -> visitsHistoryUI.displayHistoricalVisits();
                case 7 -> handleSummaryReports();
                case 8 -> displayLiveQueueStatus();
                case 9 -> handleProcessNextPatient();
            }

        } while (choice != 0);
    }

    private void displayVisitsMenu() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("      QUEUE MANAGEMENT MENU");
        System.out.println("=".repeat(35));
        System.out.println("1. Register New Visit");
        System.out.println("2. Display Current Queue");
        System.out.println("3. Queue Composition by Severity");
        System.out.println("4. Search Functionality");
        System.out.println("5. Emergency Override");
        System.out.println("6. Display Historical Visits");
        System.out.println("7. Reports");
        System.out.println("8. Live Queue Status");
        System.out.println("9. Process Next Patient");
        System.out.println("0. Exit");
        System.out.println("=".repeat(35));
    }

    public void visitsTableHeader(){
        System.out.println("-".repeat(101));
        System.out.println(String.format("| %-10s | %-14s | %-14s | %-17s | %-30s |", "Visit ID", "Severity", "Doctor", "Register Time", "Symptoms"));
        System.out.println("-".repeat(101));
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
        Visit visit = queueManager.createVisit(patient, symptoms, isLifeThreatening, false);
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
        System.out.println("=".repeat(35));
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
            try {
                int numberPart = ValidationHelper.inputValidatedPositiveInt("Enter Visit Number (e.g., 1000) or 0 to cancel: ");
                if (numberPart == 0) {
                    return null; // user chose to exit
                }
                String visitId = "V" + numberPart;
                return TryCatchThrowFromFile.findObjectOrThrow(
                    queueManager.getAllVisits(),
                    Visit::getVisitId,
                    visitId,
                    "Visit",
                    "ID"
                );
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
    }

    private void searchVisitsFunctionality() {
        Visit result = promptForVisitById();
        System.out.println("\nVisit found:");
        visitsTableHeader();
        System.out.println(result + "\n");
        System.out.println("Patient Details:" + result.getPatient());
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
        Visit processing = queueManager.getCurrentlyProcessing();
        Visit next = queueManager.getNextPatient();
        Visit[] waiting = queueManager.getWaitingPatients();
        
        int width = 58;  
        String line = "-".repeat(width);
        String title = "Live Queue Status";
        System.out.println("\n" + line);
        System.out.printf("| %-"+(width-4)+"s |%n", title);  // auto pad
        System.out.println(line);

        System.out.printf("| %-"+(width-4)+"s |%n", "Total patients in queue: " + queueManager.getQueueSize());
        System.out.println(line);

        System.out.printf("| %-16s | %-16s | %-16s |%n", "Processing", "Next Patient", "Waiting");
        System.out.println(line);

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
        System.out.println("\n" + "=".repeat(35));
        System.out.println("      Process Next Patient");
        System.out.println("=".repeat(35));
        Visit nextVisit = queueManager.peekRootVisit();
        if (nextVisit == null) {
            System.out.println("No patients in queue.");
            return;
        }
        System.out.println("Next patient in queue: " + nextVisit.getVisitId());
        char confirm = ValidationHelper.inputValidateYesOrNo("Process this patient?");
        if (confirm == 'Y') {
            Visit processedVisit = queueManager.processNextPatient();
            System.out.println("Visit " + processedVisit.getVisitId() + " is now being processed.");
        } else {
            System.out.println("Processing cancelled.");
        }

        displayLiveQueueStatus();
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