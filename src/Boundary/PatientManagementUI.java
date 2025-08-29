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

/**
 *
 * @author NgPuiYin
 */
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
        } while (choice != 0);
    }

    private void displayMenu() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("     PATIENT MANAGEMENT MENU");
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

        String id = ValidationHelper.inputValidatedStudentID("\nEnter Student ID: ");
        String name = ValidationHelper.inputValidatedString("\nEnter name: ");
        String phone = ValidationHelper.inputValidatedPhone("\nEnter phone number: ");
        int age = ValidationHelper.inputValidatedPositiveInt("\nEnter age: ");
        char gender = ValidationHelper.inputValidatedGender("\nEnter gender (M/F): ");
        String address = ValidationHelper.inputValidatedString("\nEnter address: ");

        Patient newPatient = patientManager.registerNewPatient(ic, id, name, phone, age, gender, address);
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

    // Search by Student ID
    private Patient handleSearchByStudentID() {
        while (true) {
            System.out.print("Enter Student ID to search (or 0 to exit): ");
            String studentID = scanner.nextLine().trim();

            if (studentID.equals("0")) return null;

            try {
                Patient patient = TryCatchThrowFromFile.findObjectOrThrow(
                        patientManager.getAllPatients(),
                        Patient::getStudentID,
                        studentID,
                        "Patient",
                        "Student ID"
                );
                System.out.println("\nPatient found:\n" + patient);
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

        // Ask user which field to search by
        System.out.println("Search patient by:");
        System.out.println("1. IC Number");
        System.out.println("2. Student ID");
        System.out.println("0. Cancel");
        int choice = ValidationHelper.inputValidatedChoice(0, 2, "your choice");

        Patient patient = null;
        switch (choice) {
            case 0 -> {
                System.out.println("Search cancelled.");
                return;
            }
            case 1 -> patient = handleSearchPatient(); // existing IC search
            case 2 -> patient = handleSearchByStudentID(); // new Student ID search
        }

        if (patient != null) {
            VisitsHistoryUI historyUI = new VisitsHistoryUI(visitHistoryManager);
            historyUI.displayPatientHistory(patient);
        } else {
            System.out.println("No patient selected.");
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
        String newStudentID = ValidationHelper.inputOptionalValidatedString("New Student ID", existingPatient.getStudentID());
        String finalName = ValidationHelper.inputOptionalValidatedString("New name", existingPatient.getPatientName());
        String finalPhone = ValidationHelper.inputOptionalValidatedPhone("New phone", existingPatient.getPatientPhoneNo());
        int finalAge = ValidationHelper.inputOptionalValidatedPositiveInt("New age", existingPatient.getPatientAge());
        char finalGender = ValidationHelper.inputOptionalValidatedGender("New gender", existingPatient.getPatientGender());
        String finalAddress = ValidationHelper.inputOptionalValidatedString("New address", existingPatient.getPatientAddress());
        Patient updatedPatient = new Patient(existingPatient.getPatientIC(), newStudentID, finalName, finalPhone, finalAge, finalGender, finalAddress);

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
        if (patient == null) {
            System.out.println("\nPatient deletion cancelled."); 
            return;
        }

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

        System.out.println("View patients group by:");
        System.out.println("1. Male");
        System.out.println("2. Female");
        System.out.println("3. All");
        System.out.println("0. Exit");
        int choice = ValidationHelper.inputValidatedChoice(0, 3, "your choice");

        if (choice == 0) return; // exit

        Patient[] displayList = new Patient[0];

        switch (choice) {
            case 1 -> displayList = patientManager.getPatientsByGender('M');
            case 2 -> displayList = patientManager.getPatientsByGender('F');
            case 3 -> displayList = patientManager.getAllPatients();
        }

        // Ask for sorting
        System.out.println("\nSort by age?");
        System.out.println("1. Oldest first");
        System.out.println("2. Youngest first");
        System.out.println("0. No sorting");
        int sortChoice = ValidationHelper.inputValidatedChoice(0, 2, "your choice");

        if (sortChoice == 1) patientManager.sortPatientsByAge(displayList, true);
        else if (sortChoice == 2) patientManager.sortPatientsByAge(displayList, false);

        patientTableHeader();
        for (Patient patient : displayList) {
            System.out.printf("| %-15s | %-15s | %-20s | %-15s | %-5d | %-8s | %-40s |\n",
                    patient.getPatientIC(),
                    patient.getStudentID(),
                    patient.getPatientName(),
                    patient.getPatientPhoneNo(),
                    patient.getPatientAge(),
                    patient.getPatientGender(),
                    patient.getPatientAddress());
            System.out.println("-".repeat(140));
        }
    }

    public void patientTableHeader() {
        System.out.println("-".repeat(140));
        System.out.println(String.format("| %-15s | %-15s | %-20s | %-15s | %-5s | %-8s | %-40s |", "Patient IC", "Student ID", "Patient Name", "Phone Number", "Age", "Gender", "Address"));
        System.out.println("-".repeat(140));
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
        System.out.println("0. Back");
        System.out.println("=".repeat(40));
        int choice;
        choice = ValidationHelper.inputValidatedChoice(0, 1, "your choice");
        switch (choice) {
            case 1 -> displayGenderDistribution();
            case 0 -> System.out.println("Returning to previous menu.");
        }
    }

    // Helper method to display chart + table
    private void printChartAndTable(String title, String[] labels, int[] counts) {
        // Calculate the total count
        int total = 0;
        for (int i = 0; i < counts.length; i++) {
            total += counts[i];
        }

        // Print the header for the chart
        System.out.println("\n" + "=".repeat(40));
        System.out.println("       " + title + " CHART");
        System.out.println("=".repeat(40));

        int maxBarLength = 28; // maximum number of stars
        int maxCount = 0;
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > maxCount) {
                maxCount = counts[i];
            }
        }

        // Print the bar chart
        for (int i = 0; i < labels.length; i++) {
            int barLength = 0;
            if (maxCount > 0) {
                barLength = (counts[i] * maxBarLength) / maxCount;
            }
            System.out.printf("%-8s | %s%n", labels[i], "*".repeat(barLength));
        }

        // Print separator and table header
        System.out.println("-".repeat(40));
        System.out.println("       " + title + " TABLE");
        System.out.println("-".repeat(40));

        // Print table with counts and percentages
        System.out.printf("%-10s %-10s %-10s%n", "Label", "Count", "Percentage");
        System.out.println("-".repeat(40));
        for (int i = 0; i < labels.length; i++) {
            double percent = (total > 0) ? (counts[i] * 100.0 / total) : 0;
            System.out.printf("%-10s %-10d %-9.2f%%%n", labels[i], counts[i], percent);
        }

        System.out.println("-".repeat(40));
        System.out.println("Total: " + total);
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
        System.out.println("      CLEAR PATIENTS");
        System.out.println("=".repeat(35));

        if (patientManager.isEmpty()) {
            System.out.println("\nNo patients to clear.");
            return;
        }

        System.out.println("Current total patients: " + patientManager.getTotalPatients());
        System.out.println("\nChoose an option:");
        System.out.println("1. Clear ALL patients");
        System.out.println("2. Clear patients aged 31 and above");
        System.out.println("0. Cancel");
        int choice = ValidationHelper.inputValidatedChoice(0, 2, "your choice");

        switch (choice) {
            case 0 -> {
                System.out.println("\nClear operation cancelled.");
                return;
            }
            case 1 -> {
                char confirmAll = ValidationHelper.inputValidateYesOrNo("\nAre you sure you want to clear ALL patients? This cannot be undone!");
                if (confirmAll == 'Y') {
                    patientManager.clearAllPatients();
                    System.out.println("\nAll patients have been cleared from the system.");
                } else {
                    System.out.println("\nClear operation cancelled.");
                }
            }
            case 2 -> {
                Patient[] above30 = patientManager.getPatientsAbove30();
                
                if (above30.length == 0) {
                    System.out.println("\nNo patients aged 31 and above.");
                    break;
                }
                
                System.out.println("\nPatients aged 31 and above:");
                patientTableHeader();
                for (Patient p : above30) {
                    System.out.printf("| %-15s | %-15s | %-20s | %-15s | %-5d | %-8s | %-40s |\n",
                            p.getPatientIC(),
                            p.getStudentID(),
                            p.getPatientName(),
                            p.getPatientPhoneNo(),
                            p.getPatientAge(),
                            p.getPatientGender(),
                            p.getPatientAddress());
                    System.out.println("-".repeat(140));
                }

                char confirm30 = ValidationHelper.inputValidateYesOrNo(
                        "\nAre you sure you want to clear all patients with age 31 and above?");
                if (confirm30 == 'Y') {
                    patientManager.removePatientsAbove30();
                    System.out.println("\nPatients with age 31 and above have been removed.");
                } else {
                    System.out.println("\nClear operation cancelled.");
                }
            }
        }
    }
}