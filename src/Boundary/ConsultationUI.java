package Boundary;

import Control.AppointmentManager;
import Entity.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Boundary class to handle user interface for consultation booking.
 * Handles input, displays options, and interacts with AppointmentManager.
 * 
 * Author: calve
 */
public class ConsultationUI {

    private AppointmentManager manager;
    private Scanner scanner;

    public ConsultationUI() {
        manager = new AppointmentManager();
        scanner = new Scanner(System.in);
        
        loadDummyAppointments(); //FOR TESTING PURPOSE
    }
    
    private void loadDummyAppointments() {
        manager.bookAppointment("Alice", "Dr. Tan", LocalDateTime.now().plusWeeks(1).withHour(9).withMinute(0));
        manager.bookAppointment("Bob", "Dr. Lim", LocalDateTime.now().plusWeeks(1).withHour(9).withMinute(30));
        manager.bookAppointment("Charlie", "Dr. Tan", LocalDateTime.now().plusWeeks(1).withHour(10).withMinute(0));
    }

    public void consultMenu() {
        while (true) {
            System.out.println("Total Appointment: " + manager.totalAppointments());
            System.out.println("Incoming Appointment: " + manager.getIncomingAppointment());
            
            System.out.println("\n--- Consultation Menu ---");
            System.out.println("1. Handle Consultation");
            System.out.println("2. View Appointments");
            System.out.println("3. Back");

            System.out.print("Choose > ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            switch (choice) {
                case 1 -> handleConsultation();
                case 2 -> manager.displayAllAppointments();
                case 3 -> {
                    System.out.println("Returning to main menu...");
                    return;
                }
                default -> System.out.println("Invalid choice.\n");
            }
        }
    }
    
    private void handleConsultation() {
        Appointment current = manager.getNextAppointment(); // from patient module

        if (current == null) {
           System.out.println("No more patients in queue.");
            return;
        }

        System.out.println("\n Now Consulting:");
        System.out.println("Patient: " + current.getPatientName());
        System.out.println("Doctor: " + current.getDoctorName());
        System.out.println("Time: " + current.getTime());

        // 5. Store Consultation Details
        System.out.print("Enter diagnosis/notes: ");
        String notes = scanner.nextLine();
    

        System.out.println("Next action:");
        System.out.println("1. Schedule follow-up appointment");
        System.out.println("2. Send to treatment");// USE ARRAY METHOD TO HANDLE WHAT PATIENT NEED DO
        System.out.println("3. Send to pharmacy");// USE ARRAY METHOD TO HANDLE WHAT MED WANT TO GET
        System.out.println("4. Done");
    
        int action = scanner.nextInt();
        scanner.nextLine();

        switch (action) {
            case 1 -> bookAppointment(); // reuse
            //case 2 -> ;
            //case 3 -> ;
            //case 4 -> ;
            default -> System.out.println("❌ Invalid choice.");
        }
    }

    private void bookAppointment() {
        boolean success = false;
        
        suggestNextAvailableSlot();
        System.out.print("Enter patient name: ");
        String patient = scanner.nextLine();

        System.out.print("Enter doctor name: ");
        String doctor = scanner.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime time = null;

        while (true && !success) {
            System.out.print("Enter appointment date and time (yyyy-MM-dd HH:mm): ");
            String input = scanner.nextLine();
            try {
                time = LocalDateTime.from(formatter.parse(input));
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format! Example: 2024-12-20 08:00");
            }
        }

        success = manager.bookAppointment(patient, doctor, time);
        if (success) {
            System.out.println("Appointment booked successfully!");
        } else {
            System.out.println("Failed to book appointment.");
        }
    }

    private void suggestNextAvailableSlot() {
        LocalDate today = LocalDate.now();
        LocalDateTime slot = manager.findNextAvailableSlot();
        if (slot != null) {
            System.out.println("Earliest available slot today: " +
                    slot.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        } else {
            System.out.println("No available slots today.");
        }
    }
}
