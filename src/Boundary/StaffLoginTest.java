package Boundary;

import java.util.Scanner;
import Control.DoctorManager;
import Control.QueueManager;
import Control.StaffManager;
import Entity.MedRecord;
import Entity.TreatmentAppointment;
import adt.Queue;
import exception.*;


/**
 *
 * @author tanjixian
 */
public class StaffLoginTest {

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
    private final PharmacyUI pharUI;
    private final PatientManagementUI patientUI;
    private final StaffManagementUI staffUI;
    private final PaymentUI payUI;
    
    
    public String login;        // Used to identify the position type logging in
    public String currID;       // Temporal field for storing ID
    
    // Constructor
    public StaffLoginTest(QueueManager queueManager, StaffManager staffManager, ConsultationUI consultUI, TreatmentUI treatmentUI, PharmacyUI pharUI, PatientManagementUI patientUI, StaffManagementUI staffUI, PaymentUI payUI) {
        this.queueManager = queueManager;
        this.staffManager = staffManager;
        this.consultUI = consultUI;
        this.treatmentUI = treatmentUI;
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
        
        System.out.print("User ID: ");
        id = scanner.nextLine();
        
        System.out.print("Password: ");
        password = scanner.nextLine();
        
        if(!checkLogin(id, password)) return;
        System.out.print(login);
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
    public void printTitle(String position, String name){
        String title = "Clinic Management System";
        int center = (35 + title.length()) / 2;
        System.out.println("WELCOME (" + position + ") " + name);
        System.out.println("\n===================================");
        System.out.printf("%" + center + "s\n", title);
        System.out.println("===================================");
    }
    
    // Menu for ADMIN
    public void adminMenu(String userID, ConsultationUI consultUI, TreatmentUI treatmentUI, PharmacyUI pharUI, PatientManagementUI patientUI, StaffManagementUI staffUI, QueueManager queueManager, PaymentUI payUI){
        while (true){ // Repeat the step if the user input invalid choice
            queueManager.loadVisit();
            printTitle("DOCTOR", staffManager.findStaff(userID).getName());
            System.out.println("1. Consultation System"); //read only
            System.out.println("2. Treatment System"); //read only??
            System.out.println("3. Pharmacy System (Read-Only)");
            System.out.println("4. Patient Registration System");
            System.out.println("5. Payment System");
            System.out.println("6. Staff Management System");
            System.out.println("0. Log Out");   
            System.out.println("===============================");
            
            choice = ValidationHelper.inputValidatedChoice(0, 5, "your choice");

            switch(choice){
                case 1 -> {                 
                    consultUI.consultMainMenu(staffManager.findStaff(userID)); }// Consultation System
                case 2 -> treatmentUI.treatmentMenu(); // Treatment System
                case 3 -> pharUI.pharmacyMenuRead(); // Pharmacy System (READ_ONLY)
                case 4 -> patientUI.patientMenu();
                case 5 -> payUI.paymentMenu();
                case 6 -> staffUI.staffMenu(); // Staff Management System
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
            printTitle("Doctor: ", staffManager.findStaff(userID).getName());
            System.out.println("1. Consultation System");
            System.out.println("2. Treatment System");
            System.out.println("3. Pharmacy System (Read-Only)");
            System.out.println("4. Staff Management System");
            System.out.println("0. Log Out");   
            System.out.println("===============================");
            
            choice = ValidationHelper.inputValidatedChoice(0, 4, "your choice");

            switch(choice){
                case 1 -> {                 
                    consultUI.consultMainMenu(staffManager.findStaff(userID)); }// Consultation System
                case 2 -> treatmentUI.treatmentMenu(); // Treatment System
                case 3 -> pharUI.pharmacyMenuRead(); // Pharmacy System (READ_ONLY)
                case 4 -> staffUI.staffMenu(); // Staff Management System
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
            printTitle("Nurse: ", staffManager.findStaff(userID).getName());
            //System.out.println("1. Consultation System");
            System.out.println("1. Pharmacy Control System");
            System.out.println("2. Patient Registration System");
            System.out.println("3. Staff Management System");
            System.out.println("0. Exit");   
            System.out.println("===============================");
            
            choice = ValidationHelper.inputValidatedChoice(0, 4, "your choice");

            switch(choice){
               /* case 1 -> {                 
                    consultUI.consultMainMenu(staffManager.findStaff(userID)); }// Consultation System*/
                case 1 -> pharUI.pharmacyMenu(); // Pharmacy System 
                case 2 -> patientUI.patientMenu();
                case 3 -> staffUI.staffMenu(); // Staff Management System
                case 0 -> { // Exit
                    System.out.println("\nThank you for using Doctor Management System");
                    login();
                }
                default -> System.out.printf("\nInvalid choice entered. Please choose again.");
            }
        } 
    }                
}