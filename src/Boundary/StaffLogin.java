package Boundary;

import java.util.Scanner;
import Control.QueueManager;
import Control.StaffManager;
import Entity.Staff;
import exception.*;


/**
 *
 * @author tanjixian
 */
public class StaffLogin {

    /**
     * Creates new form NewJFrame
     */
    
    // Variable
    int choice = -1;     // Used for menu navigation
    private final Scanner scanner;  
    private final QueueManager queueManager;
    private final StaffManager staffManager;
    private final ConsultationUI consultUI;
    private final TreatmentUI treatmentUI;
    private final TreatmentApptUI treatmentApptUI;
    private final PharmacyUI pharUI;
    private final PatientManagementUI patientUI;
    private final StaffManagementUI staffUI;
    private final PaymentUI payUI;
    
    
    public String login;        // Used to identify the position type logging in
    public String currID;       // Temporal field for storing ID
    
    // Constructor
    public StaffLogin(QueueManager queueManager, StaffManager staffManager, ConsultationUI consultUI, TreatmentUI treatmentUI, TreatmentApptUI treatmentApptUI, PharmacyUI pharUI, PatientManagementUI patientUI, StaffManagementUI staffUI, PaymentUI payUI) {
        this.queueManager = queueManager;
        this.staffManager = staffManager;
        this.consultUI = consultUI;
        this.treatmentUI = treatmentUI;
        this.treatmentApptUI = treatmentApptUI;
        this.pharUI = pharUI;
        this.patientUI = patientUI;
        this.staffUI = staffUI;
        this.payUI = payUI;
        this.scanner = new Scanner(System.in);
    }
                                           

    private boolean checkLogin(String id, String password) {
        String position = getPosition(id).trim().toUpperCase();

        if (!idFormat(id)) {
            System.out.println("User ID must be in the correct format e.g. D0001.");
            return false;
        }

        currID = id;

        // If staff not found
        if (staffManager.findStaff(id) == null) {
            System.out.println("ID not found!");
            return false;
        }

        // Verify credentials
        if (id.equalsIgnoreCase(staffManager.findStaff(id).getID()) && password.equalsIgnoreCase(staffManager.findStaff(id).getPassword())) {
            System.out.println("Login Successful!");
            login = position;
            return true;
        } else {
            System.out.println("Invalid Password.");
            return false;
        }
    }
    
    public void login(){
        String id;
        String password;
        
        System.out.println("\n===================================");
        System.out.printf("               Login");
        System.out.println("\n===================================");
        

        // USER ID
        while(true){
            System.out.print("User ID: ");
            id = scanner.nextLine();
            try {
                TryCatchThrowFromFile.validateNotNull(id);
                if(!idFormat(id)) throw new InvalidInputException("ID format is incorrect!");
                if(staffManager.findStaff(id) == null) throw new InvalidInputException("ID not found!");
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }          
        }
        
        // PASSWORD
        while (true){
            System.out.print("Password: ");
            password = scanner.nextLine();
            try{
                TryCatchThrowFromFile.validateNotNull(password);
                if (!password.equalsIgnoreCase(staffManager.findStaff(id).getPassword())) throw new InvalidInputException("Invalid Password!");
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }
        }
        
        if(!checkLogin(id, password)) return;
        staffLogin(queueManager, consultUI, treatmentUI, pharUI, patientUI, staffUI, payUI);
    }

   // HELPER FUNCTION
    // Login Menu Navigator
    public void staffLogin(QueueManager queueManager,ConsultationUI consultUI, TreatmentUI treatmentUI, PharmacyUI pharUI, PatientManagementUI patientUI, StaffManagementUI staffUI, PaymentUI payUI){
        switch(login){
            case "ADMIN" -> adminMenu(currID, consultUI, treatmentUI, pharUI, patientUI, staffUI, queueManager, payUI);
            case "DOCTOR" -> doctorMenu(currID, consultUI, treatmentUI, pharUI, staffUI, queueManager);
            case "NURSE" -> nurseMenu(currID, consultUI, pharUI, patientUI, staffUI, queueManager);
        }           
    }
    
    // Check Correct ID format using REGEX
    private boolean idFormat(String id){
        return id.matches("^[ADN]\\d{4}$");
        
    }
    
    // Get the position of a id
    private String getPosition(String id){
        return switch(id.charAt(0)){
            case 'A' -> "ADMIN";
            case 'N' -> "NURSE";
            case 'D' -> "DOCTOR";     
            default -> "UNKNOWN";
        };
    }
   
