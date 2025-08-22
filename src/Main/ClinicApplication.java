/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Boundary.*;
import Control.*;
import Entity.*;
import adt.*;
import data.CSVLoader;
import exception.*;
import java.time.Duration;

/**
 *
 * @author calve
 */
public class ClinicApplication {
    // Shared ADTs
    private final Heap<Doctor> sharedDoc = new Heap<>(false);
    private final Heap<Visit> sharedVisitQueue = new Heap<>(true);
    private final LinkedHashMap<String,Treatment> providedTreatments = new LinkedHashMap<>();
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
    private final TreatmentUI treatmentUI;
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
        consultReport = new ConsultationReport(consultLog, apptManager);
        trtManager = new TreatmentManager(providedTreatments);
        medControl = new MedicineControl(medMap);
        medRecControl = new MedRecordControl(medRecList);

        consultUI = new ConsultationUI(docManager, apptManager, consultManager, trtManager, medControl, consultReport);
        patientUI = new PatientManagementUI(queueManager, patientManager);
        treatmentUI = new TreatmentUI(trtManager);
        pharReport = new PharmacyReport(medRecList,medMap);
        pharUI = new PharmacyUI(medRecControl, medControl, medCollectQueue, pharReport);
    }
    
    
    public void run(){
        CSVLoader.loadPatientFromCSV("src/data/patients.csv", patientManager);
        CSVLoader.loadDoctorsFromCSV("src/data/doctor.csv", docManager);
        CSVLoader.loadConsultRecFromCSV("src/data/consultation.csv", patientManager, docManager, consultLog);
        CSVLoader.loadTreatmentFromCSV("src/data/treatment.csv", trtManager);
        CSVLoader.loadMedicineFromCSV("src/data/medicine.csv", medControl);
        CSVLoader.loadMedRecordFromCSV("src/data/medicineRec.csv", patientManager, docManager, medControl, medRecList);

        int choice;
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
            case 4 -> treatmentUI.treatmentMenu();
            case 5 -> pharUI.pharmacyMenu();
            case 0 -> {
                System.out.println("Thank you for using the Clinic System!");
                System.exit(0);
            }
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("     CLINIC MANAGEMENT SYSTEM");
        System.out.println("=".repeat(35));
        System.out.println("1. Patient Registration System");
        System.out.println("2. Doctor Management System");
        System.out.println("3. Consultation System");
        System.out.println("4. Treatment System");
        System.out.println("5. Pharmacy Control System");
        System.out.println("0. Exit");
        System.out.println("=".repeat(35));
    }  
}
