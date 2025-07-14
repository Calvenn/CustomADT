package Control;

import adt.ADTHeap;
import Entity.Appointment;
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

    private final ADTHeap<Appointment> appointmentHeap;
    private final LocalTime WORK_START = LocalTime.of(8, 0);   // 08:00
    private final LocalTime WORK_END = LocalTime.of(17, 0);    // 17:00

    public AppointmentManager() {
        appointmentHeap = new ADTHeap<>(false); // Min-heap: earliest appointment on top
    }

    /**
     * Check if appointment time is within working hours.
     */
    public boolean isWithinWorkingHours(LocalDateTime time) {
        LocalTime appointmentTime = time.toLocalTime();
        return !appointmentTime.isBefore(WORK_START) && !appointmentTime.isAfter(WORK_END);
    }

    /**
     * Find the next available appointment slot,
     * starting from 1 week later, then 1 month, then 3 months.
     */
    public LocalDateTime findNextAvailableSlot() {
        LocalDateTime now = LocalDateTime.now();

        // Dates to check: 1 week, 1 month, 3 months later
        LocalDate[] baseDates = new LocalDate[] {
            now.toLocalDate().plusDays(0), //TESTING PURPOSE
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

        return null; // No available slot found
    }

    /**
     * Book an appointment if valid.
     */
    public boolean bookAppointment(String patientName, String doctorName, LocalDateTime time) {
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

        Appointment newAppt = new Appointment(patientName, doctorName, time);
        appointmentHeap.insert(newAppt);
        return true;
    }

    /**
     * Display all booked appointments.
     */
    public void displayAllAppointments() {
        appointmentHeap.display();
    }

    public Appointment getIncomingAppointment() {
        return appointmentHeap.peekRoot();
    }
    
    public Appointment getNextAppointment() {
        return appointmentHeap.extractRoot();
    }

    /**
     * Get total number of appointments.
     */
    public int totalAppointments() {
        return appointmentHeap.size();
    }

    /**
     * Allow access to heap element by index (for conflict check).
     * Make sure `ADTHeap` has `get(int index)` method.
     */
    public Appointment getAppointment(int index) {
        return appointmentHeap.get(index);
    }
}
