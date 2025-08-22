package Boundary;
import java.util.Scanner;

import Control.QueueManager;
import Control.PatientManager;
import Control.VisitHistoryManager;

import Entity.Patient;

import exception.InvalidInputException;
import exception.TryCatchThrowFromFile;
import exception.ValidationHelper;
import exception.ValidationUtility;

public class PatientManagementUI {
    private QueueManager queueManager;
    private PatientManager patientManager;
    private VisitHistoryManager visitHistoryManager;
    private Scanner scanner;

    public PatientManagementUI(QueueManager queueManager, PatientManager patientManager, VisitHistoryManager visitHistoryManager) {
        this.queueManager = queueManager;
        this.patientManager = patientManager;
        this.visitHistoryManager = visitHistoryManager;
        this.scanner = new Scanner(System.in);
    }

    public void patientMenu() {
        int choice;
        do {
            displayMenu();
            choice = ValidationHelper.inputValidatedChoice(0, 8, "your choice");

            switch (choice) {
                case 1 -> handleNewPatientRegistration();
                case 2 -> handleVisitRegistration();
                case 3 -> handleSearchAndDisplayHistory();
                case 4 -> handleUpdatePatient();
                case 5 -> handleDeletePatient();
                case 6 -> handleDisplayAllPatients();
                case 7 -> handlePatientStatistics();
                case 8 -> handleClearAllPatients();
            }

            if (choice != 0) pauseForUser();
        } while (choice != 0);
    }

