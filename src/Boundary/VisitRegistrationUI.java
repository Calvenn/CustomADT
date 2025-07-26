package Boundary;
import Control.PatientManager;
import Control.QueueManager;
import Entity.Visit;
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

    public void handleVisitRegistration() {
        System.out.println("\n=== Visit Registration ===");

        System.out.print("Enter patient IC number: ");
        String ic = scanner.nextLine().trim();

        var patient = patientManager.findPatientByIC(ic);
        if (patient == null) {
            System.out.println("\nPatient not found. Please register the patient first.");
            return;
        }

        System.out.println("\nPatient found:");
        System.out.println(patient);

        boolean isLifeThreatening = askLifeThreatening();

        System.out.println("\nPlease describe the symptoms:");
        String symptoms = scanner.nextLine();

        Visit visit = queueManager.createVisit(patient, symptoms, isLifeThreatening);

        System.out.println("\nVisit Registration Successful!");
        System.out.println("Visit ID: " + visit.getVisitId());
        System.out.println("Severity: " + visit.getSeverityLevel());
        System.out.println("Symptoms: " + visit.getSymptoms());

        // ✅ Show queue
        displayQueue();
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
        queueManager.displayQueueDetails();
    }
}
