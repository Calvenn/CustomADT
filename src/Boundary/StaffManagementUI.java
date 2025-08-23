
package Boundary;

import Control.DoctorManager;
import Control.StaffManager;
import data.CSVLoader;
import java.util.Scanner;
import Entity.Staff.Position;
import Entity.Staff;
import java.time.LocalDateTime;
/**
 *
 * @author tanjixian
 */
public class StaffManagementUI {
    DoctorManager docManager;
    StaffManager staffManager;
    
    // Variables
    int choice = 0;     // Used for menu navigation
    Scanner scanner  = new Scanner(System.in);
    
    // Login Menu
    public String staffLogin(){
        return "hi";
    }

    
    // Menu for ADMIN
    public void adminMenu(){
        while (choice < 1 && choice > 5){ // Repeat the step if the user input invalid choice
            System.out.println("===============================");
            System.out.println("    Staff Management System    ");
            System.out.println("===============================");
            System.out.println("1. Register a new Staff");
            System.out.println("2. Edit Information");
            System.out.println("3. Remove Staff");
            System.out.println("4. View All Staff");
            System.out.println("5. Exit");  
            System.out.println("===============================");
            
            System.out.println("Enter your choice: ");
            choice = scanner.nextInt();

            switch(choice){
                case 1 -> choice = 1; //register
                case 2 -> choice = 1; //edit
                case 3 -> choice = 1; //remove
                case 4 -> choice = 1; //view all
                case 5 -> { // exit
                    System.out.println("\nThank you for using Doctor Management System");
                    return;
                }
                default -> System.out.printf("\nInvalid choice entered. Please choose again.");
            }
            
        } // End of While Loop
    }
    // Menu for DOCTOR
    public void doctorMenu(){
        while (choice < 1 && choice > 5){ // Repeat the step if the user input invalid choice
            System.out.println("===============================");
            System.out.println("    Staff Management System    ");
            System.out.println("===============================");
            System.out.println("1. Register a new Staff");
            System.out.println("2. Edit Information");
            System.out.println("3. Remove Staff");
            System.out.println("4. View All Staff");
            System.out.println("5. Exit");  
            System.out.println("===============================");
            
            System.out.println("Enter your choice: ");
            choice = scanner.nextInt();

            switch(choice){
                case 1 -> choice = 1; //register
                case 2 -> choice = 1; //edit
                case 3 -> choice = 1; //remove
                case 4 -> choice = 1; //view all
                case 5 -> { // exit
                    System.out.println("\nThank you for using Doctor Management System");
                    return;
                }
                default -> System.out.printf("\nInvalid choice entered. Please choose again.");
            }
            
        } // End of While Loop
    }
    
    // Menu for NURSE
        public void nurseMenu(){
        while (choice < 1 && choice > 5){ // Repeat the step if the user input invalid choice
            System.out.println("===============================");
            System.out.println("    Staff Management System    ");
            System.out.println("===============================");
            System.out.println("1. Register a new Staff");
            System.out.println("2. Edit Information");
            System.out.println("3. Remove Staff");
            System.out.println("4. View All Staff");
            System.out.println("5. Exit");  
            System.out.println("===============================");
            
            System.out.println("Enter your choice: ");
            choice = scanner.nextInt();

            switch(choice){
                case 1 -> choice = 1; //register
                case 2 -> choice = 1; //edit
                case 3 -> choice = 1; //remove
                case 4 -> choice = 1; //view all
                case 5 -> { // exit
                    System.out.println("\nThank you for using Doctor Management System");
                    return;
                }
                default -> System.out.printf("\nInvalid choice entered. Please choose again.");
            }
            
        } // End of While Loop
    }

    
    // addDoctorUI
    // editDoctorUI
    // removeDoctorUI
    // viewAllDoctorUI
    
}
