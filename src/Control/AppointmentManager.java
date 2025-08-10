package Control;

import adt.Heap;
import Entity.Appointment;
import Entity.Consultation;
import Entity.Doctor;
import adt.LinkedHashMap;
import adt.List;
import adt.Queue;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Appointment Manager - handles appointment booking, validation,
 * and next available consultation slot search using min-heap.
 * 
 * Author: calve
 */
public class AppointmentManager {
    private final Heap<Appointment> apptQueue;
    private final LinkedHashMap<String, Queue<Appointment>> missAppt;
    private final LinkedHashMap<String, List<Consultation>> consultLog;
    private final DoctorManager docManager;
    private final QueueManager queueManager;
    
    private final LocalTime WORK_START = LocalTime.of(8, 0);   // 08:00
    private final LocalTime WORK_END = LocalTime.of(20, 0);    // 17:00 //RMB CHANGEEEEEEEEEE

    
    public AppointmentManager(LinkedHashMap<String, Queue<Appointment>> missAppt, LinkedHashMap<String, List<Consultation>> consultLog, DoctorManager docManager, QueueManager queueManager) {
        this.apptQueue = new Heap<>(false);
        this.missAppt = missAppt;
        this.consultLog = consultLog;
        this.docManager = docManager;
        this.queueManager = queueManager;
        refreshHeapFromConsultations();
    }
    
    private void refreshHeapFromConsultations() {
        String[] allDocID = docManager.peekAllDoctorID(); // doctor IDs
        Appointment consultAppt = null;

        for (int i = 0; i < allDocID.length; i++) { // loop over doctors
            List<Consultation> consultations = consultLog.get(allDocID[i]);

            if (consultations == null) continue; // skip doctor if no consults

            for (int j = 1; j <= consultations.size(); j++) {
                consultAppt = consultations.get(j);

                if (consultAppt == null) continue; // skip null entries

                if (consultAppt.getDateTime() != null && !apptQueueContains(consultAppt)) {
                    apptQueue.insert(consultAppt);                 
                }
            }
        }
        queueManager.loadVisit();
    }

    // Helper to check if apptQueue already has this appointment
    private boolean apptQueueContains(Appointment appt) {
        for (int i = 0; i < apptQueue.size(); i++) {
            Appointment existing = apptQueue.get(i);
            if (existing != null && existing.equals(appt)) { 
                return true; // Found duplicate
            }
        }
        return false;
    }

    public boolean isWithinWorkingHours(LocalDateTime time) {
        LocalTime appointmentTime = time.toLocalTime();
        return !appointmentTime.isBefore(WORK_START) && !appointmentTime.isAfter(WORK_END);
    }

    public LocalDateTime findNextAvailableSlot() {
        LocalDateTime now = LocalDateTime.now();

        // Dates to check: 1 week, 1 month, 3 months later
        LocalDate[] baseDates = new LocalDate[] {
            now.toLocalDate().plusDays(1), //TESTING PURPOSE
            now.toLocalDate().plusWeeks(1),
            now.toLocalDate().plusMonths(1),
            now.toLocalDate().plusMonths(3)
        };

        for (LocalDate date : baseDates) {
            LocalDateTime currentSlot = LocalDateTime.of(date, WORK_START);

            while (!currentSlot.toLocalTime().isAfter(WORK_END)) {
                boolean conflict = false;

                // Check for time conflict with existing appointments
                for (int i = 0; i < apptQueue.size(); i++) {
                    Appointment appt = apptQueue.get(i); 
                    if (appt.getDateTime().equals(currentSlot)) {
                        conflict = true;
                        break;
                    }
                }

                if (!conflict) return currentSlot;
                currentSlot = currentSlot.plusMinutes(30); // try next 30-min slot
            }
        }

        return null; 
    }
    
    public Appointment findPatienInfo(String ic){
        for(int i=0; i< apptQueue.size();i++){
            Appointment appt = apptQueue.get(i);
            if (appt.getPatient().getPatientIC().equals(ic)){
                return appt;
            }
        }
        return null;
    }

    public boolean bookAppointment(Appointment consultAppt, LocalDateTime dateTime) {
        if (consultAppt instanceof Consultation) {
            Consultation newConsult = (Consultation) consultAppt;
            
            if(dateTime != null){
                if (!isWithinWorkingHours(dateTime)) {
                    System.out.println("Time is outside working hours (08:00 to 17:00).");
                    return false;
                }
            
                for (int i = 0; i < apptQueue.size(); i++) {
                    Appointment appt = apptQueue.get(i);
                    if (appt.getDateTime().equals(dateTime)) {
                        System.out.println("Slot already booked.");
                        return false;
                    }
                }
            }
            
            int index = findOldApptIndex(newConsult);       
            System.out.println(index);
            String doctorId = newConsult.getDoctor().getDoctorID();
            List<Consultation> consultations = consultLog.get(doctorId);
            String oldID = consultations.get(index).getID();
            
            // Create a new Consultation with the new date
            Consultation consult = new Consultation(
                    oldID,
                newConsult.getSeverity(),
                newConsult.getPatient(),
                newConsult.getDisease(),
                newConsult.getNotes(),
                newConsult.getDoctor(),
                dateTime
            );                 

            if(index != -1) consultations.replace(index, consult);
            refreshHeapFromConsultations();
            return true;
        }   
        return false;
    }
    
    private List<Consultation> findOldAppt(Appointment oldAppt){
        String doctorId = oldAppt.getDoctor().getDoctorID();
        List<Consultation> consultations = consultLog.get(doctorId);
        return consultations;
    }
    
