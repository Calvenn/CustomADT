package Boundary;
import Control.TreatmentApptManager;
import adt.List;
import exception.InvalidInputException;
import exception.TryCatchThrowFromFile;
import exception.ValidationUtility;
import java.time.format.DateTimeFormatter;
import java.util.Scanner; 

/**
 *
 * @author MeganYeohTzeXuan
 */
public class TreatmentApptUI {
    private final TreatmentApptManager treatmentApptManager;
    private final Scanner scanner; 
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"); 
    
    public TreatmentApptUI(TreatmentApptManager treatmentApptManager) {
        this.treatmentApptManager = treatmentApptManager; 
        this.scanner = new Scanner(System.in); 
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
            printTitle("Treatment Appointment Menu", 35);
            System.out.println("1. Show All Incoming Appointments"); 
            System.out.println("2. Search An Appointment"); 
            System.out.println("3. Show Treatment Appointment History"); 
            System.out.println("4. Add New Treatment Appointment");
            System.out.println("5. Update A Treatment");
            System.out.println("6. Cancel A Treatment");
            System.out.println("7. Generate Reports");
            System.out.println("8. Back"); 
            
            while (true) {
                System.out.print("Choose > "); 
                input = scanner.nextLine(); 
                try {
                    TryCatchThrowFromFile.validateIntegerRange(input, 1, 8);
                    break; 
                } catch(InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            
            switch(Integer.parseInt(input)) {
                case 1 -> showIncomingUI();
                case 2 -> searchAppointmentUI(); 
                case 3 -> showHistoryUI();
                case 4 -> addNewAppointmentUI();
                case 5 -> updateAppointmentUI();
                case 6 -> cancelAppointmentUI();
                case 7 -> generateReportUI(); 
                case 8 -> {
                    System.out.println("Returning to main menu..."); 
                    return; 
                }
            }
        }
    }
    
    public void showIncomingUI() {
        printTitle("Incoming Treatment Appointments", 35);
        treatmentApptManager.upcomingAppointments();
        System.out.println("Enter to return..."); 
        scanner.nextLine(); 
    }
    
    public void searchAppointmentUI() {}
    
    public void showHistoryUI() {}
    
    public void addNewAppointmentUI() {}
    
    public void updateAppointmentUI() {}
    
    public void cancelAppointmentUI() {}
    
    public void generateReportUI() {
        printTitle("Treatment Apointment Reports", 35); 
        String input; 
        while(true) {
            System.out.println("Select report to generate: "); 
            System.out.println("1. "); 
            System.out.println("2. "); 
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
            
//            switch(Integer.parseInt(input)) {
//                case 1 -> 
//                case 2 -> 
//                case 3 -> {
//                    System.out.println("Returning to treatment menu..."); 
//                    return; 
//                }
//            }
            
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
    
    public List<String> suggestedTrt(String symptoms) {
        List<String> trtType = new List<>();
        String input = symptoms.toLowerCase();

        if (input.contains("fever") || input.contains("fatigue")) {
            trtType.add("Blood test");
            trtType.add("Urine test");
        } else if (input.contains("cough") || input.contains("shortness of breath")) {
            trtType.add("X-ray");
            trtType.add("Nebuliser");
        } else if (input.contains("allergy") || input.contains("rash")) {
            trtType.add("Allergy test");
            trtType.add("Cryotherapy");
        } else if (input.contains("injury") || input.contains("wound")) {
            trtType.add("Wound care");
            trtType.add("Physical therapy");
        } else if (input.contains("vision problem") || input.contains("blurred vision")) {
            trtType.add("Eye Examination");
        } else if (input.contains("dehydration")) {
            trtType.add("IV Fluid therapy");
        } else if (input.contains("pregnancy")) {
            trtType.add("Ultrasound");
        } else if (input.contains("vaccination") || input.contains("flu prevention")) {
            trtType.add("Vaccination");
        } else {
            trtType.add("General checkup");
        }

        return trtType;
    }
    
}