   // MENU
    // Menu Title
    public void printTitle(Staff staff){
        String title = "Clinic Management System";
        int center = (35 + title.length()) / 2;
        System.out.println("\nWelcome (" + staff.getPosition() + ") " + staff.getName());
        System.out.println("\n===================================");
        System.out.printf("%" + center + "s\n", title);
        System.out.println("===================================");
    }
    
    // Menu for ADMIN
    public void adminMenu(String userID, ConsultationUI consultUI, TreatmentUI treatmentUI, PharmacyUI pharUI, PatientManagementUI patientUI, StaffManagementUI staffUI, QueueManager queueManager, PaymentUI payUI){
        while (true){ // Repeat the step if the user input invalid choice
            queueManager.loadVisit();
            printTitle(staffManager.findStaff(userID));
            System.out.println("1. Consultation System"); //read only
            System.out.println("2. Treatment System"); //read only??
            System.out.println("3. Treatment Appointment System");
            System.out.println("4. Pharmacy System (Read-Only)");
            System.out.println("5. Patient Registration System");
            System.out.println("6. Payment System");
            System.out.println("7. Staff Management System");
            System.out.println("0. Log Out");     
            System.out.println("===============================");
            
            choice = ValidationHelper.inputValidatedChoice(0, 7, "your choice");

            switch(choice){
                case 1 -> consultUI.consultMainMenuRead(); // Consultation System
                case 2 -> treatmentUI.treatmentMenu(); // Treatment System
                case 3 -> treatmentApptUI.treatmentApptMenu(userID); // treatment appointment system
                case 4 -> pharUI.pharmacyMenuRead(); // Pharmacy System (READ_ONLY)
                case 5 -> patientUI.patientMenu();
                case 6 -> payUI.paymentMenu();
                case 7 -> staffUI.staffMenu();  // Staff Management System
                case 0 -> { // Exit
                    System.out.println("\nThank you for using Doctor Management System");
                    login();
                }
                default -> System.out.printf("\nInvalid choice entered. Please choose again.");
            }
        }
    }
    
    // Menu for DOCTOR
    public void doctorMenu(String userID, ConsultationUI consultUI, TreatmentUI treatmentUI, PharmacyUI pharUI, StaffManagementUI staffUI, QueueManager queueManager){
        while (true){    
            queueManager.loadVisit();
            printTitle(staffManager.findStaff(userID));
            System.out.println("1. Consultation System");
            System.out.println("2. Treatment System");
            System.out.println("3. Treatment Appointment System");
            System.out.println("4. Pharmacy System (Read-Only)");
            System.out.println("5. Staff Management System");
            System.out.println("0. Log Out");     
            System.out.println("===============================");
            
            choice = ValidationHelper.inputValidatedChoice(0, 5, "your choice");

            switch(choice){
                case 1 -> consultUI.consultMainMenu(staffManager.findStaff(userID)); // Consultation System
                case 2 -> treatmentUI.treatmentMenu(); // Treatment System
                case 3 -> treatmentApptUI.treatmentApptMenu(userID); //treatment appointment system
                case 4 -> pharUI.pharmacyMenuRead(); // Pharmacy System (READ_ONLY)
                case 5 -> staffUI.staffMenu(); 
                case 0 -> { // Exit
                    System.out.println("\nThank you for using Doctor Management System");
                    login();
                }
                default -> System.out.printf("\nInvalid choice entered. Please choose again.");
            }
        } 
    }
    
    // Menu for NURSE
        public void nurseMenu(String userID, ConsultationUI consultUI, PharmacyUI pharUI, PatientManagementUI patientUI, StaffManagementUI staffUI, QueueManager queueManager){
        while (true){ // Repeat the step if the user input invalid choice
            queueManager.loadVisit();
            printTitle(staffManager.findStaff(userID));
            System.out.println("1. Consultation System");
            System.out.println("2. Pharmacy Control System");
            System.out.println("3. Patient Registration System");
            System.out.println("0. Exit");   
            System.out.println("===============================");
            
            choice = ValidationHelper.inputValidatedChoice(0, 2, "your choice");

            switch(choice){
                case 1 -> consultUI.consultMainMenuRead(); // Consultation System*/
                case 2 -> pharUI.pharmacyMenu(); // Pharmacy System 
                case 3 -> patientUI.patientMenu();
                case 0 -> { // Exit
                    System.out.println("\nThank you for using Doctor Management System");
                    login();
                }
                default -> System.out.printf("\nInvalid choice entered. Please choose again.");
            }
        } 
    }                
}