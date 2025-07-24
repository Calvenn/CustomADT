package Boundary;

import Control.AppointmentManager;
import Control.ConsultationManager;
import Control.DoctorManager; //TEMP
import Control.MedicineControl;
import Control.TreatmentManager;

import Entity.Doctor;
import Entity.Appointment;
import Entity.Medicine;
import Entity.Patient;
import Entity.Severity;
import Entity.Visit;
import Entity.Treatment;

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
    private final AppointmentUI apptUI;
    private final AppointmentManager apptManager;
    private final ConsultationManager consultManager;
    private final DoctorManager docManager;
    private final TreatmentManager trtManager;
    private final MedicineControl medControl;
    private static Doctor currentDoc = null;
    private final Scanner scanner;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ConsultationUI(DoctorManager docManager, AppointmentManager apptManager, ConsultationManager consultManager, TreatmentManager trtManager, MedicineControl medControl) {
        this.docManager = docManager;
        this.apptManager = apptManager;
        this.apptUI = new AppointmentUI(apptManager); 
        this.consultManager = consultManager;
        this.trtManager = trtManager;
        this.medControl = medControl;
        this.scanner = new Scanner(System.in);
    }
    
    //handle in doctor ui
    private boolean doctorLogin() {
        System.out.println("\n--- Doctor Login ---");
        System.out.print("Enter your ID (e.g. D001): ");
        String docId = scanner.nextLine().trim();
        
        currentDoc = docManager.findDoctor(docId);

        if (currentDoc != null) {
            System.out.println("Login successful. Welcome, " + currentDoc.getDoctorName() + "!");
            return true;
        } else {
            System.out.println("No doctor found. Please try again.");
            return false;
        }
    }
    
    public void consultMainMenu() {     
        if (currentDoc == null && !doctorLogin()) {
            System.out.println("Login failed.");
            return;
        }
        
        while (true) {
            System.out.println("Total Appointment: " + apptManager.totalAppointments());
            System.out.println("Incoming Appointment: " + apptManager.getIncomingAppointment());
            //get incoming queue from patient & doc
            
            System.out.println("\n--- Consultation Menu ---");
            System.out.println("1. Handle Consultation");
            System.out.println("2. Appointments");
            System.out.println("3. Back");

            System.out.print("Enter your choice: ");
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

        Patient patient = null;
        if (currentPatient instanceof Appointment appointment) {
            patient = appointment.getPatient();
            System.out.println("Type     : Appointment");
            System.out.println("Patient  : " + patient.getPatientName());
            System.out.println("Doctor   : " + appointment.getDoctor().getDoctorName());
            System.out.println("Severity : " + appointment.getSeverity());
            System.out.println("Time     : " + appointment.getTime().format(formatter));
        } else if (currentPatient instanceof Visit visit) {
            patient = visit.getPatient();
            System.out.println("Type     : Walk-In");
            System.out.println("Patient  : " + patient.getPatientName());
            System.out.println("Doctor   : " + visit.getDoctor().getDoctorName());
            System.out.println("Severity : " + visit.getSeverityLevel().getSeverity());
            System.out.println("Symptoms : " + visit.getSymptoms());
        } else {
            System.out.println("Unknown patient type.");
            return;
        }

        if (consultManager.consultationRecord(patient)) {
            System.out.println("Record saved.");

            while (true) {
                System.out.println("Next action:");
                System.out.println("1. Schedule follow-up appointment");
                System.out.println("2. Send to treatment");
                System.out.println("3. Send to pharmacy");
                System.out.println("4. Done");
                System.out.print("Choose > ");

                int action;
                try {
                    action = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number (1-4).");
                    continue;
                }

                switch (action) {
                    case 1 -> {
                        String docId = currentPatient instanceof Appointment appt ? 
                                       appt.getDoctor().getDoctorID() : 
                                       ((Visit) currentPatient).getDoctor().getDoctorID();

                        int sev = currentPatient instanceof Appointment appt ?
                                  appt.getSeverity() :
                                  ((Visit) currentPatient).getSeverityLevel().getSeverity();

                        apptUI.bookAppointmentUI(patient, docId, sev, currentDoc);

                        if (isToPharmacy()) {
                            toPharmacyUI(currentDoc, patient);
                        }
                        return;
                    }
                    case 2 -> {
                        toTreatmentUI(currentDoc);
                        return;
                    }
                    case 3 -> {
                        toPharmacyUI(currentDoc, patient);
                        return;
                    }
                    case 4 -> {
                        System.out.println("Done.");
                        return;
                    }
                    default -> System.out.println("Please choose a valid option (1-4).");
                }
            }
        }
    }
    
    private void toTreatmentUI(Doctor doc) {
        while (true) {
            System.out.println("\n--- Select Treatment ---");
            trtManager.displayAllTreatments();

            System.out.println("Enter treatment name to assign: ");
            String name = scanner.nextLine();

            Treatment selected = trtManager.findTreatment(name);
            if (selected == null) {
                System.out.println("Treatment not found. Please try again.\n");
                continue;
            }

            System.out.println("Enter room: ");
            String room = scanner.nextLine();

            System.out.println("Enter severity (1-3): ");
            int sev;
            try {
                sev = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter 1-3.");
                continue;
            }

            Severity severity = Severity.fromValue(sev);

            LocalDateTime time = LocalDateTime.now();
            if (consultManager.toTreatment(doc, selected, room, time, severity)) {
                System.out.println("Treatment recorded.");
                break;
            } else {
                System.out.println("Failed to assign treatment. Please try again.");
            }
        }
    }
    
    private void toPharmacyUI(Doctor doc, Patient patient) {
        while (true) {
            System.out.println("\n--- Medicine Record ---");
            medControl.displayAllStock();

            System.out.println("Enter Medicine Name/ID: ");
            String medName = scanner.nextLine();

            Medicine selected = medControl.findMedicine(medName);
            if (selected == null) {
                System.out.println("Medicine not found. Please try again.\n");
                continue;
            }

            System.out.println("Quantity taken: ");
            int qty;
            try {
                qty = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Please enter a number.");
                continue;
            }

            LocalDateTime time = LocalDateTime.now();
            if (consultManager.toPharmacy(doc, patient, selected, qty, time)) {
                System.out.println("Medicine collection recorded.");
                break;
            } else {
                System.out.println("Failed to record. Please check and try again.");
            }
        }
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
