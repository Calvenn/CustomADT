package Boundary;

import Control.AppointmentManager;
import Control.ConsultationManager;
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
    private AppointmentUI apptUI;
    private AppointmentManager apptManager;
    private ConsultationManager consultManager;
    private Scanner scanner;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ConsultationUI() {
        apptManager = new AppointmentManager();
        apptUI = new AppointmentUI(apptManager);
        consultManager = new ConsultationManager();
        scanner = new Scanner(System.in);
        
        loadDummyAppointments();
    }
    
    private void loadDummyAppointments() {
        apptManager.bookAppointment("Alice", "012-1231234", "Dr. Tan", 2, LocalDateTime.now().plusWeeks(1).withHour(9).withMinute(0));
        apptManager.bookAppointment("Bob", "012-2231234", "Dr. Lim", 2, LocalDateTime.now().plusWeeks(1).withHour(9).withMinute(30));
        apptManager.bookAppointment("David", "012-2231234", "Dr. Ang", 3, LocalDateTime.now().plusWeeks(1).withHour(10).withMinute(0));
    }    

    /*DOCTOR MUST LOGIN FIRST TO VIEW OWN INCOMING QUEUE AND APPT AFTER DOC MODULE */
    public void consultMainMenu() {
        while (true) {
            System.out.println("Total Appointment: " + apptManager.totalAppointments());
            System.out.println("Incoming Appointment: " + apptManager.getIncomingAppointment());
            //get incoming queue from patient & doc
            
            System.out.println("\n--- Consultation Menu ---");
            System.out.println("1. Handle Consultation");
            System.out.println("2. Appointments");
            System.out.println("3. Back");

            System.out.print("Choose > ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            switch (choice) {
                case 1 -> consultationMenu();
                case 2 -> apptUI.apptMenu();
                case 3 -> {
                    System.out.println("Returning to main menu...");
                    return;
                }
                default -> System.out.println("Invalid choice.\n");
            }
        }
    }
    
    private void consultationMenu() {
        System.out.println("\n--- Appointment Menu ---");
        System.out.println("1. Consultation Record");
        System.out.println("2. Consultation History");
        System.out.println("3. Back");
        
        System.out.print("Choose > ");
        int choice = scanner.nextInt();
        scanner.nextLine(); 
        
        switch (choice) {
            case 1 -> consultRecord(); 
            case 2 -> consultManager.displayAllRecords();
            case 3 -> {
                System.out.println("Returning to main menu...");
                return;
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void consultRecord(){
        // how to handle both walk in and appt patient?????
        // and one from patient module & doc(QUEUE)
        // call dispatchNextPatient after integrate with patient queue
        // will extract at dispatchPatient func
        Appointment current = apptManager.getNextAppointment(); 

        if (current == null) {
           System.out.println("No more patients in queue.");
            return;
        }

        System.out.println("\n Now Consulting:");
        System.out.println("Patient: " + current.getPatientName());
        System.out.println("Doctor: " + current.getDoctorName());
        System.out.println("Current Severity: " + current.getSeverity());
        System.out.println("Time: " + current.getTime().format(formatter));
        
        if(consultManager.consultationRecord()){
            System.out.println("Record successfully");
            
            System.out.println("Next action:");
            System.out.println("1. Schedule follow-up appointment");
            System.out.println("2. Send to treatment");// USE ARRAY METHOD TO HANDLE WHAT PATIENT NEED DO
            System.out.println("3. Send to pharmacy");// USE ARRAY METHOD TO HANDLE WHAT MED WANT TO GET
            System.out.println("4. Done");

            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 1 -> apptUI.bookAppointmentUI(current.getPatientName(), current.getPhoneNum(), current.getDoctorName(), current.getSeverity()); 
                //case 2 -> ;
                //case 3 -> ;
                //case 4 -> ;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    
}
