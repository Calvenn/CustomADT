package Control;
import Entity.Visit;
import Entity.Patient;
import Entity.Severity;
import Entity.Doctor;
import Control.DoctorManager;
import Boundary.ConsultationUI;
import adt.ADTHeap;
import java.time.LocalDate;

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
    private DoctorManager docManager;
    public boolean isEmpty() {
        return visitQueue.isEmpty();
    }

    public QueueManager(ADTHeap<Visit> sharedQueue, DoctorManager docManager) {
        this.visitQueue = sharedQueue;
        this.docManager = docManager;
        queueNumber = 1000;
        
    }

    public Visit createVisit(Patient patient, String symptoms, boolean isLifeThreatening) {
        String visitId = generateVisitId();
        Severity severityLevel = Entity.Symptoms.assessSeverity(symptoms, isLifeThreatening);
      
        Doctor doctor = docManager.getMinWorkDoctor();
        Visit visit = new Visit(visitId, patient, symptoms, severityLevel, doctor);
        docManager.updateDoctor(doctor);
        visitQueue.insert(visit);
        return visit;
    }

    public int getQueueSize() {
        return visitQueue.size();
    }

    private String generateVisitId() {
        return "V" + String.format("%03d", queueNumber++);
    }

    //do only show visit id, severity level, and symptoms
    public void displayQueueDetails() {
        visitQueue.display();
    }
    
    public Visit peekRootVisit(){
        return visitQueue.peekRoot();
    }
    
    public Visit extractRootVisit(){
        return visitQueue.extractRoot();
    }
}
