package Main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import Boundary.ConsultationUI;
import Boundary.PatientManagementUI;
import Boundary.ReportUI;
import Boundary.PharmacyUI;

import Control.DoctorManager;
import Control.AppointmentManager;
import Control.ConsultationManager;
import Control.ConsultationReport;
import Control.MedicineControl;
import Control.MedRecordControl;
import Control.PatientManager;
import Control.QueueManager;
import Control.TreatmentManager;

import Entity.Patient; //testing purpose
import Entity.Doctor;
import Entity.Appointment;
import Entity.Consultation;
import Entity.MedRecord;
import Entity.Medicine;
import Entity.Treatment;
import Entity.TreatmentAppointment;
import Entity.Visit;

import adt.Heap;
import adt.LinkedHashMap;
import adt.List;
import adt.Queue;

import java.time.LocalDate;
import java.time.Duration; 
import java.time.LocalDateTime; // testing purpose
import java.time.Month;
import java.util.Scanner;
/**
 *
 * @author calve
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Heap<Doctor> sharedDoc = new Heap<>(false);
        Heap<Visit> sharedVisitQueue = new Heap<>(true);
        //Heap<Appointment> consultAppt = new Heap<>(false);
        Heap<Treatment> providedTreatments = new Heap<>(true);
        LinkedHashMap<String,Medicine> medMap = new LinkedHashMap<>();
        List<MedRecord> medRecList = new List<>();
        
        Queue<TreatmentAppointment> treatmentQueue = new Queue<>();
        Queue<MedRecord> medCollectQueue = new Queue<>();
        
        LinkedHashMap<String, Queue<Appointment>> missAppt = new LinkedHashMap<>();  
        LinkedHashMap<String, List<Consultation>> consultLog = new LinkedHashMap<>();
           
        PatientManager patientManager = new PatientManager();
        DoctorManager docManager = new DoctorManager(sharedDoc);
        QueueManager queueManager = new QueueManager(sharedVisitQueue, docManager, patientManager, consultLog);
        AppointmentManager apptManager = new AppointmentManager(missAppt, consultLog, docManager, queueManager);
        ConsultationManager consultManager = new ConsultationManager(sharedVisitQueue, apptManager.getAppointmentHeap(), docManager, consultLog, treatmentQueue, medCollectQueue, apptManager);
        TreatmentManager trtManager = new TreatmentManager(providedTreatments); 
        MedicineControl medControl = new MedicineControl(medMap);
        MedRecordControl medRecControl = new MedRecordControl(medRecList);
        
        loadDummyDoctors(docManager);
        loadDummyTreatment(trtManager);
        loadDummyMed(medControl);
        loadDummyMedRec(medRecControl, docManager, medControl);
        //loadDummyAppt(apptManager, docManager);
        loadDummyConsult(consultManager, docManager, consultLog);
        
        ConsultationReport consultReport = new ConsultationReport(consultLog);

        ConsultationUI consultUI = new ConsultationUI(docManager, apptManager, consultManager, trtManager, medControl);
        PatientManagementUI patientUI = new PatientManagementUI(sharedVisitQueue, queueManager, docManager);
        ReportUI reportUI = new ReportUI(consultReport);
        PharmacyUI pharUI = new PharmacyUI(medRecControl, medControl);

        int choice;
        do {
            while (true) {
            queueManager.loadVisit();
            displayMainMenu();
                try {
                    System.out.print("Enter your choice: ");
                    choice = Integer.parseInt(scanner.nextLine());
                    if (choice < 1 || choice > 8) {
                        System.out.println("Invalid choice. Please enter 1 to 3.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            switch (choice) {
                case 1 -> patientUI.patientMenu();
                //case 2 -> consultUI.consultMainMenu();
                case 3 -> {
                    System.out.println("Treatment queue: " + treatmentQueue.size());
                    System.out.println("Med Collection queue: " + medCollectQueue.size());
                    consultUI.consultMainMenu();
                }
                //case 4 -> consultUI.consultMainMenu();
                case 5 -> pharUI.pharmacyMenu();
                case 6 -> reportUI.reportMainMenu();
                case 7 -> {
                    System.out.println("Thank you for using the Clinic System!");
                    System.exit(0);
                }
            }
        } while (choice != 6);
    }

    private static void displayMainMenu() {
        System.out.println("\n=== Clinic Management System ===");
        System.out.println("1. Patient Registration System");
        System.out.println("2. Doctor Management System");
        System.out.println("3. Consultation System");
        System.out.println("4. Treatment System");
        System.out.println("5. Pharmacy Control System");
        System.out.println("6. Report");
        System.out.println("7. Exit");
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

    private static void loadDummyMed(MedicineControl medControl) {
        Medicine med1 = new Medicine("Panadol", "Panadol", "Pain reliever and fever reducer", 100);
        Medicine med2 = new Medicine("Amoxicillin", "Amoxicillin", "Antibiotic for bacterial infections", 80);
        Medicine med3 = new Medicine("VitaminC", "Vitamin C", "Immune system booster", 150);
        Medicine med4 = new Medicine("Loratadine", "Loratadine", "Antihistamine for allergies", 60);
        Medicine med5 = new Medicine("Omeprazole", "Omeprazole", "Reduces stomach acid", 90);
        Medicine med6 = new Medicine("Paracetamol", "Paracetamol", "Common painkiller", 200);
        Medicine med7 = new Medicine("Ibuprofen", "Ibuprofen", "Anti-inflammatory and pain relief", 120);
        Medicine med8 = new Medicine("Salbutamol", "Salbutamol", "Asthma inhaler medication", 40);
        Medicine med9 = new Medicine("Aspirin", "Aspirin", "Blood thinner and pain relief", 70);
        Medicine med10 = new Medicine("Metformin", "Metformin", "Controls blood sugar in type 2 diabetes", 50);

        medControl.addMedicine(med1);
        medControl.addMedicine(med2);
        medControl.addMedicine(med3);
        medControl.addMedicine(med4);
        medControl.addMedicine(med5);
        medControl.addMedicine(med6);
        medControl.addMedicine(med7);
        medControl.addMedicine(med8);
        medControl.addMedicine(med9);
        medControl.addMedicine(med10);
    }
    private static void loadDummyMedRec(MedRecordControl medRecControl, DoctorManager docManager, MedicineControl medControl) {
        Patient p1 = new Patient("050606070606", "Lina", "0124282783", 20, 'F', "Bayan Lepas");
        Patient p2 = new Patient("050707070707", "Bob", "0124282783", 21, 'M', "Gelugor");
        Patient p3 = new Patient("050808070808", "Nana", "0124282783", 19, 'F', "Air Itam");

        // Get doctors
        Doctor doc1 = docManager.findDoctor("D001"); // John
        Doctor doc2 = docManager.findDoctor("D002"); // Spider Man
        Doctor doc3 = docManager.findDoctor("D003"); // Iron Man

        // Get medicines
        Medicine med1 = medControl.findMedicine("Panadol");
        Medicine med2 = medControl.findMedicine("Amoxicillin");
        Medicine med3 = medControl.findMedicine("VitaminC");
        Medicine med4 = medControl.findMedicine("Ibuprofen");
        Medicine med5 = medControl.findMedicine("Aspirin");

        // Add records
        medRecControl.addRecord(p1, doc1, med1, 10); // Lina took Panadol from John
        medRecControl.addRecord(p2, doc2, med2, 5);  // Bob took Amoxicillin from Spider Man
        medRecControl.addRecord(p3, doc3, med3, 15); // Nana took Vitamin C from Iron Man
        medRecControl.addRecord(p1, doc2, med4, 8);  // Lina took Ibuprofen from Spider Man
        medRecControl.addRecord(p2, doc1, med5, 12); // Bob took Aspirin from John
    }
    /*
    private static void loadDummyAppt(AppointmentManager apptManager, DoctorManager docManager) {
        Patient patient = new Patient("050606070606", "Lina", "0124282783", 20, 'M', "Bayan Lepas");
        Doctor doc = docManager.findDoctor("D001");
        //apptManager.bookAppointment(patient, 1, LocalDateTime.now(), doc);
        //apptManager.bookAppointment(patient, 2, LocalDateTime.of(2025, Month.DECEMBER, 12, 12, 30), doc);
    }*/
    
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


