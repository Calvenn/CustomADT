package Main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import Boundary.ConsultationUI;
import Boundary.PatientManagementUI;

import Control.DoctorManager;
import Control.AppointmentManager;
import Control.ConsultationManager;
import Control.MedicineControl;
import Control.QueueManager;
import Control.TreatmentManager;

import Entity.Doctor;
import Entity.Appointment;
import Entity.MedRecord;
import Entity.Medicine;
import Entity.Treatment;
import Entity.TreatmentAppointment;
import Entity.Visit;

import adt.ADTHeap;
import adt.ADTQueue;

import java.time.LocalDate;
import java.time.Duration; 
import java.util.Scanner;
/**
 *
 * @author calve
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ADTHeap<Doctor> sharedDoc = new ADTHeap<>(false);
        ADTHeap<Visit> sharedVisitQueue = new ADTHeap<>(true);
        ADTHeap<Appointment> sharedApptHeap = new ADTHeap<>(false);
        ADTHeap<Treatment> providedTreatments = new ADTHeap<>(true);
        ADTHeap<Medicine> lowStockMed = new ADTHeap<>(false);
        
        ADTQueue<TreatmentAppointment> treatmentQueue = new ADTQueue<>();
        ADTQueue<MedRecord> medCollectQueue = new ADTQueue<>();
           
        DoctorManager docManager = new DoctorManager(sharedDoc);
        AppointmentManager apptManager = new AppointmentManager(sharedApptHeap);
        QueueManager queueManager = new QueueManager(sharedVisitQueue, docManager);
        ConsultationManager consultManager = new ConsultationManager(sharedVisitQueue, apptManager.getAppointmentHeap(), docManager, treatmentQueue, medCollectQueue);
        TreatmentManager trtManager = new TreatmentManager(providedTreatments); 
        MedicineControl medControl = new MedicineControl(lowStockMed);
        
        loadDummyDoctors(docManager);
        loadDummyTreatment(trtManager);
        loadDummyMed(medControl);

        ConsultationUI consultUI = new ConsultationUI(docManager, apptManager, consultManager, trtManager, medControl);
        PatientManagementUI patientUI = new PatientManagementUI(sharedVisitQueue, queueManager, docManager);

        int choice;
        do {
            displayMainMenu();
            while (true) {
                try {
                    System.out.print("Enter your choice: ");
                    choice = Integer.parseInt(scanner.nextLine());
                    if (choice < 1 || choice > 66) {
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
                //case 5 -> consultUI.consultMainMenu();
                case 6 -> {
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
        System.out.println("6. Exit");
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
        Medicine med = new Medicine("Panadol", "Panadol", "testest", "June", 100);
        medControl.addMedicine(med);
    }
}


