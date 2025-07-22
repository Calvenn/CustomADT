/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

import Entity.Appointment;
import Entity.Doctor;
import Control.AppointmentManager;
import Control.ConsultationManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 *
 * @author calve
 */
public class AppointmentUI {
    private AppointmentManager apptManager;
    private Scanner scanner;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public AppointmentUI(AppointmentManager shared) {
        this.apptManager = shared;
        scanner = new Scanner(System.in);     
    }
    
    public void apptMenu() {
        while (true) {
            System.out.println("Total Appointment: " + apptManager.totalAppointments());
            System.out.println("Incoming Appointment " + apptManager.getIncomingAppointment());
            
            System.out.println("\n--- Appointment Menu ---");
            System.out.println("1. View Appointments");
            System.out.println("2. Update Appointment");
            System.out.println("3. Delete Appointment");
            System.out.println("4. Back");

            System.out.print("Choose > ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            switch (choice) {
                case 1 -> apptManager.displayAllAppointments();
                case 2 -> updateAppointmentUI();
                case 3 -> cancelAppointmentUI();
                case 4 ->{
                    System.out.println("Returning to main menu");
                    return;
                }
                default -> System.out.println("Invalid choice.\n");
            }
        }
    }
    
    void bookAppointmentUI(String patient, String phoneNum, String doctor, int severity, Doctor currentDoc) {
        LocalDateTime time = null;

        while (true) {
            suggestNextAvailableSlot();
            System.out.print("Enter appointment date and time (yyyy-MM-dd HH:mm): ");
            String input = scanner.nextLine();

            try {
                time = LocalDateTime.parse(input, formatter);

                boolean success = apptManager.bookAppointment(patient, phoneNum, doctor, severity, time, currentDoc);
                if (success) {
                    System.out.println("Appointment booked successfully!");
                    break;
                } else {
                    System.out.println("Failed to book appointment. Please try a different time.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format! Please use yyyy-MM-dd HH:mm (e.g. 2024-12-20 08:00)");
            }
        }
    }

    
    private void updateAppointmentUI() {
        System.out.print("Enter patient phone number: ");
        String phoneNum = scanner.nextLine();
        
        Appointment oldData = apptManager.findPatienInfo(phoneNum, formatter);
        
        if(oldData != null){
            System.out.print("Enter new appointment time (yyyy-MM-dd HH:mm): ");
            String newTimeStr = scanner.nextLine();
            LocalDateTime newTime = LocalDateTime.parse(newTimeStr, formatter);
            
            if(getConfirmation("Are you sure want to change appointment to " + newTime.format(formatter) + " ?")){
                boolean success = apptManager.updateAppointment(oldData, newTime, formatter);
                System.out.println(success ? "Appointment updated." : "Not found or update failed. Please try again");  
            }
        } else {
            System.out.println("Phone Number " + phoneNum + " not found. Please try again");
        }
    }

    private void cancelAppointmentUI() {
        System.out.print("Enter patient phone number: ");
        String phoneNum = scanner.nextLine();

        Appointment appt = apptManager.findPatienInfo(phoneNum, formatter);

        if(appt != null){
            if(getConfirmation("Are you sure want to cancel patient " + appt.getPatientName() + " at " + appt.getTime() + " ?")){
                boolean success = apptManager.cancelAppointment(appt, formatter);
                System.out.println(success ? "Appointment cancelled." : "Not found. Please try again");
            }
        } else {
            System.out.println("Phone Number " + phoneNum + " not found. Please try again");
        }
    }
    
    private void suggestNextAvailableSlot() {
        LocalDate today = LocalDate.now();
        LocalDateTime slot = apptManager.findNextAvailableSlot();
        if (slot != null) {
            System.out.println("Earliest available slot today: " +
                    slot.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        } else {
            System.out.println("No available slots today.");
        }
    }
    
    private boolean getConfirmation(String message) {
        while (true) {
            System.out.print(message + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Please enter 'y' or 'n'");
            }
        }
    }
}
