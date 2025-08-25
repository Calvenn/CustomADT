package Boundary;
import Control.MedRecordControl;
import Control.MedicineControl;
import Control.PharmacyReport;
import Entity.MedRecord;
import Entity.Medicine;
import adt.Queue;
import exception.*;
import java.util.Scanner;


public class PharmacyUI {
    private final MedRecordControl medRecControl;
    private final MedicineControl medControl;
    private final PharmacyReport pharReport;
    private final Scanner scanner;
    private Queue<MedRecord> medCollectQueue = new Queue<>();
    
    public PharmacyUI(MedRecordControl medRecControl, MedicineControl medControl,Queue<MedRecord> medCollectQueue, PharmacyReport pharReport){
        this.medRecControl = medRecControl;
        this.medControl = medControl;
        this.scanner = new Scanner(System.in);
        this.medCollectQueue = medCollectQueue;
        this.pharReport = pharReport;
    }
    
    public void pharmacyMenuRead(){
        int choice;
        do{
            System.out.println("=========================");
            System.out.println("      Pharmacy Menu      ");
            System.out.println("=========================");
            System.out.println("1. View all medicines");
            System.out.println("2. View medicine record");
            System.out.println("3. Pharmacy Report");
            System.out.println("0. Back");
            System.out.println("=========================");
            System.out.print("Enter choice: ");

            choice = ValidationHelper.inputValidatedChoice(0,3, "your choice");

            switch (choice) {
                case 1 -> {medControl.displayAllMedicines();pressEnterToContinue();}

                case 2 -> viewRecord();
                
                case 3 -> pharReport();

                case 0 -> {System.out.println("Returning to main menu...");return;}

                default -> System.out.println("Invalid choice. Please try again.");
            }
        }while(choice!=0);
    }
    
    public void pharmacyMenu(){
        int choice;
        do{
            System.out.println("=========================");
            System.out.println("      Pharmacy Menu      ");
            System.out.println("=========================");
            System.out.println("1. Dispense medicine");
            System.out.println("2. Medicine Management");
            System.out.println("3. View medicine record");
            System.out.println("4. Pharmacy Report");
            System.out.println("0. Back");
            System.out.println("=========================");
            System.out.print("Enter choice: ");

            choice = ValidationHelper.inputValidatedChoice(0,4, "your choice");

            switch (choice) {
                case 1 -> {dispenseMed();pressEnterToContinue();}

                case 2 -> medManagement();

                case 3 -> viewRecord();
                
                case 4 -> pharReport();

                case 0 -> {System.out.println("Returning to main menu...");return;}

                default -> System.out.println("Invalid choice. Please try again.");
            }
        }while(choice!=0);
    }
    
    public void pharReport(){
        int choice;
        do{
            System.out.println("=========================");
            System.out.println("        Report Menu      ");
            System.out.println("=========================");
            System.out.println("1. Inactive Medicine Summary");
            System.out.println("2. Medicine Dispense Summary");
            System.out.println("3. Medicine Monthly Trend Summary");
            System.out.println("0. Back");
            System.out.println("=========================");
            System.out.print("Enter choice: ");

            choice = ValidationHelper.inputValidatedChoice(0,3, "your choice");

            switch (choice) {
                case 1 -> {pharReport.generateInactiveMedicinesReport();pressEnterToContinue();}
                
                case 2 -> {pharReport.generateMedicineDispenseSummary();pressEnterToContinue();}
                
                case 3 -> {pharReport.generateMonthlyTrendsReport();pressEnterToContinue();}

                case 0 -> {System.out.println("Returning to main menu...");return;}

                default -> System.out.println("Invalid choice. Please try again.");
            }
        }while(choice !=0);
    }
    
