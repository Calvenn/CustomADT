package Main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import Boundary.ConsultationUI;
import Boundary.PatientManagementUI;
import Control.AppointmentManager;
import Control.QueueManager;
import Entity.Appointment;
import Entity.Visit;
import adt.ADTHeap;
import java.util.Scanner;
/**
 *
 * @author calve
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ADTHeap<Visit> sharedVisitQueue = new ADTHeap<>(true);
        ADTHeap<Appointment> sharedApptHeap = new ADTHeap<>(false);
        
        AppointmentManager apptManager = new AppointmentManager(sharedApptHeap);
        QueueManager queueManager = new QueueManager(sharedVisitQueue);
        ConsultationUI consultUI = new ConsultationUI(sharedVisitQueue, apptManager);
        PatientManagementUI patientUI = new PatientManagementUI(sharedVisitQueue);
        
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
}    