    private int findOldApptIndex(Appointment oldAppt){
        int index = -1;
        List<Consultation> consultations = findOldAppt(oldAppt);
    
        if(consultations == null) return -1;
        for (int i = 1; i <= consultations.size(); i++) {
            Consultation consult = consultations.get(i);
            if (consult == null) {
                System.out.println("Consultation at index " + i + " is null!");
                continue; // Skip nulls safely
            }
            if (consult.getPatient().getPatientIC().equals(oldAppt.getPatient().getPatientIC()) &&
                consult.getDoctor().getDoctorID().equals(oldAppt.getDoctor().getDoctorID())) {
                index = i;
                break;
            }
        }
        return index;
    }
    
    public boolean updateAppointment(Appointment oldAppt, LocalDateTime newDateTime) {
        int index = findOldApptIndex(oldAppt);
        List<Consultation> consultations = findOldAppt(oldAppt);
        String oldID = consultations.get(index).getID();
            if(oldAppt instanceof Consultation oldConsult && index != -1){
                Consultation newAppt = new Consultation(oldID,oldConsult.getSeverity(), oldConsult.getPatient(), oldConsult.getDisease(), oldConsult.getNotes(), oldConsult.getDoctor(), newDateTime); 
                consultations.replace(index, newAppt);
                apptQueue.remove(oldAppt);
                refreshHeapFromConsultations();
                return true;
            }
        return false;
    }    
    
    public boolean cancelAppointment(Appointment appt){
        int index = findOldApptIndex(appt);
        List<Consultation> consultations = findOldAppt(appt);
        String oldID = consultations.get(index).getID();
            if(appt instanceof Consultation oldConsult && index != -1){
                Consultation newAppt = new Consultation(oldID,oldConsult.getSeverity(), oldConsult.getPatient(), oldConsult.getDisease(), oldConsult.getNotes(), oldConsult.getDoctor(), null); 
                consultations.replace(index, newAppt);
                apptQueue.remove(appt);
                refreshHeapFromConsultations();
                return true;
            }
        return false;
    }

    public void displayAllAppointments() {
        refreshHeapFromConsultations();
        apptQueue.display();
    }
    
    public void displayAllAppointmentByDoctor(String docId){
        refreshHeapFromConsultations();
        boolean found = false;
        for (int i = 0; i < apptQueue.size(); i++) {
            Appointment appt = apptQueue.get(i);
            if (appt.getDoctor().getDoctorID().equalsIgnoreCase(docId)) {
                System.out.println(appt); 
                found = true;
            }
        }
        if(!found){
            System.out.println("No appointment found");
        }
    }
    
    public Heap<Appointment> getAppointmentHeap() {
        refreshHeapFromConsultations();
        return apptQueue;
    }

    public Appointment getIncomingAppointment(String docId) {
        refreshHeapFromConsultations();
        Appointment earliest = null;

        for (int i = 0; i < apptQueue.size(); i++) {
            Appointment appt = apptQueue.get(i);
            if (appt.getDoctor().getDoctorID().equalsIgnoreCase(docId) && appt.getDateTime().isAfter(LocalDateTime.now())) {
                if (earliest == null) {
                    earliest = appt;
                }
            }
        }

        return earliest;
    }

    public int totalAppointments(String docId) {
        refreshHeapFromConsultations();
        int totalAppt = 0;
        for (int i = 0; i < apptQueue.size(); i++) {
            Appointment appt = apptQueue.get(i);
            if (appt.getDoctor().getDoctorID().equalsIgnoreCase(docId) && appt.getDateTime().isAfter(LocalDateTime.now())) {
                totalAppt++;
            }
        }
        return totalAppt;
    }
    
    public void checkMissedAppt(String docId) {
        if (!missAppt.containsKey(docId)) {
            missAppt.put(docId, new Queue<>());
        }

        Queue<Appointment> docQueue = missAppt.get(docId);

        for (int i = apptQueue.size() - 1; i >= 0; i--) {
            Appointment appt = apptQueue.get(i);

            if (appt.getDoctor().getDoctorID().equalsIgnoreCase(docId)
                    && (appt.getDateTime().isBefore(LocalDateTime.now()) 
                    || appt.getDateTime().equals(LocalDateTime.now()))) {

                // Check for duplicates 
                boolean exists = false;
                for (int j = 0; j < docQueue.size(); j++) {
                    Appointment existing = docQueue.peek();
                    if (existing.equals(appt)) { 
                        exists = true;
                    }
                }

                if (!exists) {
                    docQueue.enqueue(appt);
                }
                apptQueue.remove(appt);
            }
        }
    }

    public int getNumMissedAppt(String docId) {
        if (missAppt.containsKey(docId)) {
            return missAppt.get(docId).size();
        }
        return 0;
    }
    
    public void displayAllMissedAppt(Doctor doc) {
        if (missAppt.containsKey(doc.getDoctorID())) {
            missAppt.display();
        }
    }
    
    public Appointment getMissedAppt(Doctor doc, String changedIC) {
        Appointment found = null;
        if (missAppt.containsKey(doc.getDoctorID())) {
            found = missAppt.get(doc.getDoctorID()).peek();
            if(found.getPatient().getPatientIC().equals(changedIC)){
                return found;
            }
        }
        return null;
    }
    
    public boolean removeMissedAppt(Doctor doc, String patientIC) {
        if (!missAppt.containsKey(doc.getDoctorID())) return false;

        Queue<Appointment> originalQueue = missAppt.get(doc.getDoctorID());
        Queue<Appointment> tempQueue = new Queue<>();
        boolean removed = false;

        while (!originalQueue.isEmpty()) {
            Appointment appt = originalQueue.dequeue();
            if (!removed && appt.getPatient().getPatientIC().equals(patientIC)) {
                removed = true; 
            } else {
                tempQueue.enqueue(appt);
            }
        }

        missAppt.put(doc.getDoctorID(), tempQueue);
        return removed;
    }
}