    public void dispenseMed() {
        System.out.println("=========================");
        System.out.println("    Dispense Medicine    ");
        System.out.println("=========================");

        if (medCollectQueue.isEmpty()) {
            System.out.println("No patients are waiting to collect medicine.");
            return;
        }
        int choice;
        do {
            MedRecord nextRecord = medCollectQueue.peek(); // look at next patient
            System.out.println("=========================");
            System.out.println("  Patient in Next Queue  ");
            System.out.println("=========================");
            System.out.println("Patient: " + nextRecord.getPatient().getPatientName()+ " (IC: " + nextRecord.getPatient().getPatientIC() + ")");
            System.out.println("Doctor: " + nextRecord.getDoctor().getName()+ " (ID: " + nextRecord.getDoctor().getID() + ")");
            System.out.println("Medicine: " + nextRecord.getMed().getName() + " (ID: " + nextRecord.getMed().getMedID() + ")");
            System.out.println("Quantity: " + nextRecord.getQuantityTaken());
            System.out.println("Requested At: " + nextRecord.getTimestamp());
            System.out.println("=========================\n");
            System.out.println("=========================");
            System.out.println("\n1. Dispense this medicine");
            System.out.println("2. Skip patient");
            System.out.println("0. Back to Pharmacy Menu");
            System.out.print("Enter choice: ");
            System.out.println("=========================");

            choice = ValidationHelper.inputValidatedChoice(0,2, "your choice");

            switch (choice) {
                case 1 -> {
                    char confirm = ValidationHelper.inputValidateYesOrNo("Dispense medicine to this patient?");
                    if(confirm == 'Y'){
                        MedRecord collectMed = medCollectQueue.dequeue();

                        // Step 2: Update medicine stock
                        Medicine med = collectMed.getMed();
                        int newStock = med.getStock() - collectMed.getQuantityTaken();
                        if (newStock < 0) newStock = 0; // avoid negative stock
                        medControl.updateStock(med.getMedID(), newStock);

                        boolean toSave = true;
                        // Step 3: Add this transaction into MedRecordControl
                        medRecControl.addRecord(
                                collectMed.getPatient(),
                                collectMed.getDoctor(),
                                med,
                                collectMed.getQuantityTaken(),
                                toSave,
                                collectMed.getConsult()
                        );

                        System.out.println("Medicine dispensed and record saved!");
                    }
                    if (medCollectQueue.isEmpty()) {
                        System.out.println("All patients have collected their medicine.");
                        return;
                    }
                }
                case 2 -> {
                    // move this patient to the back of the queue
                    medCollectQueue.enqueue(medCollectQueue.dequeue());
                    System.out.println("️ Patient moved to the back of the queue.");
                }
                case 0 -> {System.out.println("Returning to Pharmacy menu...");return;}
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice!=0 && !medCollectQueue.isEmpty());
    }

    
    public void medManagement() {
        int choice;
        do{
            System.out.println("=========================");
            System.out.println("   Medicine Management   ");
            System.out.println("=========================");
            medControl.displayLowStock(20);
            System.out.println("1. Add medicine");
            System.out.println("2. Update stock");
            System.out.println("3. Remove medicine");
            System.out.println("4. View all medicines");
            System.out.println("0. Back");
            System.out.println("=========================");
            System.out.print("Enter choice: ");

            choice = ValidationHelper.inputValidatedChoice(0,4, "your choice");

            switch (choice) {
                case 1 -> {addMedUI();pressEnterToContinue();}

                case 2 -> {updateStockUI();pressEnterToContinue();}

                case 3 -> {removeMedUI();pressEnterToContinue();}

                case 4 -> {medControl.displayAllMedicines();pressEnterToContinue();}

                case 0 -> {System.out.println("Returning to Pharmacy menu...");return;}

                default -> System.out.println("Invalid choice. Please try again.");
            }
        }while(choice !=0);
    }
    

    
    public void viewRecord(){
        int choice;
            do{
            System.out.println("=========================");
            System.out.println("  View Medicine Records  ");
            System.out.println("=========================");
            System.out.println("1. View all records");
            System.out.println("2. Search by Patient IC/Name");
            System.out.println("3. Search by Doctor ID/Name");
            System.out.println("4. Search by Medicine ID/Name");
            System.out.println("0. Back");
            System.out.println("=========================");
            System.out.print("Enter choice: ");

            choice = ValidationHelper.inputValidatedChoice(0,4, "your choice");

            switch (choice) {
                case 1 -> {medRecControl.viewAllRecords();pressEnterToContinue();}
                case 2 -> {
                    System.out.print("Enter Patient IC/Name: ");
                    String patient = scanner.nextLine().trim();
                    try {
                        TryCatchThrowFromFile.validateNotNull(patient);
                        medRecControl.searchByPatient(patient);
                    } catch (InvalidInputException e) {
                        ValidationUtility.printErrorWithSolution(e);
                    }
                    pressEnterToContinue();
                }
                case 3 -> {
                    System.out.print("Enter Doctor ID/Name: ");
                    String doctor = scanner.    nextLine().trim();
                    try {
                        TryCatchThrowFromFile.validateNotNull(doctor);
                        medRecControl.searchByDoctor(doctor);
                    } catch (InvalidInputException e) {
                        ValidationUtility.printErrorWithSolution(e);
                    }
                    pressEnterToContinue();
                }

                case 4 -> {
                    System.out.print("Enter Medicine ID/Name: ");
                    String med = scanner.nextLine().trim();
                    try {
                        TryCatchThrowFromFile.validateNotNull(med);
                        medRecControl.searchByMedicine(med);
                    } catch (InvalidInputException e) {
                        ValidationUtility.printErrorWithSolution(e);
                    }
                    pressEnterToContinue();
                }
                case 0 -> {System.out.println("Returning to Pharmacy menu...");return;}
                default -> System.out.println(" Invalid choice. Please try again.");
            }
        }while(choice !=0 );
    }
    
    public void addMedUI() {
        System.out.println("=== Add New Medicine ===");

        // Medicine ID
        System.out.print("Enter Medicine ID: ");
        String medID = scanner.nextLine().trim();
        try {
            TryCatchThrowFromFile.validateNotNull(medID);
            if (medControl.consists(medID)) {
                System.out.println("Medicine ID already exists! Cannot add.");
                return;
            }
        } catch (InvalidInputException e) {
            ValidationUtility.printErrorWithSolution(e);
            return;
        }

        // Medicine Name
        System.out.print("Enter Medicine Name: ");
        String name = scanner.nextLine().trim();
            try {
                TryCatchThrowFromFile.validateNotNull(name);
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
                return;
            }

            // Description
            System.out.print("Enter Description: ");
            String desc = scanner.nextLine().trim();
            try {
                TryCatchThrowFromFile.validateNotNull(desc);
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
                return;
            }

            // Stock Quantity
        System.out.print("Enter Stock Quantity: ");
        String stockInput = scanner.nextLine().trim();
        int stock = 0;
        try {
            TryCatchThrowFromFile.validatePositiveInteger(stockInput);
            stock = Integer.parseInt(stockInput);
        } catch (InvalidInputException e) {
            ValidationUtility.printErrorWithSolution(e);
            return;
        }

        Medicine newMed = new Medicine(medID, name.toLowerCase(), desc, stock);
        medControl.addMedicine(newMed);

        System.out.println("Medicine added successfully!");
    }

    
    
    public void updateStockUI() {
        System.out.println("=== Update Medicine Stock ===");

        // Validate Medicine ID
        System.out.print("Enter Medicine ID: ");
        String medID = scanner.nextLine().trim();
        try {
            TryCatchThrowFromFile.validateNotNull(medID);
        } catch (InvalidInputException e) {
            ValidationUtility.printErrorWithSolution(e);
            return;
        }

        Medicine med = medControl.findMedicineById(medID);
        if (med == null) {
            System.out.println("Medicine ID not found!");
            return;
        }

        System.out.println("Current stock for " + med.getName() + ": " + med.getStock());

        // Validate new stock
        System.out.print("Enter new stock quantity: ");
        String newStockInput = scanner.nextLine().trim();
        int newStock = 0;
        try {
            TryCatchThrowFromFile.validatePositiveInteger(newStockInput);
            newStock = Integer.parseInt(newStockInput);
        } catch (InvalidInputException e) {
            ValidationUtility.printErrorWithSolution(e);
            return;
        }

        if (medControl.updateStock(medID, newStock)) {
            System.out.println("Stock updated successfully!");
        } else {
            System.out.println("Failed to update stock.");
        }
    }

    
    public void removeMedUI() {
        System.out.println("=== Remove Medicine ===");

        // Validate Medicine ID
        System.out.print("Enter Medicine ID to remove: ");
        String medID = scanner.nextLine().trim();
        try {
            TryCatchThrowFromFile.validateNotNull(medID);
        } catch (InvalidInputException e) {
            ValidationUtility.printErrorWithSolution(e);
            return;
        }

        Medicine med = medControl.findMedicineById(medID);
        if (med == null) {
            System.out.println("Medicine ID not found!");
            return;
        }

        Character choice = ValidationHelper.inputValidateYesOrNo(
            "Are you sure you want to remove " + med.getName() + "?"
        );
        if (choice != 'Y') {
            System.out.println("Removal cancelled.");
            return;
        }

        if (medControl.removeMedicine(medID)) {
            System.out.println("Medicine removed successfully!");
        } else {
            System.out.println("Failed to remove medicine.");
        }
    }
    
    private void pressEnterToContinue() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}