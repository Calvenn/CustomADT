package Control;

import adt.Heap;
import Entity.Appointment;
import Entity.Doctor;
import Entity.Patient;
import adt.LinkedHashMap;
import adt.Queue;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Appointment Manager - handles appointment booking, validation,
 * and next available consultation slot search using min-heap.
 * 
 * Author: calve
 */
public class AppointmentManager {
    private final Heap<Appointment> appointmentHeap;
    private LinkedHashMap<String, Queue<Appointment>> missAppt;
    private final LocalTime WORK_START = LocalTime.of(8, 0);   // 08:00
    private final LocalTime WORK_END = LocalTime.of(20, 0);    // 17:00 //RMB CHANGEEEEEEEEEE

    public AppointmentManager() {
        appointmentHeap = new Heap<>(false); 
    }
    
    public AppointmentManager(Heap<Appointment> sharedHeap, LinkedHashMap<String, Queue<Appointment>> missAppt) {
        this.appointmentHeap = sharedHeap;
        this.missAppt = missAppt;
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
                for (int i = 0; i < appointmentHeap.size(); i++) {
                    Appointment appt = appointmentHeap.get(i); 
                    if (appt.getTime().equals(currentSlot)) {
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
    
    public Appointment findPatienInfo(String phoneNum, DateTimeFormatter f){
        for(int i=0; i< appointmentHeap.size();i++){
            Appointment appt = appointmentHeap.get(i);
            if (appt.getPatient().getPatientPhoneNo().equals(phoneNum)){
                System.out.println("Patient Name: " + appt.getPatient().getPatientName());
                System.out.println("Current appointment: " + appt.getTime().format(f));
                return appt;
            }
        }
        return null;
    }

    public boolean bookAppointment(Patient patient, int severity ,LocalDateTime time, Doctor currentDoc) {
        if (!isWithinWorkingHours(time)) {
            System.out.println("Time is outside working hours (08:00 to 17:00).");
            return false;
        }

        // Check for conflict
        for (int i = 0; i < appointmentHeap.size(); i++) {
            Appointment appt = appointmentHeap.get(i);
            if (appt.getTime().equals(time)) {
                System.out.println("Slot already booked.");
                return false;
            }
        }

        Appointment newAppt = new Appointment(patient, currentDoc, severity, time);
        appointmentHeap.insert(newAppt);
        return true;
    }
    
    public boolean cancelAppointment(Appointment appt, DateTimeFormatter f){
        return appointmentHeap.remove(appt);
    }
    
    public boolean updateAppointment(Appointment oldAppt, LocalDateTime newTime, DateTimeFormatter f) {
        Appointment newAppt = new Appointment(
            oldAppt.getPatient(),
            oldAppt.getDoctor(),
            oldAppt.getSeverity(),
            newTime
        );
        return appointmentHeap.update(oldAppt, newAppt); // ✅ Fixed
    }

    public void displayAllAppointments() {
        appointmentHeap.display();
    }
    
    public void displayAllAppointmentByDoctor(String docId){
        boolean found = false;
        for (int i = 0; i < appointmentHeap.size(); i++) {
            Appointment appt = appointmentHeap.get(i);
            if (appt.getDoctor().getDoctorID().equalsIgnoreCase(docId)) {
                System.out.println(appt); 
                found = true;
            }
        }
        if(!found){
            System.out.println("No appointment found");
        }
    }

    public Appointment getIncomingAppointment(String docId) {
        Appointment earliest = null;

        for (int i = 0; i < appointmentHeap.size(); i++) {
            Appointment appt = appointmentHeap.get(i);
            if (appt.getDoctor().getDoctorID().equalsIgnoreCase(docId)) {
                if (earliest == null) {
                    earliest = appt;
                }
            }
        }

        return earliest;
    }

    public int totalAppointments(String docId) {
        int totalAppt = 0;
        for (int i = 0; i < appointmentHeap.size(); i++) {
            Appointment appt = appointmentHeap.get(i);
            if (appt.getDoctor().getDoctorID().equalsIgnoreCase(docId)) {
                totalAppt++;
            }
        }
        return totalAppt;
    }

    public Appointment getAppointment(int index) {
        return appointmentHeap.get(index);
    }
    
    public Heap<Appointment> getAppointmentHeap() {
        return appointmentHeap;
    }
    
    public void checkMissedAppt(String docId) {
        if (!missAppt.containsKey(docId)) {
            missAppt.put(docId, new Queue<>());
        }

        Queue<Appointment> docQueue = missAppt.get(docId);

        for (int i = 0; i < appointmentHeap.size(); i++) {
            Appointment appt = appointmentHeap.get(i);
            if (appt.getDoctor().getDoctorID().equalsIgnoreCase(docId)
                    && (appt.getTime().isBefore(LocalDateTime.now()) || appt.getTime().equals(LocalDateTime.now()))) {
                docQueue.enqueue(appt);
                appointmentHeap.remove(appt);
                System.out.println("Missed appointment moved to queue for " + docId);
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
                removed = true; // skip adding it to tempQueue
            } else {
                tempQueue.enqueue(appt);
            }
        }

        missAppt.put(doc.getDoctorID(), tempQueue);
        return removed;
    }
}
