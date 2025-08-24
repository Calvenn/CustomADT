
package Boundary;

import java.util.Scanner;
import Entity.Staff.Position;
import Control.StaffManager;
import Control.DoctorManager;
import exception.InvalidInputException;
import exception.TryCatchThrowFromFile;
import exception.ValidationUtility;
import java.time.LocalDateTime;
/**
 *
 * @author tanjixian
 */
public class StaffManagementUI {
        
    // Variables
    Scanner scanner = new Scanner(System.in);
    StaffManager staffManager;
    DoctorManager docManager;
    String choice = "1";
    
    // Constructor
    public StaffManagementUI(StaffManager staffManager, DoctorManager docManager){
        this.staffManager = staffManager;
        this.docManager = docManager;
    }
    
   //FUNCTIONS   
    // Add New Staff
    public void addStaffMenu(){
        // Variables
        Position position = null;  // ENUM FOR ADMIN, DOCTOR, NURSE
        String name;
        int age;
        String phoneNo;     // 011-222 4568
        String gender;      // Male - Female
        LocalDateTime dateJoined;   // 23 January 2023
        String password;
        String department; // Treatment / Cosult !doctor only
        
        while (true){
            printTitle("Staff Management: Add New Staff");

            // Get Staff Position
            while (true){
                System.out.println("\nStaff Position: 1. Admin");
                System.out.println("                  2. Doctor");
                System.out.println("                  3. Nurse");
                System.out.println("Select: ");
                scanner.nextLine();
                try{
                    TryCatchThrowFromFile.validateIntegerRange(choice, 1, 3);
                    break;
                } catch (InvalidInputException e){
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            
            // Get position of new staff
            switch(choice){
                case "1" -> position = Position.valueOf("ADMIN");
                case "2" -> position = Position.valueOf("DOCTOR");
                case "3" -> position = Position.valueOf("NURSE");
            }
            
            // Get User Input one-by-one
            System.out.println("\n\nStaff Position: " + position.name().toLowerCase());
            
            name = name();
            age = age();
            phoneNo = phoneNo();
            gender = gender();
            dateJoined = dateJoined();
            password = password();
            

            
            if(position.name().equals("DOCTOR")){
                // Get Department !!FOR DOCTOR ONLY
                department = department();
                
                if(docManager.addNewDoctor(name, age, phoneNo, gender, position, department, dateJoined, password)){
                    System.out.println("\nStaff Successfully Added");
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                }
            }
                // Add Staff
                if(staffManager.addNewStaff(name, age, phoneNo, gender, position, dateJoined, password)){
                    System.out.println("\nStaff Successfully Added");
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    break;
                }
        }   // End of Loop
    }
    
    // editDoctorUI
    
    // removeDoctorUI
    public void removeStaffMenu(){
        String id;
        String confirmation;
        printTitle("Staff Management: Remove Staff");
        System.out.println("\nStaff ID to delete (E.g. D0001)");
        id = id();
        
        while(true){
            System.out.println("Are you sure you want to remove " + staffManager.findStaff(id).getName() + " ?");
            System.out.print("(Y/N): ");
            confirmation = scanner.nextLine();
            try{
                TryCatchThrowFromFile.validateYesOrNo(confirmation.charAt(0));
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }
        }
        
        if(confirmation.charAt(0) == 'Y'){
            staffManager.removeStaff(id);
            System.out.println("\nStaff Removed Successfully.");
            if(docManager.isDoctor(staffManager.findStaff(id))){
                docManager.removeDoctor(id);
            }
        } else {
            System.out.println("\nRequest Rejected / Declined, Removal was unsuccessful.");
        }
        
        System.out.println("\nEnter to Continue...");
        scanner.nextLine();
        
    }
    
    // viewAllDoctorUI
    public void viewAllStaffMenu(){
        printTitle("Staff Management: View All Staff");
        staffManager.printStaff(staffManager.viewAllStaff());
        System.out.println("\nPress Enter to Continue...");
        scanner.nextLine();
    }
    
   //Helper Function
    // ID
    public String id(){
        String input;
        
        // Get ID
        while(true){
            System.out.print("Enter Staff ID: ");
            input = scanner.nextLine();
            try {
                TryCatchThrowFromFile.validateNotNull(input);
                staffManager.existStaff(input);
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }
        }
        
        return input;
    }
    
    // Name
    public String name(){
        String input;
        
        // Get Name
        while(true){
            System.out.print("Staff Name: ");
            input = scanner.nextLine();
            try {
                TryCatchThrowFromFile.validateNotNull(input);
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }
        }
        
        return input;
    }
      
    // Age
    public int age(){
        
        String input;
        
        // Get Age
        while(true){
                System.out.print("Staff Age: ");
                input = scanner.nextLine();
                try{
                    TryCatchThrowFromFile.validatePositiveInteger(input);
                    break;
                } catch (InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
        
        return Integer.parseInt(input);
    }   
    
    // PhoneNo
    public String phoneNo(){
        String input;
        
        // Get Phone Number
        while(true){
            System.out.print("Phone Number: ");
            input = scanner.nextLine();
            try{
                TryCatchThrowFromFile.validatePhone(input);
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }
        }
        
        return input;
    }
    
    // Gender
    public String gender(){
        String input = null;
        
        // Get Gender
        while(true){
            System.out.println("Gender - 1. Male");
            System.out.println("        2. Female");
            System.out.print("Select:  ");

            try{
                TryCatchThrowFromFile.validateIntegerRange(choice, 1, 2);
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }

            switch(choice){
                case "1" -> input = "Male";
                case "2" -> input = "Female";
            }

        }
        
        return input;
        
    }
    
    // DateJoined
    public LocalDateTime dateJoined(){
        String input;
        
        // Get Date Joined
        while (true){
            System.out.print("Date Joined (2025-08-23 24:00) : ");
            input = scanner.nextLine();
            try{
                TryCatchThrowFromFile.validateDateTime(input);
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }
        }
            
        return LocalDateTime.parse(input);
    }

    // Password
    public String password(){
        String input;
       
        // Get Password For Login
        while (true){
            System.out.print("Password: ");
            input = scanner.nextLine();
            try{
                TryCatchThrowFromFile.validateNotNull(input);
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }
        }
        
        return input;
        
    }
    
    // Department
    public String department(){
        String input = null;
        
        // Get Department
        while(true){
            System.out.println("Department - 1. Treatment");
            System.out.println("            2. Consult");
            System.out.print("Select: ");


            try{
                TryCatchThrowFromFile.validateIntegerRange(choice, 1, 2);
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }

            switch(choice){
                case "1" -> input = "Treatment";
                case "2" -> input = "Consult";
            }           
        }
        
        return input;
    }
    
    
   //Menu
    // Menu Title
    public void printTitle(String title){
        int center = (35 + title.length()) / 2;
        System.out.println("\n===================================");
        System.out.printf("%" + center + "s\n", title);
        System.out.println("===================================");
    }
   
    // Staff Menu
    public void staffMenu(){
        while (true){ // Repeat the step if the user input invalid choice
           
            try {
               TryCatchThrowFromFile.validateIntegerRange(choice, 1, 5);
           } catch (InvalidInputException e){
               ValidationUtility.printErrorWithSolution(e);
           }
           
            printTitle("Staff Management Module");
            System.out.println("1. Register a new Staff");
            System.out.println("2. Edit Information");
            System.out.println("3. Remove Staff");
            System.out.println("4. View All Staff");
            System.out.println("5. Exit");  
            System.out.println("===============================");
            
            System.out.println("Enter your choice: ");
            choice = scanner.nextLine();

            switch(choice){
                case "1" -> addStaffMenu();
                case "2" -> choice = "1"; //edit
                case "3" -> removeStaffMenu();
                case "4" -> viewAllStaffMenu();
                case "5" -> { // exit
                    System.out.println("\nThank you for using Staff Management System");
                    return;
                }
                default -> System.out.printf("\nInvalid choice entered. Please choose again.");
            }
            
        } // End of While Loop
    }
     
}
