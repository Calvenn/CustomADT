package Boundary;

import Control.AppointmentManager;
import Control.ConsultationManager;
import Control.DoctorManager; //TEMP
import Control.TreatmentManager;

import Entity.Doctor;
import Entity.Appointment;
import Entity.Severity;
import Entity.Visit;
import Entity.Treatment;
import Entity.TreatmentAppointment;
import adt.ADTHeap;
import adt.ADTQueue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Boundary class to handle user interface for consultation booking.
 * Handles input, displays options, and interacts with AppointmentManager.
 * 
 * Author: calve
 */
public class ConsultationUI {
    private final ADTHeap<Visit> queue;
    private ADTQueue<TreatmentAppointment> treatmentQueue;
    private AppointmentUI apptUI;
    private AppointmentManager apptManager;
    private ConsultationManager consultManager;
    private DoctorManager docManager;
    private TreatmentManager trtManager;
    private Doctor currentDoc = null;
    private Scanner scanner;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ConsultationUI(ADTHeap<Visit> queue, DoctorManager docManager, AppointmentManager apptManager, ADTQueue<TreatmentAppointment> treatmentQueue) {
        this.queue = queue;
        this.docManager = docManager;
        this.apptManager = apptManager;
        this.apptUI = new AppointmentUI(apptManager); 
        this.consultManager = new ConsultationManager(queue, apptManager.getAppointmentHeap(), docManager, treatmentQueue);
        this.trtManager = new TreatmentManager();
        this.scanner = new Scanner(System.in);
    }
    
    //handle in doctor ui
    private boolean doctorLogin() {
        System.out.println("\n--- Doctor Login ---");
        System.out.print("Enter your ID (e.g. D001): ");
        String docId = scanner.nextLine().trim();
        
        currentDoc = docManager.findDoctor(docId);

        if (currentDoc != null) {
            System.out.println("Login successful. Welcome, " + docId + "!");
            return true;
        } else {
            System.out.println("No appointments found for that doctor. Please try again.");
            return false;
        }
    }

    
    public void consultMainMenu() {       
        while (true && (doctorLogin() || currentDoc != null)) {
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
    
    private void consultRecord() {
        Object currentPatient = consultManager.dispatchNextPatient(currentDoc); 

        if (currentPatient == null) {
            System.out.println("No more patients in queue.");
            return;
        }

        System.out.println("\n--- Now Consulting ---");

        if (currentPatient instanceof Appointment appointment) {
            System.out.println("Type     : Appointment");
            System.out.println("Patient  : " + appointment.getPatientName());
            System.out.println("Doctor   : " + appointment.getDoctor().getDoctorName());
            System.out.println("Severity : " + appointment.getSeverity());
            System.out.println("Time     : " + appointment.getTime().format(formatter));

            if (consultManager.consultationRecord()) {
                System.out.println("Record saved.");

                System.out.println("Next action:");
                System.out.println("1. Schedule follow-up appointment");
                System.out.println("2. Send to treatment");
                System.out.println("3. Send to pharmacy");
                System.out.println("4. Done");

                int action = scanner.nextInt();
                scanner.nextLine();

                switch (action) {
                    case 1 -> {
                        apptUI.bookAppointmentUI(
                                appointment.getPatientName(), 
                                appointment.getPhoneNum(), 
                                appointment.getDoctor().getDoctorID(), 
                                appointment.getSeverity(), 
                                currentDoc);
                        if(isToPharmacy()) consultManager.toPharmacy();
                    }
                    case 2 -> toTreatmentUI(currentDoc);
                    case 3 -> consultManager.toPharmacy();
                    default -> System.out.println("Done.");
                }
            }

        } else if (currentPatient instanceof Visit visit) {
            System.out.println("Type     : Walk-In");
            System.out.println("Patient  : " + visit.getPatient().getPatientName());
            System.out.println("Doctor   : " + visit.getDoctor().getDoctorName());
            System.out.println("Severity : " + visit.getSeverityLevel().getSeverity());
            System.out.println("Symptoms : " + visit.getSymptoms());

            if (consultManager.consultationRecord()) {
                System.out.println("Record saved.");

                System.out.println("Next action:");
                System.out.println("1. Schedule follow-up appointment");
                System.out.println("2. Send to treatment");
                System.out.println("3. Send to pharmacy");
                System.out.println("4. Done");

                int action = scanner.nextInt();
                scanner.nextLine();

                switch (action) {
                    case 1 ->{ 
                        apptUI.bookAppointmentUI(
                                visit.getPatient().getPatientName(), 
                                visit.getPatient().getPatientPhoneNo(), 
                                visit.getDoctor().getDoctorName(), 
                                visit.getSeverityLevel().getSeverity(),
                                currentDoc);
                        if(isToPharmacy()) consultManager.toPharmacy();        
                    }
                    case 2 -> toTreatmentUI(currentDoc);
                    case 3 -> consultManager.toPharmacy();
                    default -> System.out.println("Done.");
                }
            }
        } else {
            System.out.println("Unknown patient type.");
        }
    }
    
    private void toTreatmentUI(Doctor doc){
        System.out.println("\n--- Select Treatment ---");
        trtManager.displayAllTreatments();
        
        System.out.println("Enter treatment name to assign: ");
        String name = scanner.nextLine();
        
        Treatment selected = trtManager.findTreatment(name);
        if (selected == null) {
            System.out.println("Treatment not found.");
            return;
        }
        
        System.out.println("Enter room: ");
        String room = scanner.nextLine();
        
        LocalDateTime time = LocalDateTime.now();
        
        System.out.println("Enter severity (1-3): "); //TEMP
        int sev = scanner.nextInt();
        Severity severity = Severity.fromValue(sev);
        consultManager.toTreatment(doc, selected, room, time ,severity);
    }
    
    private boolean isToPharmacy(){
        while(true){
            System.out.println("Does patient need to collect medicine? (Y/N)");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("Y")) return true;
            else if (input.equals("N")) return false;
            else System.out.println("Please enter Y or N only.");
        }
    }  
}
