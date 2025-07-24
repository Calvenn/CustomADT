package Control;

import adt.ADTHeap;
import Entity.Appointment;
import Entity.Doctor;
import Entity.Patient;
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
    private final ADTHeap<Appointment> appointmentHeap;
    private final LocalTime WORK_START = LocalTime.of(8, 0);   // 08:00
    private final LocalTime WORK_END = LocalTime.of(17, 0);    // 17:00

    public AppointmentManager() {
        appointmentHeap = new ADTHeap<>(false); 
    }
    
    public AppointmentManager(ADTHeap<Appointment> sharedHeap) {
        this.appointmentHeap = sharedHeap;
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

    public boolean bookAppointment(Patient patient,String doctorName, int severity ,LocalDateTime time, Doctor currentDoc) {
        System.out.println("Earliest available ");
        if (time.isBefore(LocalDateTime.now())) {
            System.out.println("Cannot book in the past.");
            return false;
        }

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
                1, //ammendmentttttttttttttttttttttttt
            newTime
        );
        return appointmentHeap.update(oldAppt, newAppt); // ✅ Fixed
    }

    public void displayAllAppointments() {
        appointmentHeap.display();
    }

    public Appointment getIncomingAppointment() {
        return appointmentHeap.peekRoot();
    }
    
    public Appointment getNextAppointment() {
        return appointmentHeap.extractRoot();
    }

    public int totalAppointments() {
        return appointmentHeap.size();
    }

    public Appointment getAppointment(int index) {
        return appointmentHeap.get(index);
    }
    
    public ADTHeap<Appointment> getAppointmentHeap() {
        return appointmentHeap;
    }
}

//HOW TO ENSURE SMOOTH FLOW BETWEEN CONSULTATION & PHARMANCY????