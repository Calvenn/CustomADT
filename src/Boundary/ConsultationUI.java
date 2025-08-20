package Boundary;

import Control.AppointmentManager;
import Control.ConsultationManager;
import Control.DoctorManager; //TEMP
import Control.MedicineControl;
import Control.TreatmentManager;

import Entity.Doctor;
import Entity.Appointment;
import Entity.Consultation;
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
            consultManager.currentDoc = currentDoc;
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
            System.out.println(apptManager.totalAppointments(currentDoc.getDoctorID()) == 0?
                    "No appointment found"
                    : "\nTotal Appointment: " + apptManager.totalAppointments(currentDoc.getDoctorID()));
            System.out.println(apptManager.getIncomingAppointment(currentDoc.getDoctorID()) == null?
                   "No incoming appointment found"
                    : "Incoming Appointment: " + apptManager.getIncomingAppointment(currentDoc.getDoctorID()));
            apptManager.checkMissedAppt(currentDoc.getDoctorID());
            System.out.println(
                    apptManager.getNumMissedAppt(currentDoc.getDoctorID()) == 0? 
                    "No appointment missed" 
                    : "Miss Appointment: " + apptManager.getNumMissedAppt(currentDoc.getDoctorID()));
            if(apptManager.getNumMissedAppt(currentDoc.getDoctorID()) != 0){
                apptUI.missedFlag = true;
            } else {
                apptUI.missedFlag = false;
            }
            
            System.out.println("\n--- Consultation Menu ---");
            System.out.println("1. Handle Consultation");
            System.out.println("2. Appointments");
            System.out.println("3. Back");
            System.out.println("4. Log Out");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            switch (choice) {
                case 1 -> consultationMenu();
                case 2 -> apptUI.apptMenu(currentDoc);
                case 3 -> {
                    System.out.println("Returning to main menu...");
                    return;
                }
                case 4 -> {
                    System.out.println("Thank You " + currentDoc.getDoctorName());
                    currentDoc = null;
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
            case 2 -> displayRecordUI();
            case 3 -> {
                System.out.println("Returning to main menu...");
                return;
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void consultRecord() {
        Object currentPatient = null;
        System.out.print("Do you want to call next patient? (y/any key for no): ");
        String choice = scanner.nextLine();
        
        if(choice.equalsIgnoreCase("y")){          
            currentPatient = consultManager.dispatchNextPatient();
        }

        if (currentPatient == null) {
            System.out.println("No more patients in queue.");
            return;
        }

        System.out.println("\n--- Now Consulting ---");
        String id = null;
        Patient patient = null;
        if(currentPatient instanceof Visit visit){
            System.out.println("Type     : Walk-In");
            System.out.println("Visit ID : " + visit.getVisitId());
            System.out.println("Patient  : " + visit.getPatient().getPatientName());
            System.out.println("Doctor   : " + visit.getDoctor().getDoctorName());
            System.out.println("Severity : " + visit.getSeverityLevel().getSeverity());
            System.out.println("Symptoms : " + visit.getSymptoms());
            patient = visit.getPatient();
        } else if (currentPatient instanceof Consultation appt){
            id = appt.getID();
            System.out.println("Type   : Appointment");
            System.out.println("ID       : " + id);
            System.out.println("Patient  : " + appt.getPatient().getPatientName());
            System.out.println("Doctor   : " + appt.getDoctor().getDoctorName());
            System.out.println("Severity : " + appt.getSeverity());
            System.out.println("Symptoms : " + appt.getDisease());
            patient = appt.getPatient();
        }
        
        System.out.print("Enter severity level: ");
        int severity = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter diagnosis: ");
        String diagnosis = scanner.nextLine();

        System.out.print("Enter notes (if any): ");
        String notes = scanner.nextLine();
        
        Consultation consultInfo = consultManager.consultationRecord(id, patient, severity, diagnosis, notes);

        if (consultInfo != null) {
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
                        apptUI.bookAppointmentUI(consultInfo);
                        Consultation.numOfFollowUp++;

                        if (isToPharmacy()) {
                            toPharmacyUI(currentDoc, patient);
                            Consultation.numOfPharmacy++;
                        }
                        return;
                    }
                    case 2 -> {
                        toTreatmentUI(currentDoc);
                        Consultation.numOfTreatment++;
                        return;
                    }
                    case 3 -> {
                        toPharmacyUI(currentDoc, patient);
                        Consultation.numOfPharmacy++;
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

            Treatment selected = trtManager.findTreatmentName(name);
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
            medControl.displayAllMedicines();

            System.out.println("Enter Medicine ID: ");
            String medID = scanner.nextLine();

            Medicine selected = medControl.findMedicine(medID);
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
    
    private void displayRecordUI(){
        if(!consultManager.displayAllRecordsByDoctor(currentDoc)){
            System.out.println("No record found.");
        } else {
            System.out.print("Do you want to sort by patient IC? (y/any key to exit): ");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("y")) {
                while (true) {
                    System.out.print("Please enter patient IC (press 'x' to exit): ");
                    String searchedIC = scanner.nextLine();

                    if (searchedIC.equalsIgnoreCase("x")) return;

                    if (!consultManager.displayRecordsByIC(searchedIC)) {
                        System.out.println("Patient IC " + searchedIC + " not found");
                    }
                }
            }
        }
    }
}
