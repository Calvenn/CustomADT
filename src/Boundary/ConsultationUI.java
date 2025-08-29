package Boundary;

import Control.AppointmentManager;
import Control.ConsultationManager;
import Control.ConsultationReport;
import Control.DoctorManager;   
import Control.MedicineControl;
import Control.PaymentManager;

import Entity.Doctor;
import Entity.Appointment;
import Entity.Consultation;
import Entity.MedRecord;
import Entity.Medicine;
import Entity.Patient;
import Entity.Payment;
import Entity.Severity;
import Entity.Staff;
import Entity.Visit;
import Entity.TreatmentAppointment;
import adt.LinkedHashMap;
import adt.List;

import exception.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 *
 * @author CalvenPhnuahKahHong
 */
public class ConsultationUI {
    private final AppointmentUI apptUI;
    private final AppointmentManager apptManager;
    private final ConsultationManager consultManager;
    private final TreatmentApptUI treatmentApptUI;
    private final DoctorManager docManager;
    private final MedicineControl medControl;
    private static Doctor currentDoc = null;
    private final ConsultationReport consultReport;
    private final Scanner scanner;

    public ConsultationUI(DoctorManager docManager, AppointmentManager apptManager, ConsultationManager consultManager, MedicineControl medControl, ConsultationReport consultReport, TreatmentApptUI treatmentApptUI) {
        this.docManager = docManager;
        this.apptManager = apptManager;
        this.apptUI = new AppointmentUI(apptManager); 
        this.consultManager = consultManager;
        this.medControl = medControl;
        this.consultReport = consultReport;
        this.treatmentApptUI = treatmentApptUI;
        this.scanner = new Scanner(System.in);
    }
    
    public void consultMainMenuRead() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("Select doctor to view record: ");
        List<Doctor> doc = docManager.getDoctorsByDept("CONSULT");
        for(int i = 1; i <= doc.size(); i++){
            Doctor d = doc.get(i);
            System.out.println("[" + (i) + "]" + d.getName());
        }
        System.out.println("" + "=".repeat(35));
        int select = ValidationHelper.inputValidatedChoice(1, doc.size(), "selected doctor");
        currentDoc = doc.get(select);
        
        consultationApptSummary();
        apptManager.refreshMissApptFlag(currentDoc);
        System.out.println("\n" + "=".repeat(35));
        System.out.println("1. Consultation History By " + currentDoc.getName());
        System.out.println("2. Appointment Record");
        System.out.println("3. All consultation History");
        System.out.println("0. Back");
        System.out.println("=".repeat(35));

        int choice = ValidationHelper.inputValidatedChoice(0,3, "your choice");
        
