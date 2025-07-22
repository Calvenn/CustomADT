package Main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import Boundary.ConsultationUI;
import Boundary.PatientManagementUI;
import Control.DoctorManager;
import Control.AppointmentManager;
import Control.QueueManager;
import Entity.Doctor;
import Entity.Appointment;
import Entity.Visit;
import adt.ADTHeap;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        
        DoctorManager docManager = new DoctorManager(sharedDoc);
        AppointmentManager apptManager = new AppointmentManager(sharedApptHeap);
        QueueManager queueManager = new QueueManager(sharedVisitQueue, docManager);
        
        //loadDummyAppointments(apptManager);
        loadDummyDoctors(docManager);
        
        ConsultationUI consultUI = new ConsultationUI(sharedVisitQueue, docManager, apptManager);
        PatientManagementUI patientUI = new PatientManagementUI(sharedVisitQueue, queueManager, docManager);
        
        int choice;
        do {
            displayMainMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            
            switch(choice) {
                case 1:
                    // Patient Management System
                    patientUI.patientMenu();
                    break;
                case 2:
                    // Consultation System                  
                    consultUI.consultMainMenu();
                    break;
                case 3:
                    System.out.println("\nThank you for using the Hospital System!");
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        } while (choice != 3);
    }
    
    private static void displayMainMenu() {
        System.out.println("\n=== Clinic Management System ===");
        System.out.println("1. Patient Registration System");
        System.out.println("2. Consultation System");
        System.out.println("3. Exit");
        System.out.print("\nEnter your choice: ");
    }
    /*
    private static void loadDummyAppointments(AppointmentManager apptManager) {
        apptManager.bookAppointment("Alice", "012-1231234", "Dr. Tan", 2, LocalDateTime.now().plusWeeks(1).withHour(9).withMinute(0));
        apptManager.bookAppointment("Bob", "012-2231234", "Dr. Lim", 2, LocalDateTime.now().plusWeeks(1).withHour(9).withMinute(30));
        apptManager.bookAppointment("David", "012-2231234", "Dr. Ang", 3, LocalDateTime.now().plusWeeks(1).withHour(10).withMinute(0));
    } */
    
    private static void loadDummyDoctors(DoctorManager docManager) {
        docManager.addNewDoctor("D001", "John", 30, "012-1231234", "Man", "Head", LocalDate.now());
        docManager.addNewDoctor("D002", "Spider Man", 25, "012-1231234", "Man", "Doctor", LocalDate.now());
        docManager.addNewDoctor("D003", "Iron Man", 26, "012-1231234", "Man", "Assistant", LocalDate.now());
    }   
}    

