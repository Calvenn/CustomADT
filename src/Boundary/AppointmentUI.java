/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

import Entity.Appointment;
import Entity.Doctor;
import Control.AppointmentManager;
import Entity.Patient;

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
    protected boolean missedFlag;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public AppointmentUI(AppointmentManager shared) {
        this.apptManager = shared;
        scanner = new Scanner(System.in);     
    }
    
    public void apptMenu(Doctor currentDoc) {
        while (true) {
            //System.out.println("Total Appointment: " + apptManager.totalAppointments());
            //System.out.println("Incoming Appointment " + apptManager.getIncomingAppointment());
            
            System.out.println("\n--- Appointment Menu ---");
            System.out.println("1. View Appointments");
            System.out.println("2. Update Appointment");
            System.out.println("3. Delete Appointment");
            System.out.print(missedFlag == true? 
                    "4. Reschedule Miss Appointment"
                    :"");
            System.out.println(missedFlag == false? 
                    "4. Back"
                    :"\n5. Back");

            System.out.print("Choose > ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            if (!missedFlag) {
                switch (choice) {
                    case 1 -> apptManager.displayAllAppointmentByDoctor(currentDoc.getDoctorID());
                    case 2 -> updateAppointmentUI();
                    case 3 -> cancelAppointmentUI();
                    case 4 -> {
                        System.out.println("Returning to main menu");
                        return;
                    }
                    default -> System.out.println("Invalid choice.\n");
                }
            } else {
                switch (choice) {
                    case 1 -> apptManager.displayAllAppointmentByDoctor(currentDoc.getDoctorID());
                    case 2 -> updateAppointmentUI();
                    case 3 -> cancelAppointmentUI();
                    case 4 -> rescheduleMissedApptUI(currentDoc);
                    case 5 -> {
                        System.out.println("Returning to main menu");
                        return;
                    }
                    default -> System.out.println("Invalid choice.\n");
                }
            }
        }
    }
    
    void bookAppointmentUI(Patient patient, int severity, Doctor currentDoc) {
        LocalDateTime time = null;

        while (true) {
            suggestNextAvailableSlot();
            System.out.print("Enter appointment date and time (yyyy-MM-dd HH:mm): ");
            String input = scanner.nextLine();


            try {
                time = LocalDateTime.parse(input, formatter);
                
                if (time.isBefore(LocalDateTime.now())) {
                    System.out.println("Cannot book in the past.");
                }

                boolean success = apptManager.bookAppointment(patient, severity, time, currentDoc);
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
            if(getConfirmation("Are you sure want to cancel patient " + appt.getPatient().getPatientName() + " at " + appt.getTime() + " ?")){
                boolean success = apptManager.cancelAppointment(appt, formatter);
                System.out.println(success ? "Appointment cancelled." : "Not found. Please try again");
            }
        } else {
            System.out.println("Phone Number " + phoneNum + " not found. Please try again");
        }
    }
    
    private void rescheduleMissedApptUI(Doctor doc){
        apptManager.displayAllMissedAppt(doc);
        System.out.print("Enter patient IC to change appointment date: ");
        String changedIC = scanner.nextLine();
        
        Appointment a = apptManager.getMissedAppt(doc, changedIC);
        if(a == null) System.out.println("Patient IC " + changedIC + " not found");
        System.out.println(a);
        
        System.out.print("Please enter new date and time to reschedule (yyyy-MM-dd HH:mm): ");
        String newTimeStr = scanner.nextLine();
        LocalDateTime newTime = LocalDateTime.parse(newTimeStr, formatter);
        
        if(getConfirmation("Are you sure want to change appointment to " + newTime.format(formatter) + " ?")){
           boolean success = apptManager.bookAppointment(a.getPatient(), a.getSeverity(),newTime, doc);
           System.out.println(success ? 
                   "Appointment updated." : 
                   "Not found or update failed. Please try again");
           if(apptManager.removeMissedAppt(doc, a.getPatient().getPatientIC())){
               System.out.println("Successful");
           }
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