        switch (choice) {
            case 1 -> displayRecordUI(); 
            case 2 -> apptUI.apptMenu(currentDoc);
            case 3 -> displayRecordUIAll();
            case 0 -> {
                System.out.println("Returning to main menu...");
                return;
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    
    public void consultMainMenu(Staff staff) {     
        currentDoc = docManager.findDoctor(staff.getID());
        consultManager.currentDoc = currentDoc;
        if (currentDoc == null || currentDoc.getDepartment().toUpperCase().equals("TREATMENT")) {
            System.out.println("Staff " + staff.getID() + " unable to access");
            return;
        }
        
        while (true) {
            consultationApptSummary();
            apptManager.refreshMissApptFlag(currentDoc);
            
            System.out.println("\n" + "=".repeat(35));
            System.out.println("        CONSULTATION MENU");
            System.out.println("=".repeat(35));
            System.out.println("1. Handle Consultation");
            System.out.println("2. Appointments");
            System.out.println("3. Consultation Report");
            System.out.println("0. Back");
            System.out.println("=".repeat(35));

            int choice = ValidationHelper.inputValidatedChoice(0,3, "your choice");

            switch (choice) {
                case 1 -> consultationMenu();
                case 2 -> apptUI.apptMenu(currentDoc);
                case 3 -> consultationReportMenu();
                case 0 -> {
                    System.out.println("Returning to main menu...");
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
        System.out.println("2. Consultation Duration Report");
        System.out.println("0. Back");
        System.out.println("=".repeat(35));
        int choice = ValidationHelper.inputValidatedChoice(0,2, "your choice");
        
        switch (choice) {
            case 1 -> displayDiagnosisTrends();
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
        Character choice = ValidationHelper.inputValidateYesOrNo("Do you want to call next patient? ");
        
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
            System.out.println("Doctor   : " + visit.getDoctor().getName());
            System.out.println("Severity : " + visit.getSeverityLevel().getSeverity());
            System.out.println("Symptoms : " + visit.getSymptoms());
            System.out.println("=".repeat(35));
            patient = visit.getPatient();
        } else if (currentPatient instanceof Consultation appt){
            id = appt.getID();
            System.out.println("=".repeat(35));
            System.out.println("Type     : Appointment");
            System.out.println("ID       : " + id);
            System.out.println("Patient  : " + appt.getPatient().getPatientName());
            System.out.println("Doctor   : " + appt.getDoctor().getName());
            System.out.println("Severity : " + appt.getSeverity());
            System.out.println("Symptoms : " + appt.getDisease());
            System.out.println("=".repeat(35));
            patient = appt.getPatient();
            
            Character confirm = ValidationHelper.inputValidateYesOrNo("Has arrive? ");
            if(confirm == 'n' || confirm == 'N') {
                currentPatient = null;
                return;
            }
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
            System.out.println("Record saved");

            while (true) {
                System.out.println("\nNext action:");
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
                            toPharmacyUI(currentDoc, patient, diagnosis);
                        }
                        return;
                    }
                    case 2 -> {
                        if(!treatmentApptUI.addNewAppointmentUI(consultInfo)) continue;                       
                        return;
                    }
                    case 3 -> {
                        toPharmacyUI(currentDoc, patient, diagnosis);
                        return;
                    }
                    case 4 -> {
                        System.out.println("Done.");
                        PaymentManager.paymentRec.add(new Payment(patient, consultInfo, Payment.consultPrice, false, null, null));
                        return;
                    }
                    default -> System.out.println("Please choose a valid option (1-4).");
                }
            }
        }
    }
    
    private void toPharmacyUI(Doctor doc, Patient patient, String diagnosis) {
        String medGiven = "";
        List<String> med;   
        
        while (true) {
            System.out.println("\n--- Suggested Medicine ---");
            med = medControl.suggestedMeds(diagnosis);
            
            if (med.size() == 1) {
                // only 1 suggestion
                System.out.println("Suggested Medicine: " + med.get(0));
                medGiven = med.get(0);
            } else {
                // multiple suggestions
                for (int i = 1; i <= med.size(); i++) {
                    System.out.println("[" + (i) + "] " + med.get(i));
                }
                int choice = ValidationHelper.inputValidatedChoice(1, med.size(), "medicine");
                medGiven = med.get(choice);
            }           

            Medicine selected = medControl.findMedicineByName(medGiven.toLowerCase());
            if (selected == null) {
                System.out.println("Medicine not found. Please try again.\n");
                continue;
            }
            
            System.out.println("\nMedicine Given: " + medGiven);

            int qty = ValidationHelper.inputValidatedPositiveInt("Quantity taken: ");

            LocalDateTime time = LocalDateTime.now();
            if (consultManager.toPharmacy(doc, patient, selected, qty, time)) {
                System.out.println("Medicine collection recorded. Please ask patient made payment at counter.");
                break;
            } else {
                System.out.println("Missing data for medicine dispensing.");
            }
        }
    }
    
    private boolean isToPharmacy(){
        while(true){
            Character input = ValidationHelper.inputValidateYesOrNo("\nDoes patient need to collect medicine?");

            if (input == 'y' || input == 'Y') return true;
            else if (input == 'n' || input == 'N') return false;
            else System.out.println("Please enter Y or N only.");
        }
    } 
    
    private void printRecords(List<Consultation> records) {
        if (records.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        System.out.println(Consultation.getHeader());
        for (int i = 1; i <= records.size(); i++) {   
            Consultation c = records.get(i);
            System.out.println(c);
        }
    }

    private void sortAndDisplayMenu(List<Consultation> records, boolean allowSearch) {
        while (true) {
            System.out.println("\nDo you want to sort records by consultation date?");
            System.out.println("[1] Ascending (oldest first)");
            System.out.println("[2] Descending (latest first)");
            System.out.print(allowSearch == true? "[3] Search by patient IC\n" : "");
            System.out.println("[0] Back");

            int maxOption = allowSearch ? 3 : 2;
            int choice = ValidationHelper.inputValidatedChoice(0, maxOption, "sort option");

            if (choice == 1) {
                consultManager.sortByDate(records, true);
                printRecords(records);
            } else if (choice == 2) {
                consultManager.sortByDate(records, false);
                printRecords(records);
            } else if (allowSearch && choice == 3) {
                displayByIC();
            } else if (choice == 0) {
                return;
            }
        }
    }

    // For all doctors
    public void displayRecordUIAll() {
        List<Consultation> record = consultManager.getRecordsAll();
        if (record.isEmpty()) {
            System.out.println("No record found.");
            return;
        }
        printRecords(record);
        sortAndDisplayMenu(record, false);
    }

    // For current doctor
    private void displayRecordUI() {
        List<Consultation> allConsultRec = consultManager.displayAllRecordsByDoctor(currentDoc);
        if (allConsultRec.isEmpty()) {
            System.out.println("No consultation records found for Doctor " + currentDoc.getID());
            return;
        }
        printRecords(allConsultRec);
        sortAndDisplayMenu(allConsultRec, true);
    }


    private void displayByIC() {
        while (true) {
            System.out.print("\nPlease enter patient IC (press 'x' to exit): ");
            String rawInput = scanner.nextLine().trim();

            if (rawInput.equalsIgnoreCase("x")) {
                return; // exit search loop
            }

            String searchedIc = ValidationHelper.validateICOnce(rawInput);
            if (searchedIc == null) continue; // invalid → try again

            List<Consultation> consultations = consultManager.displayRecordsByIC(searchedIc);

            // if no record found
            if (consultations.isEmpty()) {
                System.out.println("No record found for " + searchedIc);
                continue;
            }

            for (int i = 1; i <= consultations.size(); i++) {
                Consultation c = consultations.get(i);
                System.out.println(generateFullReport(c));
            }
        }
    }

    
    /**REPORT SECTION**/
    public void consultationApptSummary(){
        apptManager.checkMissedAppt(currentDoc.getID());
        int total = apptManager.totalAppointments(currentDoc.getID());
        int missed = apptManager.getNumMissedAppt(currentDoc.getID());
        Appointment incoming = apptManager.getIncomingAppointment(currentDoc.getID());

        // Print table header
        System.out.println("\n=================== Appointment Summary ====================");
        System.out.printf("| %-20s | %-34s |%n", "Field", "Value");
        System.out.println("------------------------------------------------------------");

        // Print each row
        System.out.printf("| %-20s | %-34s |%n", "Total Appointments", 
            (total == 0 ? "No appointment found" : total));

        System.out.printf("| %-20s | %-34s |%n", "Incoming Appointment", 
            (incoming == null ? "No incoming appointment found" : incoming.getPatient().getPatientName() + " " + incoming.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

        System.out.printf("| %-20s | %-34s |%n", "Missed Appointments", 
            (missed == 0 ? "No appointment missed" : missed));

        // Print footer
        System.out.println("============================================================\n");       
    }
    
    public String generateFullReport(Consultation c) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        StringBuilder report = new StringBuilder();

        report.append("============================================================\n");
        report.append("                     Consultation Report                  \n");
        report.append("============================================================\n\n");

        report.append(String.format("%-18s: %s\n", "Consultation ID", c.getID()));
        report.append(String.format("%-18s: %s\n", "Patient Name", c.getPatient().getPatientName()));
        report.append(String.format("%-18s: %s\n", "Patient IC", c.getPatient().getPatientIC()));
        report.append(String.format("%-18s: %s\n", "Severity Level", c.getSeverity()));
        report.append(String.format("%-18s: %s\n", "Diagnosis", c.getDisease()));
        report.append(String.format("%-18s: %s\n", "Notes", (c.getNotes() == null ? "-" : c.getNotes())));
        report.append(String.format("%-18s: %s\n", "Doctor", c.getDoctor().getName()));
        report.append(String.format("%-18s: %s\n", "Consult Time", c.getConsultTime().format(formatter)));
        report.append(String.format("%-18s: %s\n", "Appointment Time", 
                (c.getDateTime() == null ? "-" : c.getDateTime().format(formatter))));
        report.append(String.format("%-18s: %s\n", "Created At", c.getCreatedAt().format(formatter)));

        // --- Medical Records ---
        report.append("\n------------------------------------------------------------\n");
        report.append("                      Medical Records                      \n");
        report.append("------------------------------------------------------------\n");
        report.append(String.format("%-10s %-20s %-40s\n", "MedID", "Date", "Medicine"));

        if (c.medRecords.isEmpty()) {
            report.append("No medical records found.\n");
        } else {
            for (int i = 1; i <= c.medRecords.size(); i++) {
                MedRecord m = c.medRecords.get(i);
                report.append(String.format("%-10s %-20s %-40s\n",
                        m.getRecordID(),
                        m.getTimestamp().format(formatter),
                        m.getMed().getName()));
            }
        }

        // --- Treatment Records ---
        report.append("\n------------------------------------------------------------\n");
        report.append("                     Treatment Records                     \n");
        report.append("------------------------------------------------------------\n");
        report.append(String.format("%-10s %-20s %-40s\n", "TrtID", "Date", "Treatment"));

        if (c.trtAppts.isEmpty()) {
            report.append("No treatment records found.\n");
        } else {
            for (int i = 1; i <= c.trtAppts.size(); i++) {
                TreatmentAppointment t = c.trtAppts.get(i);
                report.append(String.format("%-10s %-20s %-40s\n",
                        t.getAppointmentId(),
                        t.getDateTime().format(formatter),
                        t.getTreatment().getName()));
            }
        }

        report.append("============================================================\n");

        return report.toString();
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

        if (trends == null || trends.isEmpty()) {
            System.out.println("\nNo consultation records or diagnoses available.");
            return;
        }

        System.out.println("\n" + "=".repeat(35));
        System.out.println("                TYPE");
        System.out.println("=".repeat(35));
        System.out.println("[1] Top 5 Diagnosis Trends");
        System.out.println("[2] View All Diagnosis Trends");
        System.out.println("=".repeat(35));
        int choice = ValidationHelper.inputValidatedChoice(1, 2, "your choice");

        Object[] keys = trends.getKeys();

        // 🔹 Sort using helper function
        consultReport.bubbleSortByCountDesc(keys, trends);

        System.out.println("\n" + "=".repeat(46));
        System.out.println("           Diagnosis Frequency Report");
        System.out.println("=".repeat(46));
        System.out.printf("| %-4s | %-25s | %-7s |\n", "Rank", "Diagnosis", "Count");
        System.out.println("|------|---------------------------|---------|");

        int limit = (choice == 1) ? Math.min(5, keys.length) : keys.length;

        for (int i = 0; i < limit; i++) {
            String key = (String) keys[i];
            int count = trends.get(key);
            System.out.printf("| %-4d | %-25s | %7d |\n", i + 1, key, count);
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
        System.out.println("             CONSULTATION DURATION REPORT");
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