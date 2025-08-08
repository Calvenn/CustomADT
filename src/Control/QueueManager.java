package Control;
import Entity.Visit;
import Entity.Patient;
import Entity.Severity;
import Entity.Doctor;
import Control.PatientManager;
import adt.Heap;
import adt.LinkedHashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QueueManager {
    private Heap<Visit> visitQueue;
    private int queueNumber;
    private DoctorManager docManager;
    private PatientManager patientManager; // Added PatientManager reference
    
    // Enhanced tracking features using custom ADTs
    private LinkedHashMap<Severity, Integer> severityCount;
    private static final int MAX_PROCESSED_VISITS = 1000;
    private CompletedVisit[] processedVisits;
    private int processedCount;
    private LinkedHashMap<String, CompletedVisit> completedVisits = new LinkedHashMap<>();

    private Visit currentlyProcessing;
    private Visit nextPatient;
    
    // Inner class for completed visits
    public static class CompletedVisit {
        private Visit visit;
        private LocalDateTime completionTime;
        private long consultationDuration; // in minutes
        private String doctorNotes;
        
        public CompletedVisit(Visit visit, LocalDateTime completionTime, long duration, String notes) {
            this.visit = visit;
            this.completionTime = completionTime;
            this.consultationDuration = duration;
            this.doctorNotes = notes;
        }
        
        public Visit getVisit() { return visit; }
        public LocalDateTime getCompletionTime() { return completionTime; }
        public long getConsultationDuration() { return consultationDuration; }
        public String getDoctorNotes() { return doctorNotes; }
        
        @Override
        public String toString() {
            return String.format("Visit: %s | Patient: %s | Completed: %s | Duration: %d min | Doctor: %s",
                visit.getVisitId(), visit.getPatient().getPatientName(), 
                completionTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                consultationDuration, visit.getDoctor().getDoctorName());
        }
    }

    // Updated constructor to include PatientManager
    public QueueManager(Heap<Visit> sharedQueue, DoctorManager docManager, PatientManager patientManager) {
        this.visitQueue = sharedQueue;
        this.docManager = docManager;
        this.patientManager = patientManager; // Initialize PatientManager
        this.queueNumber = 1000;
        
        // Initialize tracking structures
        this.severityCount = new LinkedHashMap<>();
        this.processedVisits = new CompletedVisit[MAX_PROCESSED_VISITS];
        this.processedCount = 0;
        
        // Initialize severity counters
        for (Severity severity : Severity.values()) {
            severityCount.put(severity, 0);
        }
    }

    public boolean isEmpty() {
        return visitQueue.isEmpty();
    }

    public Visit createVisit(Patient patient, String symptoms, boolean isLifeThreatening) {
        String visitId = generateVisitId();
        Severity severityLevel = Entity.Symptoms.assessSeverity(symptoms, isLifeThreatening);

        Doctor doctor = docManager.getMinWorkDoctor();
        Visit visit = new Visit(visitId, patient, symptoms, severityLevel, doctor);
        docManager.updateDoctor(doctor);
        visitQueue.insert(visit);

        refreshQueueComposition();
        updateNextPatient();

        return visit;
    }

    public int getQueueSize() {
        return visitQueue.size();
    }

    private String generateVisitId() {
        return "V" + String.format("%03d", queueNumber++);
    }

    public void displayQueueDetails() {
        visitQueue.display();
    }
    
    public Visit peekRootVisit() {
        return visitQueue.peekRoot();
    }
    
    public Visit extractRootVisit() {
        Visit visit = visitQueue.extractRoot();
        if (visit != null) {
            refreshQueueComposition();
        }
        return visit;
    }
    
    public void refreshQueueComposition() {
        // Reset counters
        for (Severity severity : Severity.values()) {
            severityCount.put(severity, 0);
        }
        
        // Count current queue
        for (int i = 0; i < visitQueue.size(); i++) {
            Visit visit = visitQueue.get(i);
            if (visit != null) {
                Severity severity = visit.getSeverityLevel();
                Integer currentCount = severityCount.get(severity);
                severityCount.put(severity, currentCount != null ? currentCount + 1 : 1);
            }
        }
    }
    
    public int[] handleQueueComposition() {
        refreshQueueComposition();

        int[] counts = new int[Severity.values().length];

        for (int i = 0; i < Severity.values().length; i++) {
            Severity severity = Severity.values()[i];
            Integer count = severityCount.get(severity);
            counts[i] = (count != null) ? count : 0;
        }

        return counts;
    }

    public Visit searchByVisitId(String visitId) {
        for (int i = 0; i < visitQueue.size(); i++) {
            Visit visit = visitQueue.get(i);
            if (visit != null && visit.getVisitId().equals(visitId)) {
                return visit;
            }
        }
        return null;
    }
    
    public boolean emergencyOverride(String visitId, String reasons) {
        for (int i = 0; i < visitQueue.size(); i++) {
            Visit visit = visitQueue.get(i);

            if (visit != null && visit.getVisitId().equals(visitId)) {
                //append new symptoms
                String updatedSymptoms = visit.getSymptoms();
                if (reasons != null && !reasons.isEmpty()) {
                    updatedSymptoms += ", " + reasons;
                }

                //create updated visit with EMERGENCY severity
                Visit updatedVisit = new Visit(visit.getVisitId(), visit.getPatient(), updatedSymptoms, Severity.EMERGENCY, visit.getDoctor());

                boolean updated = visitQueue.update(visit, updatedVisit);

                if (updated) {
                    refreshQueueComposition();
                    updateNextPatient();
                    return true;
                }
            }
        }
        return false;
    }

    public Visit processNextPatient() {
        if (currentlyProcessing == null && !visitQueue.isEmpty()) {
            // Set the first patient as currently processing (don't remove from queue)
            currentlyProcessing = visitQueue.get(0);
            
            // Set next patient if queue has more than one patient
            if (visitQueue.size() > 1) {
                nextPatient = visitQueue.get(1);
            } else {
                nextPatient = null;
            }
            
            return currentlyProcessing;
        }
        return null;
    }

    private void updateNextPatient() {
        if (currentlyProcessing == null) {
            // No one processing, next patient is top of heap
            if (!visitQueue.isEmpty()) {
                nextPatient = visitQueue.get(0);
            } else {
                nextPatient = null;
            }
        } else {
            // Someone processing, next patient is second in queue
            if (visitQueue.size() > 1) {
                nextPatient = visitQueue.get(1);
            } else {
                nextPatient = null;
            }
        }
    }

    public Visit getCurrentlyProcessing() {
        return currentlyProcessing;
    }

    public Visit getNextPatient() {
       return nextPatient;
    }

    public Visit[] getWaitingPatients() {
        //count how many patients are actually waiting
        int waitingCount = 0;
        for (int i = 0; i < visitQueue.size(); i++) {
            Visit visit = visitQueue.get(i);
            if (visit != currentlyProcessing && visit != nextPatient) {
                waitingCount++;
            }
        }
        
        Visit[] waiting = new Visit[waitingCount];
        
        //fill the array with waiting patients only
        int index = 0;
        for (int i = 0; i < visitQueue.size(); i++) {
            Visit visit = visitQueue.get(i);
            if (visit != currentlyProcessing && visit != nextPatient) {
                waiting[index] = visit;
                index++;
            }
        }
        
        return waiting;
    }

    public void completeCurrentPatient() {
        if (currentlyProcessing != null) {
            // The visit has been extracted by ConsultationManager, so we just clear our tracking
            currentlyProcessing = null;
            
            // Update next patient to be the new top of heap
            updateNextPatient();
            
            // Refresh queue composition after the visit has been removed
            refreshQueueComposition();
        }
    }

    // Add getter for the heap (so ConsultationManager can access it)
    public Heap<Visit> getVisitQueue() {
        return visitQueue;
    }
}