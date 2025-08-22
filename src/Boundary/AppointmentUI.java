/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

import Entity.Appointment;
import Entity.Doctor;
import Control.AppointmentManager;
import Entity.Consultation;

import exception.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 *
 * @author calve
 */
public class AppointmentUI {
    private final AppointmentManager apptManager;
    private final Scanner scanner;
    protected boolean missedFlag;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public AppointmentUI(AppointmentManager shared) {
        this.apptManager = shared;
        scanner = new Scanner(System.in);     
    }
    
    public void apptMenu(Doctor currentDoc) {
        int choice;
        while (true) {         
            System.out.println("\n" + "=".repeat(35));
            System.out.println("        APPOINTMENT MENU");
            System.out.println("=".repeat(35));
            System.out.println("1. View Appointments");
            System.out.println("2. Update Appointment");
            System.out.println("3. Delete Appointment");        
            System.out.print(missedFlag == true? 
                    "4. Reschedule Miss Appointment"
                    :"");
            System.out.println("0. Back");
            System.out.println("=".repeat(35));         

            if (!missedFlag) {
                choice = ValidationHelper.inputValidatedChoice(0, 3, "your choice");
                switch (choice) {
                    case 1 -> apptManager.displayAllAppointmentByDoctor(currentDoc.getDoctorID());
                    case 2 -> updateAppointmentUI();
                    case 3 -> cancelAppointmentUI();
                    case 0 -> {return;}
                    default -> System.out.println("Invalid choice.\n");
                }
            } else {
                choice = ValidationHelper.inputValidatedChoice(0, 4, "your choice");
                switch (choice) {
                    case 1 -> apptManager.displayAllAppointmentByDoctor(currentDoc.getDoctorID());
                    case 2 -> updateAppointmentUI();
                    case 3 -> cancelAppointmentUI();
                    case 4 -> rescheduleMissedApptUI(currentDoc);
                    case 0 -> {
                        System.out.println("Returning to main menu");
                        return;
                    }
                    default -> System.out.println("Invalid choice.\n");
                }
            }
        }
    }
    
    void bookAppointmentUI(Consultation consultAppt) {
        while (true) {
            suggestNextAvailableSlot();
            LocalDateTime time = ValidationHelper.inputValidatedDateTime("Enter appointment date and time");

            if (apptManager.bookAppointment(consultAppt, time)) {
                System.out.println("Appointment booked successfully!");
                break;
            } else {
                System.out.println("Time slot already taken. Please try a different time.");
            }
        }
    }

    protected void updateAppointmentUI() {
        String ic = ValidationHelper.inputValidatedIC("Enter patient IC number");   // loop until valid IC
        Appointment oldData = apptManager.findPatienInfo(ic);

        if (oldData != null) {
            LocalDateTime newTime = ValidationHelper.inputValidatedDateTime("Enter new appointment time");

            if (getConfirmation("Are you sure you want to change appointment to " + newTime.format(formatter) + " ?")) {
                boolean success = apptManager.updateAppointment(oldData, newTime);
                System.out.println(success ? "Appointment updated." : "Not found or update failed. Please try again");
            }
        } else {
            System.out.println("IC Number " + ic + " not found. Please try again");
        }
    }

    private void cancelAppointmentUI() {
        String ic = ValidationHelper.inputValidatedIC("Enter patient IC number");
        Appointment appt = apptManager.findPatienInfo(ic);

        if (appt != null) {
            System.out.println("Patient Name: " + appt.getPatient().getPatientName());
            System.out.println("Current appointment: " + appt.getDateTime().format(formatter));

            if (getConfirmation("Are you sure you want to cancel patient " + appt.getPatient().getPatientName() + " at " + appt.getDateTime() + " ?")) {
                boolean success = apptManager.cancelAppointment(appt);
                System.out.println(success ? "Appointment cancelled." : "Not found. Please try again");
            }
        } else {
            System.out.println("IC Number " + ic + " not found. Please try again");
        }
    }

    
    private void rescheduleMissedApptUI(Doctor doc) {
        apptManager.displayAllMissedAppt(doc);
        String changedIC = ValidationHelper.inputValidatedIC("Enter patient IC number");

        Appointment a = apptManager.getMissedAppt(doc, changedIC);
        if (a == null) {
            System.out.println("Patient IC " + changedIC + " not found");
            return;
        }

        System.out.println(a);
        LocalDateTime newTime = ValidationHelper.inputValidatedDateTime("Please enter new date and time to reschedule");

        if (getConfirmation("Are you sure you want to change appointment to " + newTime.format(formatter) + " ?")) {
            boolean success = apptManager.bookAppointment(a, newTime);
            if (success) {
                System.out.println("Appointment Updated");
                if (!apptManager.removeMissedAppt(doc, a.getPatient().getPatientIC())) {
                    System.out.println("Removed Unsuccessful. Please try again");
                }
            } else {
                System.out.println("Not found or update failed. Please try again");
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
    
    //Enter patient IC number
}
