/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Boundary.*;
import Control.*;
import Entity.*;
import adt.*;
import exception.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

/**
 *
 * @author calve
 */
public class ClinicApplication {
    // Shared ADTs
    private final Heap<Doctor> sharedDoc = new Heap<>(false);
    private final Heap<Visit> sharedVisitQueue = new Heap<>(true);
    private final Heap<Treatment> providedTreatments = new Heap<>(true);
    private final LinkedHashMap<String,Medicine> medMap = new LinkedHashMap<>();
    private final List<MedRecord> medRecList = new List<>(); 
    private final Queue<TreatmentAppointment> treatmentQueue = new Queue<>();
    private final Queue<MedRecord> medCollectQueue = new Queue<>();
    private final LinkedHashMap<String, Queue<Appointment>> missAppt = new LinkedHashMap<>();  
    private final LinkedHashMap<String, List<Consultation>> consultLog = new LinkedHashMap<>();

    // Control Layer
    private final PatientManager patientManager;
    private final DoctorManager docManager;
    private final QueueManager queueManager;
    private final AppointmentManager apptManager;
    private final ConsultationManager consultManager;
    private final ConsultationReport consultReport;
    private final TreatmentManager trtManager;
    private final MedicineControl medControl;
    private final MedRecordControl medRecControl;
    private final PharmacyReport pharReport;
    private final VisitHistoryManager historyManager;

    // UI Layer
    private final ConsultationUI consultUI;
    private final PatientManagementUI patientUI;
    private final PharmacyUI pharUI;

    public ClinicApplication() {
        // wire dependencies
        patientManager = new PatientManager();
        docManager = new DoctorManager(sharedDoc);
        historyManager = new VisitHistoryManager();
        queueManager = new QueueManager(sharedVisitQueue, docManager, consultLog, historyManager);
        apptManager = new AppointmentManager(missAppt, consultLog, docManager, queueManager);
        consultManager = new ConsultationManager(sharedVisitQueue, apptManager.getAppointmentHeap(),
        docManager, consultLog, treatmentQueue, medCollectQueue, apptManager);
        consultReport = new ConsultationReport(consultLog);
        trtManager = new TreatmentManager(providedTreatments);
        medControl = new MedicineControl(medMap);
        medRecControl = new MedRecordControl(medRecList);

        consultUI = new ConsultationUI(docManager, apptManager, consultManager, trtManager, medControl, consultReport);
        patientUI = new PatientManagementUI(queueManager, patientManager);
        pharReport = new PharmacyReport(medRecList);
        pharUI = new PharmacyUI(medRecControl, medControl, medCollectQueue, pharReport);
    }
    
    
    public void run(){
        //loadPatientsCSV("/CustomADT/src/data/patients.csv");
        loadDummyPatients(patientManager);
        loadDummyDoctors(docManager);
        loadDummyTreatment(trtManager);
        loadDummyMed(medMap);
        loadDummyMedRec(docManager, medControl,medRecList);
        //loadDummyAppt(apptManager, docManager);
        loadDummyConsult(consultManager, docManager, consultLog);
        
        int choice;
        do {
            queueManager.loadVisit();
            displayMainMenu();
            choice = ValidationHelper.inputValidatedChoice(0,5);

            switch (choice) {
                case 1 -> patientUI.patientMenu();
                // case 2 -> consultUI.consultMainMenu();
                case 3 -> {
                    System.out.println("Treatment queue: " + treatmentQueue.size());
                    System.out.println("Med Collection queue: " + medCollectQueue.size());
                    consultUI.consultMainMenu();
                }
                // case 4 -> consultUI.consultMainMenu();
                case 5 -> pharUI.pharmacyMenu();
                case 0 -> {
                    System.out.println("Thank you for using the Clinic System!");
                    System.exit(0);    
                }
            }

        } while (choice != 7);
    }

    private static void displayMainMenu() {
        System.out.println("\n=== Clinic Management System ===");
        System.out.println("1. Patient Registration System");
        System.out.println("2. Doctor Management System");
        System.out.println("3. Consultation System");
        System.out.println("4. Treatment System");
        System.out.println("5. Pharmacy Control System");
        System.out.println("0. Exit");
    }
    
    private static void loadDummyPatients(PatientManager patientManager) {
        patientManager.registerNewPatient("050101-07-0101", "Alice Lee", "0123456789", 25, 'F', "123, Jalan ABC");
        patientManager.registerNewPatient("050202-07-0202", "Bob Tan", "0198765432", 30, 'M', "456, Jalan XYZ");
        patientManager.registerNewPatient("050303-07-0303", "Charlie Lim", "0112345678", 22, 'M', "789, Jalan 123");
    }
    
    private static void loadDummyDoctors(DoctorManager docManager) {
        docManager.addNewDoctor("D001", "John", 30, "012-1231234", "Man", "Head", LocalDate.now());
        docManager.addNewDoctor("D002", "Spider Man", 25, "012-1231234", "Man", "Doctor", LocalDate.now());
        docManager.addNewDoctor("D003", "Iron Man", 26, "012-1231234", "Man", "Assistant", LocalDate.now());
    }