    private void displayMenu() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("      PATIENT MANAGEMENT MENU");
        System.out.println("=".repeat(35));
        System.out.println("1. Add New Patient");
        System.out.println("2. Register Visit");
        System.out.println("3. Search Patient");
        System.out.println("4. Update Patient Information");
        System.out.println("5. Delete Patient");
        System.out.println("6. Display All Patients");
        System.out.println("7. Patient Statistics");
        System.out.println("8. Clear All Patients");
        System.out.println("0. Exit");
        System.out.println("=".repeat(40));
    }

    private void handleNewPatientRegistration() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("    New Patient Registration");
        System.out.println("=".repeat(35));

        // IC
        String ic;
        while (true) {
            ic = ValidationHelper.inputValidatedIC("Enter IC number");
            if (patientManager.isPatientExist(ic)) {
                System.out.println("Patient already exists!");
                patientManager.displayPatientDetails(patientManager.findPatientByIC(ic));
                return; // stop registration
            }
            break;
        }

        String name = ValidationHelper.inputValidatedString("\nEnter name: ");
        String phone = ValidationHelper.inputValidatedString("\nEnter phone number: ");
        int age = ValidationHelper.inputValidatedPositiveInt("\nEnter age: ");
        char gender = ValidationHelper.inputValidatedGender("\nEnter gender (M/F): ");
        String address = ValidationHelper.inputValidatedString("\nEnter address: ");

        Patient newPatient = patientManager.registerNewPatient(ic, name, phone, age, gender, address);
        if (newPatient != null) {
            System.out.println("\nPatient registered successfully!");
            System.out.println(newPatient);
        } else {
            System.out.println("\nFailed to register patient.");
        }
    }

    private void handleVisitRegistration() {
        VisitRegistrationUI visitUI = new VisitRegistrationUI(queueManager, patientManager, visitHistoryManager);
        visitUI.visitsMenu();
    }

    public Patient handleSearchPatient() {
        while (true) {
            System.out.print("Enter IC number to search (or 0 to exit): ");
            String ic = scanner.nextLine().trim();

            if (ic.equals("0")) {
                return null;
            }

            String validatedIC = ValidationHelper.validateICOnce(ic);
            if (validatedIC == null) continue;

            // Find patient using findObjectOrThrow
            try {
                Patient patient = TryCatchThrowFromFile.findObjectOrThrow(patientManager.getAllPatients(), Patient::getPatientIC, validatedIC,"Patient", "IC number");
                System.out.println("\nPatient found: \n" + patient);
                return patient;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
    }

    private void handleSearchAndDisplayHistory() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("      Search Patient History");
        System.out.println("=".repeat(30));
        Patient patient = handleSearchPatient(); 
        if (patient != null) {  
            VisitsHistoryUI historyUI = new VisitsHistoryUI(visitHistoryManager);
            historyUI.displayPatientHistory(patient);
        }
    }

    private void handleUpdatePatient() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("    Update Patient Information");
        System.out.println("=".repeat(35));

        Patient existingPatient = handleSearchPatient();
        if (existingPatient == null) {
            return;
        }

        System.out.println("\nEnter new information (press Enter to keep current value):");
        String finalName = ValidationHelper.inputOptionalValidatedString("New name", existingPatient.getPatientName());
        String finalPhone = ValidationHelper.inputOptionalValidatedPhone("New phone", existingPatient.getPatientPhoneNo());
        int finalAge = ValidationHelper.inputOptionalValidatedPositiveInt("New age", existingPatient.getPatientAge());
        char finalGender = ValidationHelper.inputOptionalValidatedGender("New gender", existingPatient.getPatientGender());
        String finalAddress = ValidationHelper.inputOptionalValidatedString("New address", existingPatient.getPatientAddress());
        Patient updatedPatient = new Patient(existingPatient.getPatientIC(), finalName, finalPhone, finalAge, finalGender, finalAddress);

        if (patientManager.updatePatient(existingPatient.getPatientIC(), updatedPatient)) {
            System.out.println("\nPatient information updated successfully!");
            System.out.println("\nUpdated Patient Details:");
            System.out.println(updatedPatient);
        } else {
            System.out.println("\nFailed to update patient information.");
        }
    }

    private void handleDeletePatient() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("        Delete Patient");
        System.out.println("=".repeat(30));

        // Step 1: Reuse search logic to get the patient
        Patient patient = handleSearchPatient();
        if (patient == null) return;

        char confirm = ValidationHelper.inputValidateYesOrNo("\nAre you sure you want to delete this patient?");

        if (confirm == 'Y') {
            Patient removedPatient = patientManager.removePatient(patient.getPatientIC());
            if (removedPatient != null) {
                System.out.println("\nPatient deleted successfully!");
            } else {
                System.out.println("\nFailed to delete patient.");
            }
        } else {
            System.out.println("\nPatient deletion cancelled.");
        }
    }

    private void handleDisplayAllPatients() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("        All Patients");
        System.out.println("=".repeat(30));

        if (patientManager.isEmpty()) {
            System.out.println("\nNo patients in the system.");
            return;
        }

        Patient[] patients = patientManager.getAllPatients();
        int pageSize = 10;
        int totalPatients = patients.length;
        int page = 0;

        while (page * pageSize < totalPatients) {
            int start = page * pageSize;
            int end = Math.min(start + pageSize, totalPatients);

            patientTableHeader();

            for (int i = start; i < end; i++) {
                Patient patient = patients[i];
                System.out.printf("| %-15s | %-20s | %-15s | %-5d | %-8s | %-40s |\n", patient.getPatientIC(), patient.getPatientName(), patient.getPatientPhoneNo(), patient.getPatientAge(), patient.getPatientGender(), patient.getPatientAddress());
                System.out.println("-".repeat(122));
            }
            page++;
            if (end < totalPatients) {
                System.out.print("\nPress Enter to see next page...");
                scanner.nextLine();
            }
        }
    }

    public void patientTableHeader() {
        System.out.println("-".repeat(122));
        System.out.println(String.format("| %-15s | %-20s | %-15s | %-5s | %-8s | %-40s |", "Patient IC", "Patient Name", "Phone Number", "Age", "Gender", "Address"));
        System.out.println("-".repeat(122));
    }

    private void handlePatientStatistics() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("    Patient Statistics Reports");
        System.out.println("=".repeat(35));
        
        int totalPatients = patientManager.getTotalPatients();
        System.out.println("        Total Patients: " + totalPatients);
        System.out.println("=".repeat(40));
        
        if (totalPatients == 0) {
            System.out.println("No patients in the system.");
            return;
        }

        System.out.println("1. Age Distribution Report");
        System.out.println("2. Gender Distribution Report");
        System.out.println("0. Back");
        System.out.println("=".repeat(40));
        int choice;
        do {
            choice = ValidationHelper.inputValidatedChoice(0, 2, "your choice");
            switch (choice) {
                case 1 -> displayAgeDistribution();
                case 2 -> displayGenderDistribution();
            }
        } while (choice != 0);
    }

    // Helper method to display chart + table
    private void printChartAndTable(String title, String[] labels, int[] counts) {
        int total = 0;
        for (int c : counts) total += c;
        
        // Compute percentages
        double[] percentages = new double[counts.length];
        for (int i = 0; i < counts.length; i++) {
            percentages[i] = (total > 0) ? (counts[i] * 100.0 / total) : 0;
        }

        System.out.println("\n" + "=".repeat(40));
        System.out.println("       " + title + " CHART");
        System.out.println("=".repeat(40));

        int maxBarLength = 30;
        int maxCount = 0;
        for (int c : counts) if (c > maxCount) maxCount = c;

        for (int i = 0; i < labels.length; i++) {
            int barLength = (maxCount == 0) ? 0 : (int)((counts[i] * maxBarLength * 1.0) / maxCount);
            System.out.printf("%-7s | %s%n", labels[i], "*".repeat(barLength));
        }

        System.out.println("-".repeat(40));

        // Table
        System.out.println("       " + title + " TABLE");
        System.out.println("-".repeat(40));
        if (title.toLowerCase().contains("age")) {
            System.out.printf("%-10s %-8s %-10s%n", "Range", "Count", "Percentage");
        } else {
            System.out.printf("%-8s %-8s %-10s%n", "Gender", "Count", "Percentage");
        }
        System.out.println("-".repeat(40));

        for (int i = 0; i < labels.length; i++) {
            System.out.printf("%-10s %-8d %-9.2f%%%n", labels[i], counts[i], percentages[i]);
        }

        System.out.println("-".repeat(40));
        System.out.println("Total Patients: " + total);
    }

    private void displayAgeDistribution() {
        Patient[] patients = patientManager.getAllPatients();
        int[] ageGroups = new int[5]; // 0–18, 19–35, 36–50, 51–65, 65+

        for (Patient p : patients) {
            int age = p.getPatientAge();
            if (age <= 18) ageGroups[0]++;
            else if (age <= 35) ageGroups[1]++;
            else if (age <= 50) ageGroups[2]++;
            else if (age <= 65) ageGroups[3]++;
            else ageGroups[4]++;
        }

        String[] ranges = {"0-18", "19-35", "36-50", "51-65", "65+"};
        printChartAndTable("AGE DISTRIBUTION", ranges, ageGroups);
    }

    private void displayGenderDistribution() {
        Patient[] patients = patientManager.getAllPatients();
        int maleCount = 0, femaleCount = 0;

        for (Patient p : patients) {
            if (Character.toUpperCase(p.getPatientGender()) == 'M') maleCount++;
            else if (Character.toUpperCase(p.getPatientGender()) == 'F') femaleCount++;
        }

        String[] labels = {"Male", "Female"};
        int[] counts = {maleCount, femaleCount};
        printChartAndTable("GENDER DISTRIBUTION", labels, counts);
    }

    private void handleClearAllPatients() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("      CLEAR ALL PATIENTS");
        System.out.println("=".repeat(35));

        if (patientManager.isEmpty()) {
            System.out.println("\nNo patients to clear.");
            return;
        }

        System.out.println("Current total patients: " + patientManager.getTotalPatients());

        // Use validation helper for Y/N confirmation
        char confirmation = ValidationHelper.inputValidateYesOrNo(
            "\nAre you sure you want to clear ALL patients? This cannot be undone!"
        );

        if (confirmation == 'Y') {
            patientManager.clearAllPatients();
            System.out.println("\nAll patients have been cleared from the system.");
        } else {
            System.out.println("\nClear operation cancelled.");
        }
    }

    private void pauseForUser () {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}