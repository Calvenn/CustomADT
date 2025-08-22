package Control;
import Entity.Visit;
import Entity.Patient;
import Entity.Severity;
import Entity.Doctor;
import Entity.Appointment;
import Entity.Consultation;

import adt.Heap;
import adt.LinkedHashMap;
import adt.List;

import java.time.LocalDate;

public class QueueManager {
    private Heap<Visit> visitQueue;
    private Heap<Appointment> apptQueue;
    
    private int queueNumber;
    private DoctorManager docManager;
    private VisitHistoryManager historyManager;
    
    // Enhanced tracking features using custom ADTs
    private LinkedHashMap<Severity, Integer> severityCount;
    private LinkedHashMap<String, List<Consultation>> consultLog;

    private Visit currentlyProcessing;
    private Visit nextPatient;

    // Updated constructor to include PatientManager
    public QueueManager(Heap<Visit> sharedQueue, DoctorManager docManager, LinkedHashMap<String, List<Consultation>> consultLog, VisitHistoryManager historyManager) {
        this.visitQueue = sharedQueue;
        this.docManager = docManager;
        this.queueNumber = 1000;
        
        // Initialize tracking structures
        this.severityCount = new LinkedHashMap<>();
        this.consultLog = consultLog;
        this.apptQueue = new Heap<>(false);

        this.historyManager = historyManager;
        
        // Initialize severity counters
        for (Severity severity : Severity.values()) {
            severityCount.put(severity, 0);
        }
    }
    
    public void loadVisit() {
        String[] allDocID = docManager.peekAllDoctorID();
        Appointment consultAppt = null;

        for (int i = 0; i < allDocID.length; i++) { 
            List<Consultation> consultations = consultLog.get(allDocID[i]);

            if (consultations == null) continue;

            for (int j = 1; j <= consultations.size(); j++) {
                consultAppt = consultations.get(j);

                if (consultAppt instanceof Consultation) {
                    if (consultAppt == null || consultAppt.getDateTime() == null) continue; 
                    Consultation appt = (Consultation) consultAppt;

                    if (consultAppt.getDateTime() != null) {
                        LocalDate apptDate = consultAppt.getDateTime().toLocalDate();
                        apptQueue.insert(appt);

                        if (apptDate.equals(LocalDate.now())) {
                            boolean exists = false;
                            for (int k = 0; k < visitQueue.size(); k++) { 
                                Visit v = visitQueue.get(k);
                                if (v != null && v.getPatient().equals(consultAppt.getPatient())) {
                                    exists = true;
                                    break;
                                }
                            }

                            if (!exists) {
                                createVisit(consultAppt.getPatient(), appt.getDisease(), false, true);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isEmpty() {
        return visitQueue.isEmpty();
    }

    public Visit createVisit(Patient patient, String symptoms, boolean isLifeThreatening, boolean isAppt) {
        String visitId = generateId(isAppt);
        Severity severityLevel = Entity.Symptoms.assessSeverity(symptoms, isLifeThreatening);

        Doctor doctor = docManager.getMinWorkDoctor();
        Visit visit = new Visit(visitId, patient, symptoms, severityLevel, doctor);
        docManager.updateDoctorInc(doctor);
        visitQueue.insert(visit);
        refreshQueueComposition();
        updateNextPatient();

        return visit;
    }

    public int getQueueSize() {
        return visitQueue.size();
    }

    private String generateId(boolean isAppt) {
        return isAppt? "A" + String.format("%03d", queueNumber++) : "V" + String.format("%03d", queueNumber++);
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
            // move patient to history
            historyManager.addHistoricalVisit(currentlyProcessing);

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