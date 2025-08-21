package Boundary;
import java.util.Scanner;

import Control.QueueManager;
import Control.PatientManager;

import Entity.Patient;

import exception.InvalidInputException;
import exception.TryCatchThrowFromFile;
import exception.ValidationUtility;

public class PatientManagementUI {
    private QueueManager queueManager;
    private PatientManager patientManager;
    private Scanner scanner;

    public PatientManagementUI(QueueManager queueManager) {
        this.queueManager = queueManager;
        this.patientManager = new PatientManager();
        this.scanner = new Scanner(System.in);
    }

    public void patientMenu() {
        int choice;
        do {
            displayMenu();
            
            while (true) {
                try {
                    System.out.print("Enter your choice: ");
                    String input = scanner.nextLine().trim();
                    
                    // Use your validation system directly
                    TryCatchThrowFromFile.validateIntegerRange(input, 0, 8);
                    choice = Integer.parseInt(input);
                    break; // Valid input, exit the input loop
                    
                } catch (InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }

            switch (choice) {
                case 1:
                    handleNewPatientRegistration();
                    break;
                case 2:
                    handleVisitRegistration();
                    break;
                case 3:
                    handleSearchPatient();
                    break;
                case 4:
                    handleUpdatePatient();
                    break;
                case 5:
                    handleDeletePatient();
                    break;
                case 6:
                    handleDisplayAllPatients();
                    break;
                case 7:
                    handlePatientStatistics();
                    break;
                case 8:
                    handleClearAllPatients();
                    break;
                case 0:
                    break;
            }
            
            if (choice != 0) {
                pauseForUser();
            }
        } while (choice != 0);
    }

    private void displayMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("    PATIENT MANAGEMENT SYSTEM");
        System.out.println("=".repeat(40));
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
        System.out.println("    NEW PATIENT REGISTRATION");
        System.out.println("=".repeat(35));

        String ic;
        while (true) {
            System.out.print("Enter IC number: ");
            ic = scanner.nextLine();
            try {
                TryCatchThrowFromFile.validateIC(ic);
                if (patientManager.isPatientExist(ic)) {
                    System.out.println("Patient already exists!");
                    patientManager.displayPatientDetails(patientManager.findPatientByIC(ic));
                    return; // stop registration
                }
                break; // valid IC
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }

        String name;
        while (true) {
            System.out.print("\nEnter name: ");
            name = scanner.nextLine();
            try {
                TryCatchThrowFromFile.validateNotNull(name);
                break;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }

        String phone;
        while (true) {
            System.out.print("\nEnter phone number: ");
            phone = scanner.nextLine();
            try {
                TryCatchThrowFromFile.validatePhone(phone);
                break;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }

        String ageStr;
        while (true) {
            System.out.print("\nEnter age: ");
            ageStr = scanner.nextLine();
            try {
                TryCatchThrowFromFile.validatePositiveInteger(ageStr);
                break;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }

        char gender;
        while (true) {
            System.out.print("\nEnter gender (M/F): ");
            gender = scanner.nextLine().charAt(0);
            try {
                TryCatchThrowFromFile.validateGender(gender);
                break;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }

        String address;
        while (true) {
            System.out.print("\nEnter address: ");
            address = scanner.nextLine();
            try {
                TryCatchThrowFromFile.validateNotNull(address);
                break;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }

        Patient newPatient = null;
        try {
            newPatient = patientManager.registerNewPatient(ic, name, phone, ageStr, gender, address);
            System.out.println("\nPatient registered successfully!");
            patientManager.displayPatientDetails(newPatient);
        } catch (InvalidInputException e) {
            ValidationUtility.printErrorWithSolution(e);
        }
    }

    private void handleVisitRegistration() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("    VISIT REGISTRATION");
        System.out.println("=".repeat(35));

        VisitRegistrationUI visitUI = new VisitRegistrationUI(queueManager, patientManager);
        visitUI.visitsMenu();
    }

    private Patient handleSearchPatient() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("      SEARCH PATIENT");
        System.out.println("=".repeat(30));

        Patient patient = null;

        while (true) {
            try {
                System.out.print("Enter IC number to search: ");
                String ic = scanner.nextLine();

                // Validate IC format
                TryCatchThrowFromFile.validateIC(ic);

                // Find patient
                patient = patientManager.findPatientByIC(ic);
                if (patient != null) {
                    System.out.println("\nPatient found: \n" + patient);
                    return patient;
                } else {
                    throw new InvalidInputException("Patient not found with IC " + ic);
                }

            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
    }

    private void handleUpdatePatient() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("    UPDATE PATIENT INFORMATION");
        System.out.println("=".repeat(35));

        Patient existingPatient = null;

        // 1. Get valid IC and find patient
        while (true) {
            try {
                System.out.print("Enter IC number of patient to update: ");
                String ic = scanner.nextLine();
                TryCatchThrowFromFile.validateIC(ic);

                existingPatient = patientManager.findPatientByIC(ic);
                if (existingPatient == null) {
                    throw new InvalidInputException("Patient not found with IC: " + ic);
                }
                break; // found and valid
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }

        // 2. Show current details
        System.out.println("\nCurrent patient details:");
        patientManager.displayPatientDetails(existingPatient);
        System.out.println("\nEnter new information (press Enter to keep current value):");

        // Name
        String finalName;
        while (true) {
            try {
                System.out.print("New name [" + existingPatient.getPatientName() + "]: ");
                String input = scanner.nextLine();
                if (input.trim().isEmpty()) {
                    finalName = existingPatient.getPatientName();
                    break;
                }
                TryCatchThrowFromFile.validateNotNull(input);
                finalName = input;
                break;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }

        // Phone
        String finalPhone;
        while (true) {
            try {
                System.out.print("New phone [" + existingPatient.getPatientPhoneNo() + "]: ");
                String input = scanner.nextLine();
                if (input.trim().isEmpty()) {
                    finalPhone = existingPatient.getPatientPhoneNo();
                    break;
                }
                TryCatchThrowFromFile.validatePhone(input);
                finalPhone = input;
                break;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }

        // Age
        int finalAge;
        while (true) {
            try {
                System.out.print("New age [" + existingPatient.getPatientAge() + "]: ");
                String input = scanner.nextLine();
                if (input.trim().isEmpty()) {
                    finalAge = existingPatient.getPatientAge();
                    break;
                }
                TryCatchThrowFromFile.validatePositiveInteger(input);
                finalAge = Integer.parseInt(input);
                break;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }

        // Gender
        char finalGender;
        while (true) {
            try {
                System.out.print("New gender [" + existingPatient.getPatientGender() + "]: ");
                String input = scanner.nextLine();
                if (input.trim().isEmpty()) {
                    finalGender = existingPatient.getPatientGender();
                    break;
                }
                TryCatchThrowFromFile.validateGender(input.toUpperCase().charAt(0));
                finalGender = input.toUpperCase().charAt(0);
                break;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }

        // Address
        String finalAddress;
        while (true) {
            try {
                System.out.print("New address [" + existingPatient.getPatientAddress() + "]: ");
                String input = scanner.nextLine();
                if (input.trim().isEmpty()) {
                    finalAddress = existingPatient.getPatientAddress();
                    break;
                }
                TryCatchThrowFromFile.validateNotNull(input);
                finalAddress = input;
                break;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }

        // 3. Update patient
        Patient updatedPatient = new Patient(existingPatient.getPatientIC(), finalName, finalPhone, finalAge, finalGender, finalAddress);

        if (patientManager.updatePatient(existingPatient.getPatientIC(), updatedPatient)) {
            System.out.println("\nPatient information updated successfully!");
            patientManager.displayPatientDetails(updatedPatient);
        } else {
            System.out.println("\nFailed to update patient information.");
        }
    }

    private void handleDeletePatient() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("      DELETE PATIENT");
        System.out.println("=".repeat(30));

        // Step 1: Reuse search logic to get the patient
        Patient patient = handleSearchPatient(); // This must return Patient

        // Step 2: Confirm deletion (loop until valid Y/N)
        while (true) {
            try {
                System.out.print("\nAre you sure you want to delete this patient? (Y/N): ");
                char input = scanner.nextLine().charAt(0);

                // Validate Y/N using your method
                TryCatchThrowFromFile.validateYesOrNo(input);

                if (input == 'Y') {
                    Patient removedPatient = patientManager.removePatient(patient.getPatientIC());
                    if (removedPatient != null) {
                        System.out.println("\nPatient deleted successfully!");
                    } else {
                        System.out.println("\nFailed to delete patient.");
                    }
                } else { // 'N'
                    System.out.println("\nPatient deletion cancelled.");
                }
                break; // Exit loop after valid response

            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
    }

    private void handleDisplayAllPatients() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("        ALL PATIENTS");
        System.out.println("=".repeat(35));

        if (patientManager.isEmpty()) {
            System.out.println("\nNo patients in the system.");
            return;
        }

        System.out.println("Total Patients: " + patientManager.getTotalPatients());
        patientTableHeader();

        Patient[] patients = patientManager.getAllPatients();
        for (Patient patient : patients) {
            System.out.printf("| %-15s | %-20s | %-15s | %-5d | %-8s | %-40s |\n",
                patient.getPatientIC(),
                patient.getPatientName(),
                patient.getPatientPhoneNo(),
                patient.getPatientAge(),
                patient.getPatientGender(),
                patient.getPatientAddress()
            );
            System.out.println("-".repeat(122));
        }
    }

    public void patientTableHeader() {
        System.out.println("-".repeat(122));
        System.out.println(String.format("| %-15s | %-20s | %-15s | %-5s | %-8s | %-40s |", "Patient IC", "Patient Name", "Phone Number", "Age", "Gender", "Address"));
        System.out.println("-".repeat(122));
    }

    private void handlePatientStatistics() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("      PATIENT STATISTICS");
        System.out.println("=".repeat(35));
        
        int totalPatients = patientManager.getTotalPatients();
        System.out.println("Total Patients: " + totalPatients);
        
        if (totalPatients == 0) {
            System.out.println("No patients in the system.");
            return;
        }

        int choice;
        do {
            System.out.println("\nSelect Report Type:");
            System.out.println("1. Age Distribution Report");
            System.out.println("2. Gender Distribution Report");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");
            
            String input = scanner.nextLine().trim();
            try {
                TryCatchThrowFromFile.validateIntegerRange(input, 0, 2);
                choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        displayAgeDistribution();
                        break;
                    case 2:
                        displayGenderDistribution();
                        break;
                    case 0:
                        System.out.println("Returning to main menu...");
                        break;
                }
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
                choice = -1; // stay in loop
            }
        } while (choice != 0);
    }

    // Age Distribution
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

        int total = patients.length;

        System.out.println("\n" + "=".repeat(40));
        System.out.println("        AGE DISTRIBUTION REPORT");
        System.out.println("=".repeat(40));
        System.out.printf("%-15s %-8s %-20s\n", "Range", "Count", "Chart");
        System.out.println("-".repeat(40));

        String[] ranges = {"0 to 18", "19 to 35", "36 to 50", "51 to 65", "65+"};
        for (int i = 0; i < ageGroups.length; i++) {
            System.out.printf("%-15s %-8d %-20s\n", 
                ranges[i], ageGroups[i], "#".repeat(ageGroups[i]));
        }

        System.out.println("-".repeat(40));
        System.out.println("Total Patients: " + total);
    }

    // Gender Distribution
    private void displayGenderDistribution() {
        Patient[] patients = patientManager.getAllPatients();
        int maleCount = 0, femaleCount = 0;

        for (Patient p : patients) {
            if (Character.toUpperCase(p.getPatientGender()) == 'M') maleCount++;
            else if (Character.toUpperCase(p.getPatientGender()) == 'F') femaleCount++;
        }

        int total = maleCount + femaleCount;
        double malePercent = (total > 0) ? (maleCount * 100.0 / total) : 0;
        double femalePercent = (total > 0) ? (femaleCount * 100.0 / total) : 0;

        System.out.println("\n" + "=".repeat(40));
        System.out.println("      GENDER DISTRIBUTION REPORT");
        System.out.println("=".repeat(40));
        System.out.printf("%-8s %-8s %-12s %-20s\n", "Gender", "Count", "Percentage", "Chart");
        System.out.println("-".repeat(40));

        System.out.printf("%-8s %-8d %-12.2f %-20s\n", 
            "Male", maleCount, malePercent, "#".repeat((int)(malePercent / 2))); // scale: 2% = 1 bar
        System.out.printf("%-8s %-8d %-12.2f %-20s\n", 
            "Female", femaleCount, femalePercent, "#".repeat((int)(femalePercent / 2)));

        System.out.println("-".repeat(40));
        System.out.println("Total Patients: " + total);
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

        while (true) {
            try {
                System.out.print("\nAre you sure you want to clear ALL patients? This cannot be undone! (Y/N): ");
                String input = scanner.nextLine().trim().toUpperCase();

                if (input.isEmpty()) {
                    throw new InvalidInputException("Input cannot be empty.");
                }

                char confirmation = input.charAt(0);
                TryCatchThrowFromFile.validateYesOrNo(confirmation);

                if (confirmation == 'Y') {
                    patientManager.clearAllPatients();
                    System.out.println("\nAll patients have been cleared from the system.");
                } else {
                    System.out.println("\nClear operation cancelled.");
                }
                break; // Exit loop after valid response

            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
    }

    private void pauseForUser () {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}