package Control;
import Entity.Visit;
import Entity.Severity;
import Entity.Symptoms;
import adt.ADTHeap;
import java.util.Scanner;

/*1. Initialize max heap
2. Add visit to the queue
    - generate visitId
    - record symptoms and severity level
    - create a Visit object
    - Add the Visit object to the queue using insert()
    - EMERGENCY (3) will be at root
    - URGENT (2) next priority
    - MILD (1) lowest priority
3. Assign the queue number to the patient based on the severity level
    - visitQueue automatically maintains priority based on severity

4. Get next patient: visitQueue.extractRoot() - most severe patient "EMERGENCY"
5. display all patients in the queue
6. display queue size
7. List the top patient in the queue - FIFO
 */

public class QueueManager {
    private ADTHeap<Visit> visitQueue;
    private int queueNumber;
    private Scanner scanner;

    public QueueManager() {
        visitQueue = new ADTHeap<>(true); // Max-heap for priority queue
        queueNumber = 1000;
        scanner = new Scanner(System.in);
    }

    public void addVisit(String patientPhoneNo) {
        // 1. Get symptoms from user
        System.out.println("\n=== Symptoms Registration ===");
        System.out.println("Enter patient's symptoms:");
        System.out.println("(e.g., fever, chest pain, breathing difficulty)");
        String symptoms = scanner.nextLine();
        
        // 2. Generate visit ID 
        String visitId = generateVisitId();
        
        // 3. Assess severity based on symptoms using Symptoms entity
        Severity severityLevel = Symptoms.assessSeverity(symptoms);
        
        // 4. Create visit
        Visit visit = new Visit(visitId, patientPhoneNo, symptoms, severityLevel);
        visitQueue.insert(visit);  // Queue automatically maintains priority based on severity
        
        // Display registration details
        System.out.println("\nVisit Registration Details:");
        System.out.println("Visit ID: " + visitId);
        System.out.println("Registration Time: " + visit.getRegistrationTime());
        System.out.println("Symptoms: " + symptoms);
        System.out.println("Severity Level: " + severityLevel);
        System.out.println("Priority Status: " + Symptoms.getPriorityDescription(severityLevel));
    }

    private String generateVisitId() {
        return "V" + String.format("%03d", queueNumber++);
    }

    public void displayQueue() {
        if (visitQueue.isEmpty()) {
            System.out.println("\nQueue is empty");
            return;
        }
        
        System.out.println("\n=== Current Queue Status ===");
        System.out.println("Total patients in queue: " + visitQueue.size());
        visitQueue.display();
    }

    public int getQueueSize() {
        return visitQueue.size();
    }
    
    public void displaySeverityGuidelines() {
        System.out.println("\n=== Severity Assessment Guidelines ===");
        System.out.println("EMERGENCY symptoms (Highest Priority):");
        for (String symptom : Symptoms.getEmergencySymptoms()) {
            System.out.println("  - " + symptom);
        }
        
        System.out.println("\nURGENT symptoms (Medium Priority):");
        for (String symptom : Symptoms.getUrgentSymptoms()) {
            System.out.println("  - " + symptom);
        }
        
        System.out.println("\nMILD symptoms (Regular Priority):");
        System.out.println("  - All other symptoms not listed above");
    }
}