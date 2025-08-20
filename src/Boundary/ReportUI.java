/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;
import Control.ConsultationReport;
import Control.PharmacyReport;
import java.util.Scanner;
/**
 *
 * @author calve
 */
public class ReportUI {
   private final ConsultationReport consultReport;
   private final PharmacyReport pharmacyReport;
   Scanner scanner;
   public ReportUI(ConsultationReport consultReport, PharmacyReport pharmacyReport){
       scanner = new Scanner(System.in);
       this.consultReport = consultReport;
       this.pharmacyReport = pharmacyReport;
   } 
   
   public void reportMainMenu(){
       int choice;
        do {
            displayMainMenu();
            while (true) {
                try {
                    System.out.print("Enter your choice: ");
                    choice = Integer.parseInt(scanner.nextLine());
                    if (choice < 1 || choice > 7) {
                        System.out.println("Invalid choice. Please enter 1 to 3.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            switch (choice) {
                //case 1 -> patientUI.patientMenu();
                //case 2 -> consultUI.consultMainMenu();
                case 3 -> consultationReportMenu(); 
                //case 4 -> consultUI.consultMainMenu();
                case 5 -> PharmacyReportModule();
                case 6 -> System.out.println("Thank you for using the Clinic System!");
            }
        } while (choice != 6);
    }
   
   private void displayMainMenu() {
        System.out.println("\n=== Clinic Report ===");
        System.out.println("1. Patient Report");
        System.out.println("2. Doctor Report");
        System.out.println("3. Consultation Report");
        System.out.println("4. Treatment Report");
        System.out.println("5. Pharmacy Report");
        System.out.println("6. Exit");
    }
   
   private void consultationReportMenu(){
       System.out.println("\n=== Consultation Report ===");
        System.out.println("1. Consultation Outcome Trends");
        //System.out.println("2. Doctor Report");
        
        System.out.print("Choose > ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // clear newline
        
        switch (choice) {
            case 1 -> consultReport.consultationOutcomeTrends();
            //case 2 -> 
            case 3 -> {
                System.out.println("Returning to main menu");
                return;
            }
            default -> System.out.println("Invalid choice.\n");
        }
   }
   
   private void PharmacyReportModule(){
       System.out.println("\n=== Pharmacy Report Menu ===");
        System.out.println("1. Medicine Usage by Patient");
        System.out.println("2. Medicine Dispense Summary");
        System.out.println("3. Back to Main Menu");
        
        System.out.print("Choose > ");
        int choice = scanner.nextInt();
        scanner.nextLine(); 
        
        switch (choice) {
            //case 1 -> pharmacyReport.generatePatientMedicineReport();
            //case 2 -> pharmacyReport.generateMedicineSummaryReport();
            case 3 -> {
                System.out.println("Returning to main menu");
                return;
            }
            default -> System.out.println("Invalid choice.\n");
        }
   }
}
