package Boundary;

import Control.AppointmentManager;
import Control.ConsultationManager;
import Control.ConsultationReport;
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
import adt.LinkedHashMap;

import exception.*;

import java.time.LocalDateTime;
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
    private final ConsultationReport consultReport;
    private final Scanner scanner;

    public ConsultationUI(DoctorManager docManager, AppointmentManager apptManager, ConsultationManager consultManager, TreatmentManager trtManager, MedicineControl medControl, ConsultationReport consultReport) {
        this.docManager = docManager;
        this.apptManager = apptManager;
        this.apptUI = new AppointmentUI(apptManager); 
        this.consultManager = consultManager;
        this.trtManager = trtManager;
        this.medControl = medControl;
        this.consultReport = consultReport;
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
            consultationApptSummary();
            if(apptManager.getNumMissedAppt(currentDoc.getDoctorID()) != 0){
                apptUI.missedFlag = true;
            } else {
                apptUI.missedFlag = false;
            }
            
            System.out.println("\n" + "=".repeat(35));
            System.out.println("        CONSULTATION MENU");
            System.out.println("=".repeat(35));
            System.out.println("1. Handle Consultation");
            System.out.println("2. Appointments");
            System.out.println("3. Consultation Report");
            System.out.println("4. Back");
            System.out.println("0. Log Out");
            System.out.println("=".repeat(35));

            int choice = ValidationHelper.inputValidatedChoice(0,4, "your choice");

            switch (choice) {
                case 1 -> consultationMenu();
                case 2 -> apptUI.apptMenu(currentDoc);
                case 3 -> consultationReportMenu();
                case 4 -> {
                    System.out.println("Returning to main menu...");
                    return;
                }
                case 0 -> {
                    System.out.println("Thank You " + currentDoc.getDoctorName());
                    currentDoc = null;
                    return;
                }
                default -> System.out.println("Invalid choice.\n");
            }
        }
    }
    
    private void consultationMenu() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("        CONSULTATION");
        System.out.println("=".repeat(35));
        System.out.println("1. Consultation Record");
        System.out.println("2. Consultation History");
        System.out.println("0. Back");
        System.out.println("=".repeat(35));

        int choice = ValidationHelper.inputValidatedChoice(0,2, "your choice");
        
        switch (choice) {
            case 1 -> consultRecord(); 
            case 2 -> displayRecordUI();
            case 0 -> {
                System.out.println("Returning to main menu...");
                return;
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void consultationReportMenu(){
        System.out.println("\n" + "=".repeat(35));
        System.out.println("        CONSULTATION REPORT");
        System.out.println("=".repeat(35));
        System.out.println("1. Consultation Outcome Trends");
        System.out.println("2. Wait Time Efficiency");
        System.out.println("0. Back");
        System.out.println("=".repeat(35));
        int choice = ValidationHelper.inputValidatedChoice(0,2, "your choice");
        
        switch (choice) {
            case 1 -> displayOutcomeReport();
            case 2 -> displayTimeToConsultReport();
            case 0 -> {
                System.out.println("Returning to main menu");
                return;
            }
            default -> System.out.println("Invalid choice.\n");
        }
   }
    
    private void consultRecord() {
        Object currentPatient = null;
        Severity severity = null;
        LocalDateTime startTime = null, createdAt = null;
        Character choice = ValidationHelper.inputValidateYesOrNo("Do you want to call next patient?)");
        
        if(choice == 'y' || choice == 'Y'){          
            currentPatient = consultManager.dispatchNextPatient();
        }

        if (currentPatient == null) {
            System.out.println("No more patients in queue.");
            return;
        }

        System.out.println("\n=== Now Consulting ===");
        String id = null;
        Patient patient = null;
        if(currentPatient instanceof Visit visit){
            System.out.println("=".repeat(35));
            System.out.println("Type     : Walk-In");
            System.out.println("Visit ID : " + visit.getVisitId());
            System.out.println("Patient  : " + visit.getPatient().getPatientName());
            System.out.println("Doctor   : " + visit.getDoctor().getDoctorName());
            System.out.println("Severity : " + visit.getSeverityLevel().getSeverity());
            System.out.println("Symptoms : " + visit.getSymptoms());
            System.out.println("=".repeat(35));
            patient = visit.getPatient();
        } else if (currentPatient instanceof Consultation appt){
            id = appt.getID();
            System.out.println("=".repeat(35));
            System.out.println("Type   : Appointment");
            System.out.println("ID       : " + id);
            System.out.println("Patient  : " + appt.getPatient().getPatientName());
            System.out.println("Doctor   : " + appt.getDoctor().getDoctorName());
            System.out.println("Severity : " + appt.getSeverity());
            System.out.println("Symptoms : " + appt.getDisease());
            System.out.println("=".repeat(35));
            patient = appt.getPatient();
        }      
        
        boolean isLifeThreatening = false;
        
        startTime = LocalDateTime.now();
        System.out.print("Enter diagnosis: ");
        String diagnosis = scanner.nextLine();
        
        Character lifeThreatening = ValidationHelper.inputValidateYesOrNo("Is this symptom potentially life-threatening? ");     
        if(lifeThreatening == 'y' || lifeThreatening == 'Y') isLifeThreatening = true;
        severity = Entity.Symptoms.assessSeverity(diagnosis, isLifeThreatening);

        System.out.print("Enter notes (if any): ");
        String notes = scanner.nextLine();
        
        createdAt = LocalDateTime.now();
        Consultation consultInfo = consultManager.consultationRecord(id, patient, severity.getSeverity(), diagnosis, notes, startTime, createdAt);

        if (consultInfo != null) {
            System.out.println("Record saved.");

            while (true) {
                System.out.println("Next action:");
                System.out.println("1. Schedule follow-up appointment");
                System.out.println("2. Send to treatment");
                System.out.println("3. Send to pharmacy");
                System.out.println("4. Done");
                int action = ValidationHelper.inputValidatedChoice(1, 4, "your choice");

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
                        toTreatmentUI(currentDoc, severity);
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
    
    private void toTreatmentUI(Doctor doc, Severity severity) {
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
                System.out.println("Medicine collection recorded. Please collect the medicine at counter.");
                break;
            } else {
                System.out.println("Failed to record. Please check and try again.");
            }
        }
    }
    
    private boolean isToPharmacy(){
        while(true){
            Character input = ValidationHelper.inputValidateYesOrNo("Does patient need to collect medicine?");

            if (input == 'y' || input == 'Y') return true;
            else if (input == 'n' || input == 'N') return false;
            else System.out.println("Please enter Y or N only.");
        }
    } 
    
    private void displayRecordUI(){
        if(!consultManager.displayAllRecordsByDoctor(currentDoc)){
            System.out.println("No record found.");
        } else {
            Character choice = ValidationHelper.inputValidateYesOrNo("Do you want to sort by patient IC?");

            if (choice == 'y' || choice == 'Y') {
                while (true) {
                    System.out.print("Please enter patient IC (press 'x' to exit): ");
                    String rawInput = scanner.nextLine().trim();

                    if (rawInput.equalsIgnoreCase("x")) {
                        return; // exit search loop
                    }

                    String searchedIc = ValidationHelper.inputValidatedIC(rawInput);
                    if (searchedIc == null) continue; // invalid → try again

                    if (!consultManager.displayRecordsByIC(searchedIc)) {
                        System.out.println("Patient IC " + searchedIc + " not found");
                    }
                }
            }
        }
    }
    
    /**REPORT SECTION**/
    public void consultationApptSummary(){
        // Collect values first
        int total = apptManager.totalAppointments(currentDoc.getDoctorID());
        Appointment incoming = apptManager.getIncomingAppointment(currentDoc.getDoctorID());
        int missed = apptManager.getNumMissedAppt(currentDoc.getDoctorID());

        // Print table header
        System.out.println("\n=================== Appointment Summary ====================");
        System.out.printf("| %-20s | %-34s |%n", "Field", "Value");
        System.out.println("------------------------------------------------------------");

        // Print each row
        System.out.printf("| %-20s | %-34s |%n", "Total Appointments", 
            (total == 0 ? "No appointment found" : total));

        System.out.printf("| %-20s | %-34s |%n", "Incoming Appointment", 
            (incoming == null ? "No incoming appointment found" : incoming));

        System.out.printf("| %-20s | %-34s |%n", "Missed Appointments", 
            (missed == 0 ? "No appointment missed" : missed));

        // Print footer
        System.out.println("============================================================\n");       
    }
    
    public void displayOutcomeReport() {
        int[] outcomeData = consultReport.generateOutcomeCounts();
        
        if (outcomeData == null) {
            System.out.println("\nNo consultation outcomes recorded yet.");
            return;
        }
        
        int followUp = outcomeData[0];
        int pharmacy = outcomeData[1];
        int treatment = outcomeData[2];
        int total = outcomeData[3];

        System.out.println("\n" + "=".repeat(46));
        System.out.println("          Consultation Outcome Report");
        System.out.println("=".repeat(46));
        System.out.printf("| %-12s | %7s | %12s |\n", "Outcome", "Count", "Percentage");
        System.out.println("|--------------|---------|--------------|");

        System.out.printf("| %-12s | %7d | %10.2f %% |\n", "Follow-up", followUp, followUp * 100.0 / total);
        System.out.printf("| %-12s | %7d | %10.2f %% |\n", "Pharmacy", pharmacy, pharmacy * 100.0 / total);
        System.out.printf("| %-12s | %7d | %10.2f %% |\n", "Treatment", treatment, treatment * 100.0 / total);

        System.out.println("|--------------|---------|--------------|");
        System.out.printf("| %-12s | %7d | %10.2f %% |\n", "Total", total, 100.0);
        System.out.println("=".repeat(46));
        displayDiagnosisTrends();
    }
    
    public void displayDiagnosisTrends() {
        LinkedHashMap<String, Integer> trends = consultReport.generateDiagnosisTrends();
        
        if (trends == null) {
            System.out.println("\nNo consultation records or diagnoses available.");
            return;
        }

        System.out.println("\n" + "=".repeat(46));
        System.out.println("           Diagnosis Frequency Report");
        System.out.println("=".repeat(46));
        System.out.printf("| %-22s | %-7s |\n", "Diagnosis", "Count");
        System.out.println("|------------------------|---------|");

        Object[] keys = trends.getKeys();
        for (Object keyObj : keys) {
            String key = (String) keyObj;
            int count = trends.get(key);
            System.out.printf("| %-22s | %7d |\n", key, count);
        }

        System.out.println("=".repeat(46));
    }
    
    public void displayTimeToConsultReport() {
        LinkedHashMap<String, Object[]> data = consultReport.generateTimeToConsultReport();

        if (data.isEmpty()) {
            System.out.println("\nNo consultation records available.");
            return;
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.println("             Time-to-Consult Report (Wait Time Efficiency)");
        System.out.println("=".repeat(70));
        System.out.printf("| %-12s | %-10s | %-15s | %-15s |\n",
                "Category", "Avg Wait", "Longest Wait", "Shortest Wait");
        System.out.println("|--------------|------------|-----------------|-----------------|");

        Object[] keys = data.getKeys();
        for (Object k : keys) {
            String category = (String) k;
            Object[] stats = data.get(category);
            System.out.printf("| %-12s | %-10s | %-15s | %-15s |\n",
                    category, stats[0] + " mins", stats[1], stats[2]);
        }

        System.out.println("=".repeat(70));
    }
}
