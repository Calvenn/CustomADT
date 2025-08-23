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
    public void doctorMenu() {
        int choice = -1;
        while (true) {
            System.out.println("=== Doctor Management System ===");
            System.out.println("1. Register a new Doctor");
            System.out.println("2. Edit Information");
            System.out.println("3. Remove Doctor");
            System.out.println("4. View All Doctors");
            System.out.println("5. Exit");  

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch(choice){
                //case 1 -> registerDoctor();
                //case 2 -> editDoctor();
                //case 3 -> removeDoctor();
                //case 4 -> viewAllDoctors();
                case 5 -> {
                    System.out.println("\nThank you for using Doctor Management System");
                    return;
                }
                default -> System.out.println("\nInvalid choice entered. Please choose again.");
            }
        }
    }

    
    // addDoctorUI
    // editDoctorUI
    // removeDoctorUI
    // viewAllDoctorUI
    
}
