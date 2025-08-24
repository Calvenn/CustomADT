package Boundary;
import Entity.Treatment; 
import Control.TreatmentManager;
import exception.InvalidInputException;
import adt.Heap; 
import adt.List; 
import java.util.Scanner; 
import java.time.Duration; 
import exception.TryCatchThrowFromFile;
import exception.ValidationUtility;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; 

/**
 *
 * @author MeganYeohTzeXuan
 */

public class TreatmentUI {
    private final TreatmentManager treatmentManager; 
    private Scanner scanner;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"); 
    
    public TreatmentUI(TreatmentManager treatmentManager) {
        this.treatmentManager = treatmentManager; 
        scanner = new Scanner(System.in);
    }
    
    private void printTitle(String text, int length) {
        int center = (length + text.length()) / 2; 
        System.out.println("=".repeat(length)); 
        System.out.printf("%" + center + "s\n", text); 
        System.out.println("=".repeat(length)); 
    }
    
    public void treatmentMenu() {
        while(true) {
            String input; 
            printTitle("Treatment Menu", 35);
            System.out.println("1. Show All Treatments"); 
            System.out.println("2. Search A Treatment"); 
            System.out.println("3. Add New Treatment"); 
            System.out.println("4. Update A Treatment");
            System.out.println("5. Remove A Treatment");
            System.out.println("6. Generate Reports");
            System.out.println("7. Back"); 
            
            while (true) {
                System.out.print("Choose > "); 
                input = scanner.nextLine(); 
                try {
                    TryCatchThrowFromFile.validateIntegerRange(input, 1, 7);
                    break; 
                } catch(InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            
            switch(Integer.parseInt(input)) {
                case 1 -> showTreatmentsUI();
                case 2 -> searchTreatmentUI(); 
                case 3 -> addTreatmentUI();
                case 4 -> modifyTreatmentUI();
                case 5 -> deleteTreatmentUI();
                case 6 -> generateReportUI(); 
                case 7 -> {
                    System.out.println("Returning to main menu..."); 
                    return; 
                }
            }
        }
    }
    
    public void showTreatmentsUI() {
        printTitle("Treatments List", 35);
        treatmentManager.displayAllTreatments(); 
        System.out.println("Enter to return..."); 
        scanner.nextLine(); 
    }
    
    public void searchTreatmentUI() {  
        String input; 
        while (true) {
            printTitle("Search Treatments", 35); 
            Treatment treatment = searchForTreatment(); 
            if(treatment == null) {
                return; 
            }
            System.out.println("\nFound Treatment!\n"); 
            System.out.println(treatment); 
            
            //ask if want to search another
            while (true) {
                System.out.print("Search another treatment? (y/n): "); 
                input = scanner.nextLine(); 
                try {
                    TryCatchThrowFromFile.validateYesOrNo(input.charAt(0));
                    break;
                } catch (InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            if(input.charAt(0) == 'n') {
                return; 
            }
        }
    }
    
    private String inputName() {
        String name; 
        while (true) {
            System.out.print("Treatment Name: "); 
            name = scanner.nextLine().trim();

            try {
                TryCatchThrowFromFile.validateNotNull(name);
                if(treatmentManager.treatmentExist(name)) {
                    throw new InvalidInputException("Treatment " + name + " already exist."); 
                }
                break; 
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
        return name; 
    }
    
    private String inputDescription() {
        System.out.print("Description: ");
        String description = scanner.nextLine(); 
        return description; 
    }
    
    private Duration inputDuration() {
        String duration; 
        while(true) {
            System.out.print("Duration (minutes): ");
            duration = scanner.nextLine();

            try {
                TryCatchThrowFromFile.validatePositiveInteger(duration);
                break; 
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e); 
            }
        }
        return Duration.ofMinutes(Integer.parseInt(duration)); 
    }
    
    public void addTreatmentUI() {
        printTitle("Add Treatment", 35);
        
        String name = inputName(); 
        String description = inputDescription(); 
        Duration duration = inputDuration(); 

        treatmentManager.newTreatment(name, description, duration); 
    }
    
    public Treatment searchForTreatment() {
        String input;  
        Treatment treatment;
        while(true) {
            while (true) {
                System.out.print("Enter Treatment Name to search (x to Back): "); 
                input = scanner.nextLine();
                try {
                    TryCatchThrowFromFile.validateNotNull(input);
                    break;
                } catch (InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            if(input.matches("x") || input.matches("X")) {
                return null;
            } else {
                treatment = treatmentManager.findTreatmentName(input);
            }
            if(treatment == null) {
                System.out.println("Treatment is not found."); 
                while (true) {
                    System.out.print("Try again? (y/n): "); 
                    input = scanner.nextLine(); 
                    try {
                        TryCatchThrowFromFile.validateYesOrNo(input.charAt(0));
                        break;
                    } catch (InvalidInputException e) {
                        ValidationUtility.printErrorWithSolution(e);
                    }
                }
                if(input.charAt(0) == 'n') {
                    return null; 
                }
            } else {
                return treatment;
            }
        }
        
    }
    
    public void modifyTreatmentUI() {
        while(true) {
            printTitle("Modify Treatment", 35); 
            Treatment treatment = searchForTreatment(); 
            if(treatment == null) {
                return; 
            }
            
            System.out.println(treatment); 
            
            String input; 
            
            System.out.println("Select field to modify: "); 
            System.out.println("1. Description"); 
            System.out.println("2. Duration"); 
            System.out.println("3. Back"); 
            
            while (true) {
                System.out.print("Choose > "); 
                input = scanner.nextLine(); 
                try {
                    TryCatchThrowFromFile.validateIntegerRange(input, 1, 3);
                    break; 
                } catch(InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            
            try {
                switch(Integer.parseInt(input)) {
                    case 1 -> {
                        if(!treatmentManager.changeDescription(treatment, inputDescription())) {
                            throw new InvalidInputException("No changes made due to same data.");
                        }
                        break; 
                    }
                    case 2 -> {
                        if(!treatmentManager.changeDuration(treatment, inputDuration())) {
                            throw new InvalidInputException("No changes made due to same data.");
                        }
                        break; 
                    }
                    case 3 -> {
                        break; 
                    }
                }
            } catch(InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e); 
            } 
            
            System.out.println("After changes: "); 
            System.out.println(treatment); 
            
            char another; 
            while (true) {
                System.out.print("Modify another treatment? (y/n): "); 
                another = scanner.nextLine().charAt(0); 
                try {
                    TryCatchThrowFromFile.validateYesOrNo(another);
                    break; 
                } catch(InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            
            if(Character.toLowerCase(another) == 'n') {
                System.out.println("Returning to treatment menu...\n"); 
                return; 
            }
        }
        
    }
    
    public void deleteTreatmentUI() {
        char input; 
        while(true) {
            printTitle("Delete Treatment", 35);
            Treatment treatment = searchForTreatment(); 
            if(treatment == null) {
                return; 
            }
            
            System.out.println(treatment);
            while(true) {
                System.out.print("Confirm delete? (y/n): "); 
                input = scanner.nextLine().charAt(0); 
                try {
                    TryCatchThrowFromFile.validateYesOrNo(input);
                    break; 
                } catch(InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            System.out.println(); 
            
            if(Character.toLowerCase(input) == 'y') {
                if(!treatmentManager.deleteTreatment(treatment)) {
                    System.out.println("An error occured where the delete failed. Please try again.");
                } else {
                    System.out.println("Delete success!"); 
                }
            }
            
            while(true) {
                System.out.print("Delete another record? (y/n): "); 
                input = scanner.nextLine().charAt(0); 
                try {
                    TryCatchThrowFromFile.validateYesOrNo(input);
                    break; 
                } catch(InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            System.out.println(); 

            if(Character.toLowerCase(input) == 'n') {
                System.out.println("Returning to treatment menu...\n"); 
                return; 
            }
        }
        
    }
    
    public void generateReportUI() {
        printTitle("Treatment Reports", 35); 
        String input; 
        while(true) {
            System.out.println("Select report to generate: "); 
            System.out.println("1. Treatment frequency report"); 
            System.out.println("2. Time allocation report"); 
            System.out.println("3. Back"); 
            
            while (true) {
                System.out.print("Choose > "); 
                input = scanner.nextLine(); 
                try {
                    TryCatchThrowFromFile.validateIntegerRange(input, 1, 3);
                    break; 
                } catch(InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            
            switch(Integer.parseInt(input)) {
                case 1 -> treatmentFrequencyReportUI();
                case 2 -> timeAllocationReportUI(); 
                case 3 -> {
                    System.out.println("Returning to treatment menu..."); 
                    return; 
                }
            }
            
            printTitle("End of Report", 100); 
            while(true) {
                System.out.print("Generate another report? (y/n): ");
                input = scanner.nextLine(); 
                try {
                    TryCatchThrowFromFile.validateYesOrNo(input.charAt(0));
                    break; 
                } catch (InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e); 
                }
            }
            if(input.charAt(0) == 'n') return; 
        }
    }
    
    private void treatmentFrequencyReportUI() {
        generateReportHeader("Treatment Frequency Report");
        Heap<Treatment> treatmentHeap = treatmentManager.getFrequencyReport(); 
        
        if(treatmentHeap == null) {
            System.out.println("No data is found."); 
            return; 
        }
        
        int heapSize = treatmentHeap.size(); 

        System.out.println("_".repeat(100)); 
        System.out.printf("%-13s | %-30s | %-10s \n", "Treatment ID", "Treatment Name", "Frequency");
        System.out.println("-".repeat(100)); 
        
        for(int i = 0; i < heapSize; i++) {
            Treatment treatment = treatmentHeap.extractRoot();
            System.out.printf("%-13s | %-30s | %-10d \n", treatment.getTreatmentId(), treatment.getName(), treatment.getFrequency());
        }
        System.out.println("-".repeat(100)); 
        
        System.out.println("Total Treatments: " + treatmentManager.totalTreatments() + "\n"); 
    }
    
    private void timeAllocationReportUI() {
        generateReportHeader("Time Allocation Report");
        List<Treatment> treatmentList = treatmentManager.getTimeAllocationReport(); 
        
        if(treatmentList == null) {
            System.out.println("No data is found."); 
            return; 
        }
        
        int listSize = treatmentList.size(); 
        
        System.out.println("_".repeat(100)); 
        System.out.printf("%-13s | %-30s | %-10s | %-10s | %-15s \n", "Treatment ID", "Treatment Name", "Frequency", "Duration", "Time Allocated");
        System.out.println("-".repeat(100)); 
        
        Duration totalTime = Duration.ZERO; 
        
        for(int i = 1; i <= listSize; i++) {
            Treatment treatment = treatmentList.remove(1);
            System.out.printf("%-13s | %-30s | %-10d | %-10s | %-15s \n", treatment.getTreatmentId(), treatment.getName(), treatment.getFrequency(), treatment.getDuration().toMinutes() + " minutes", treatment.getTimeAllocation().toMinutes() + " minutes");
            totalTime = totalTime.plus(treatment.getTimeAllocation());
        }
        
        System.out.println("-".repeat(100)); 
        System.out.println("Total Treatments: " + treatmentManager.totalTreatments()); 
        System.out.println("Total Time Allocated on Treatment: " + totalTime.toMinutes() + " minutes\n");
    }
    
    private void generateReportHeader(String header) {
        System.out.println("-".repeat(100)); 
        printTitle(header, 100); 
        System.out.println("Generated at: " + LocalDateTime.now().format(dateFormat)); 
    }
    
}