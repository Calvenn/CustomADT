package Boundary;
import Entity.Treatment; 
import Control.TreatmentManager;
import exception.InvalidInputException;
import java.util.Scanner; 
import java.time.Duration; 
import exception.TryCatchThrowFromFile;
import exception.ValidationUtility;

/**
 *
 * @author MeganYeohTzeXuan
 */

public class TreatmentUI {
    private final TreatmentManager treatmentManager; 
    private Scanner scanner;
    
    public TreatmentUI(TreatmentManager treatmentManager) {
        this.treatmentManager = treatmentManager; 
        scanner = new Scanner(System.in);
    }
    
    private void printTitle(String text) {
        int center = (35 + text.length()) / 2; 
        System.out.println("==================================="); 
        System.out.printf("%" + center + "s\n", text); 
        System.out.println("==================================="); 
    }
    
    public void treatmentMenu() {
        while(true) {
            String input; 
            printTitle("Treatment Menu");
            System.out.println("1. Show All Treatments"); 
            System.out.println("2. Add New Treatment"); 
            System.out.println("3. Update A Treatment");
            System.out.println("4. Remove A Treatment");
            System.out.println("5. Back"); 
            
            while (true) {
                System.out.print("Choose > "); 
                input = scanner.nextLine(); 
                try {
                    TryCatchThrowFromFile.validateIntegerRange(input, 1, 5);
                    break; 
                } catch(InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            
            switch(Integer.parseInt(input)) {
                case 1 -> showTreatmentsUI();
                case 2 -> addTreatmentUI();
                case 3 -> modifyTreatmentUI();
                case 4 -> deleteTreatmentUI();
                case 5 -> {
                    System.out.println("Returning to main menu..."); 
                    return; 
                }
            }
        }
    }
    
    public void showTreatmentsUI() {
        printTitle("Treatments List");
        treatmentManager.displayAllTreatments(); 
        System.out.println("Enter to return..."); 
        scanner.nextLine(); 
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
        printTitle("Add Treatment");
        
        String name = inputName(); 
        String description = inputDescription(); 
        Duration duration = inputDuration(); 

        treatmentManager.newTreatment(name, description, duration); 
    }
    
    public Treatment searchTreatmentUI() {
        String input;  
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
            return treatmentManager.findTreatmentName(input);
        }
        
    }
    
    public void modifyTreatmentUI() {
        while(true) {
            printTitle("Modify Treatment"); 
            Treatment treatment = searchTreatmentUI(); 
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
        
    }
}