    private static void loadDummyTreatment(TreatmentManager trtManager) {
        trtManager.newTreatment("abc", "test1", Duration.ofMinutes(30));
        trtManager.newTreatment("bcd", "test12", Duration.ofMinutes(20));
        trtManager.newTreatment("efg", "test123", Duration.ofMinutes(50));
    }

    private static void loadDummyMed(LinkedHashMap<String, Medicine> medMap) {
        Medicine med1 = new Medicine("M001", "Panadol", "Pain reliever and fever reducer", 100);
        Medicine med2 = new Medicine("M002", "Amoxicillin", "Antibiotic for bacterial infections", 80);
        Medicine med3 = new Medicine("M003", "Vitamin C", "Immune system booster", 150);
        Medicine med4 = new Medicine("M004", "Loratadine", "Antihistamine for allergies", 60);
        Medicine med5 = new Medicine("M005", "Omeprazole", "Reduces stomach acid", 9);
        Medicine med6 = new Medicine("M006", "Paracetamol", "Common painkiller", 200);
        Medicine med7 = new Medicine("M007", "Ibuprofen", "Anti-inflammatory and pain relief", 120);
        Medicine med8 = new Medicine("M008", "Salbutamol", "Asthma inhaler medication", 10);
        Medicine med9 = new Medicine("M009", "Aspirin", "Blood thinner and pain relief", 70);
        Medicine med10 = new Medicine("M010", "Metformin", "Controls blood sugar in type 2 diabetes", 50);

        // Put into LinkedHashMap first (medID -> Medicine)
        medMap.put(med1.getMedID(), med1);
        medMap.put(med2.getMedID(), med2);
        medMap.put(med3.getMedID(), med3);
        medMap.put(med4.getMedID(), med4);
        medMap.put(med5.getMedID(), med5);
        medMap.put(med6.getMedID(), med6);
        medMap.put(med7.getMedID(), med7);
        medMap.put(med8.getMedID(), med8);
        medMap.put(med9.getMedID(), med9);
        medMap.put(med10.getMedID(), med10);
    }
    
    private static void loadDummyMedRec(DoctorManager docManager, MedicineControl medControl, List<MedRecord> medRecList) {
        Patient p1 = new Patient("050606070606", "Lina", "0124282783", 20, 'F', "Bayan Lepas");
        Patient p2 = new Patient("050707070707", "Bob", "0124282783", 21, 'M', "Gelugor");
        Patient p3 = new Patient("050808070808", "Nana", "0124282783", 19, 'F', "Air Itam");

        // Get doctors
        Doctor doc1 = docManager.findDoctor("D001"); // John
        Doctor doc2 = docManager.findDoctor("D002"); // Spider Man
        Doctor doc3 = docManager.findDoctor("D003"); // Iron Man

        // Get medicines
        Medicine med1 = medControl.findMedicine("M001");
        Medicine med2 = medControl.findMedicine("M002");
        Medicine med3 = medControl.findMedicine("M003");
        Medicine med4 = medControl.findMedicine("M004");
        Medicine med5 = medControl.findMedicine("M005");

        // Add records
        MedRecord mr1 = new MedRecord(p1, doc1, med1, 10, LocalDateTime.of(2025, 8, 5, 8, 30),true); // Lina took Panadol from John
        MedRecord mr2 = new MedRecord(p2, doc2, med2, 5, LocalDateTime.of(2025, 5, 3, 14, 30),true);  // Bob took Amoxicillin from Spider Man
        MedRecord mr3 = new MedRecord(p3, doc3, med3, 15,LocalDateTime.of(2025, 7, 20, 12, 30),true); // Nana took Vitamin C from Iron Man
        MedRecord mr4 = new MedRecord(p1, doc2, med4, 8, LocalDateTime.of(2025, 1, 14, 16, 30),true);  // Lina took Ibuprofen from Spider Man
        MedRecord mr5 = new MedRecord(p2, doc1, med5, 12,LocalDateTime.of(2025, 3, 3, 15, 30),true); // Bob took Aspirin from John
        
        medRecList.add(mr1); 
        medRecList.add(mr2); 
        medRecList.add(mr3); 
        medRecList.add(mr4); 
        medRecList.add(mr5); 
    }
    
    private static void loadDummyConsult(ConsultationManager consult, DoctorManager docManager, LinkedHashMap<String, List<Consultation>> consultLog) {
        Patient p1 = new Patient("050606070606", "Lina", "0124282783", 20, 'M', "Bayan Lepas");
        Patient p2 = new Patient("050707070707", "Bob", "0124282783", 20, 'M', "Bayan Lepas");
        Patient p3 = new Patient("050808070808", "Nana", "0124282783", 20, 'M', "Bayan Lepas");

        Doctor doc1 = docManager.findDoctor("D001"); // JOHN
        Doctor doc2 = docManager.findDoctor("D002");

        List<Consultation> doc1Consults = new List<>();
        doc1Consults.add(new Consultation(1, p1, "Flu", null, doc1, null));
        Consultation.numOfFollowUp++;

        List<Consultation> doc2Consults = new List<>();
        doc2Consults.add(new Consultation(3, p2, "Fever", null, doc2, LocalDateTime.of(2025, Month.AUGUST, 9, 15, 30)));
        Consultation.numOfTreatment++;

        consultLog.put(doc1.getDoctorID(), doc1Consults);
        consultLog.put(doc2.getDoctorID(), doc2Consults);
    }
}
