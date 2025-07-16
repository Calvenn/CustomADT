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
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ConsultationUI() {
        manager = new AppointmentManager();
        scanner = new Scanner(System.in);
        
        loadDummyAppointments(); //FOR TESTING PURPOSE
    }
    
    private void loadDummyAppointments() {
        manager.bookAppointment("Alice", "012-1231234", "Dr. Tan", LocalDateTime.now().plusWeeks(1).withHour(9).withMinute(0));
        manager.bookAppointment("Bob", "012-2231234", "Dr. Lim", LocalDateTime.now().plusWeeks(1).withHour(9).withMinute(30));
        manager.bookAppointment("Charlie","012-3231234", "Dr. Tan", LocalDateTime.now().plusWeeks(1).withHour(10).withMinute(0));
    }

    public void consultMenu() {
        while (true) {
            System.out.println("Total Appointment: " + manager.totalAppointments());
            System.out.println("Incoming Appointment: " + manager.getIncomingAppointment());
            
            System.out.println("\n--- Consultation Menu ---");
            System.out.println("1. Handle Consultation");
            System.out.println("2. Appointments");
            System.out.println("3. Back");

            System.out.print("Choose > ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            switch (choice) {
                case 1 -> consultationUI();
                case 2 -> apptMenu();
                case 3 -> {
                    System.out.println("Returning to main menu...");
                    return;
                }
                default -> System.out.println("Invalid choice.\n");
            }
        }
    }
    
    public void apptMenu() {
        while (true) {
            System.out.println("Total Appointment: " + manager.totalAppointments());
            System.out.println("Incoming Appointment: " + manager.getIncomingAppointment());
            
            System.out.println("\n--- Consultation Menu ---");
            System.out.println("1. View Appointments");
            System.out.println("2. Update Appointment");
            System.out.println("3. Delete Appointment");

            System.out.print("Choose > ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            switch (choice) {
                case 1 -> manager.displayAllAppointments();
                case 2 -> updateAppointmentUI();
                case 3 -> cancelAppointmentUI();
                default -> System.out.println("Invalid choice.\n");
            }
        }
    }
    
    private void consultationUI() {
        Appointment current = manager.getNextAppointment(); // from patient module

        if (current == null) {
           System.out.println("No more patients in queue.");
            return;
        }

        System.out.println("\n Now Consulting:");
        System.out.println("Patient: " + current.getPatientName());
        System.out.println("Doctor: " + current.getDoctorName());
        System.out.println("Time: " + current.getTime().format(formatter));

        //go consultation management
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
            case 1 -> bookAppointmentUI(); 
            //case 2 -> ;
            //case 3 -> ;
            //case 4 -> ;
            default -> System.out.println("Invalid choice.");
        }
    }

    private void bookAppointmentUI() {
        boolean success = false;
        
        suggestNextAvailableSlot();
        System.out.print("Enter patient name: ");
        String patient = scanner.nextLine();
        
        System.out.print("Enter patient phone number: ");
        String phoneNum = scanner.nextLine();

        System.out.print("Enter doctor name: ");
        String doctor = scanner.nextLine();

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

        success = manager.bookAppointment(patient, phoneNum,doctor, time);
        if (success) {
            System.out.println("Appointment booked successfully!");
        } else {
            System.out.println("Failed to book appointment.");
        }
    }
    
    private void updateAppointmentUI() {
        System.out.print("Enter patient phone number: ");
        String phoneNum = scanner.nextLine();
        
        Appointment oldData = manager.findPatienInfo(phoneNum, formatter);
        
        if(oldData != null){
            System.out.print("Enter new appointment time (yyyy-MM-dd HH:mm): ");
            String newTimeStr = scanner.nextLine();
            LocalDateTime newTime = LocalDateTime.parse(newTimeStr, formatter);
            
            if(getConfirmation("Are you sure want to change appointment to " + newTime.format(formatter) + " ?")){
                boolean success = manager.updateAppointment(oldData, newTime, formatter);
                System.out.println(success ? "Appointment updated." : "Not found or update failed. Please try again");  
            }
        } else {
            System.out.println("Phone Number " + phoneNum + " not found. Please try again");
        }
    }

    private void cancelAppointmentUI() {
        System.out.print("Enter patient phone number: ");
        String phoneNum = scanner.nextLine();

        Appointment appt = manager.findPatienInfo(phoneNum, formatter);

        if(appt != null){
            if(getConfirmation("Are you sure want to cancel patient " + appt.getPatientName() + " at " + appt.getTime() + " ?")){
                boolean success = manager.cancelAppointment(appt, formatter);
                System.out.println(success ? "Appointment cancelled." : "Not found. Please try again");
            }
        } else {
            System.out.println("Phone Number " + phoneNum + " not found. Please try again");
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
    
    private boolean getConfirmation(String message) {
        while (true) {
            System.out.print(message + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Please enter 'y' or 'n'");
            }
        }
    }
}
