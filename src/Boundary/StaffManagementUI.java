
package Boundary;

import java.util.Scanner;
import Control.StaffManager;
import Control.DoctorManager;
/**
 *
 * @author tanjixian
 */
public class StaffManagementUI {
        
    // Variables
    Scanner scanner = new Scanner(System.in);
    StaffManager staffManager;
    DoctorManager docManager;
    int choice;
    
    // Constructor
    public StaffManagementUI(StaffManager staffManager, DoctorManager docManager){
        this.staffManager = staffManager;
        this.docManager = docManager;
    }
    
   //FUNCTIONS 


 

    
    // 
    // editDoctorUI
    // removeDoctorUI
    // viewAllDoctorUI
    
    //Menu
    // Menu Title
    public void printTitle(String title, String position, String name){
        int center = (35 + title.length()) / 2;
        System.out.println("WELCOME (" + position + ") " + name);
        System.out.println("\n===================================");
        System.out.printf("%" + center + "s\n", title);
        System.out.println("===================================");
    }
   
    // Staff Menu
    public void staffMenu(){
        while (choice < 1 && choice > 5){ // Repeat the step if the user input invalid choice
            //printTitle("Staff Management Module", "ADMIN", staffManager.findStaff(userID).getName());
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
                    System.out.println("\nThank you for using Staff Management System");
                    return;
                }
                default -> System.out.printf("\nInvalid choice entered. Please choose again.");
            }
            
        } // End of While Loop
    }
}
