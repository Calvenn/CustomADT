package Boundary;
import Control.TreatmentManager;
import java.util.Scanner; 
import java.time.Duration; 

/**
 *
 * @author MeganYeohTzeXuan
 */

public class TreatmentUI {
    private TreatmentManager treatmentManager; 
    private Scanner scanner;
    
    public TreatmentUI(TreatmentManager treatmentManager) {
        this.treatmentManager = treatmentManager; 
        scanner = new Scanner(System.in);
    }
    
    public void treatmentMenu() {
        while(true) {
            System.out.println("---Treatment Menu---");
            System.out.println("1. Show All Treatments"); 
            System.out.println("2. Add New Treatment"); 
            System.out.println("3. Update A Treatment");
            System.out.println("4. Remove A Treatment");
            System.out.println("5. Back"); 

            System.out.print("Choose > "); 
            int input = scanner.nextInt(); 
            scanner.nextLine(); 
            
            switch(input) {
                case 1 -> showTreatmentsUI();
                case 2 -> addTreatmentUI();
                case 3 -> modifyTreatmentUI();
                case 4 -> deleteTreatmentUI();
                case 5 -> {
                    System.out.println("Returning to main menu..."); 
                    return; 
                }
                default -> System.out.println("Invalid"); //change to throw exception afterwards 
            }
        }
    }
    
    public void showTreatmentsUI() {
        treatmentManager.displayAllTreatments(); 
        System.out.println("Enter to return..."); 
        scanner.next(); 
    }
    
    public void addTreatmentUI() {
        System.out.println("---- Add Treatment---");
        System.out.print("Treatment Name: "); 
        String name = scanner.nextLine();
        treatmentManager.treatmentExist(name.trim()); 
        System.out.print("Description: ");
        String description = scanner.nextLine(); 
        System.out.print("Duration (minutes): ");
        Duration duration = Duration.ofMinutes(scanner.nextInt()); 
        scanner.nextLine();
        treatmentManager.newTreatment(name, description, duration); 
    }
    
    public void modifyTreatmentUI() {
        while(true) {
            System.out.println("--- Modify Treatment ---\n"); 
            System.out.println("Search by:"); 
            System.out.println("1. ID"); 
            System.out.println("2. Name"); 
            System.out.println("3. Back");
            System.out.print("Select > "); 
            int select = scanner.nextInt(); 
            String input; 
            switch(select) {
                case 1 -> System.out.print("Enter ID or x to return > ");
                case 2 -> System.out.print("Enter Name of x to return > "); 
            }
            input = scanner.nextLine(); 
            if(input.toLowerCase().trim().equals("x")) {
                continue; 
            } else {
                switch(select) {
                    case 1 -> System.out.println(treatmentManager.findTreatmentID(input)); 
                    case 2 -> System.out.println(treatmentManager.findTreatmentName(input)); 
                }
            }
            System.out.println("Select field to modify: "); 
            System.out.println("1. Name"); 
            System.out.println("2. Description"); 
            System.out.println("3. Duration"); 
        }
        
    }
    
    public void deleteTreatmentUI() {
        
    }
}
