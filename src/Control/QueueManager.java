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
import java.time.LocalDateTime;

/**
 *
 * @author NgPuiYin
 */

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
    public QueueManager(Heap<Visit> sharedQueue, Heap<Appointment> apptQueue, DoctorManager docManager, LinkedHashMap<String, List<Consultation>> consultLog) {
        this.visitQueue = sharedQueue;
        this.docManager = docManager;
        this.queueNumber = 1000;
        
        // Initialize tracking structures
        this.severityCount = new LinkedHashMap<>();
        this.consultLog = consultLog;
        this.apptQueue = apptQueue;
        
        // Initialize severity counters
        for (Severity severity : Severity.values()) {
            severityCount.put(severity, 0);
        }
    }
    
    public void loadVisit() {
        List<Doctor> doc = docManager.getDoctorsByDept("CONSULT");

        for (int i = 1; i <= doc.size(); i++) { 
            List<Consultation> consultations = consultLog.get(doc.get(i).getID());
            if (consultations == null) continue;

            for (int j = 1; j <= consultations.size(); j++) {
                Consultation consultAppt = consultations.get(j);
                    if (consultAppt == null || consultAppt.getDateTime() == null) continue;

                    boolean apptExists = false;
                    for (int k = 0; k < apptQueue.size(); k++) {
                        Appointment existing = apptQueue.get(k);
                        if (existing instanceof Consultation) {
                            Consultation existingC = (Consultation) existing;
                            if (existingC.getID().equals(consultAppt.getID())) {
                                apptExists = true;
                                break;
                            }
                        }
                    }

                    if (!apptExists) {
                        apptQueue.insert(consultAppt);
                    }

                    boolean visitExists = false;
                    for (int k = 0; k < visitQueue.size(); k++) {
                        Visit v = visitQueue.get(k);
                        if (v != null && v.getPatient().equals(consultAppt.getPatient())) {
                            visitExists = true;
                            break;
                        }
                    }

                    if (!visitExists && LocalDateTime.now().isBefore(consultAppt.getDateTime().plusMinutes(15)) && 
                            consultAppt.getDateTime().toLocalDate().equals(LocalDate.now())) {
                        createVisit(consultAppt.getDoctor(), consultAppt.getPatient(), consultAppt.getDisease(), false, true);
                    }
                     
            }       
        }
    }
    
    public boolean isEmpty() {
        return visitQueue.isEmpty();
    }

    public Visit createVisit(Doctor doctor, Patient patient, String symptoms, boolean isLifeThreatening, boolean isAppt) {
        String visitId = generateId(isAppt);
        Severity severityLevel = Entity.Symptoms.assessSeverity(symptoms, isLifeThreatening);

        Visit visit = new Visit(visitId, patient, symptoms, severityLevel, doctor);
        docManager.updateDoctorInc(doctor);
        visitQueue.insert(visit);
        refreshQueueComposition();
        updateNextPatient();

        return visit;
    }
    
    public Doctor findMinWorkloadDoc(){
        return docManager.getMinWorkDoctorByDept("CONSULT");
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

    public Visit[] getAllVisits() {
        Visit[] visits = new Visit[visitQueue.size()];
        for (int i = 0; i < visitQueue.size(); i++) {
            visits[i] = visitQueue.get(i);
        }
        return visits;
    }

    // Add getter for the heap (so ConsultationManager can access it)
    public Heap<Visit> getVisitQueue() {
        return visitQueue;
    }
}