/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

import java.util.Scanner;
/**
 *
 * @author tanjixian
 */
public class DoctorManagementUI {
    // Variables
    int choice = 0;     // Used for menu navigation
    Scanner scanner  = new Scanner(System.in);
    //
    
    // Menu for "Doctor Management System"
    public void doctorMenu(){
        while (choice < 1 && choice > 5){ // Repeat the step if the user input invalid choice
            System.out.println("=== Doctor Management System ===");
            System.out.println("1. Register a new Doctor");
            System.out.println("2. Edit Information");
            System.out.println("3. Remove Doctor");
            System.out.println("4. View All Doctors");
            System.out.println("5. Exit");  

            System.out.println("Enter your choice: ");
            choice = scanner.nextInt();

            switch(choice){
                case 1 -> choice = 1; //register
                case 2 -> choice = 1; //edit
                case 3 -> choice = 1; //remove
                case 4 -> choice = 1; // view all
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
