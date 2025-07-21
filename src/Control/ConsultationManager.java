/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Entity.Appointment;
import Entity.Consultation;
import Entity.Visit;
import adt.ADTHeap;
import java.time.LocalDate;
import java.util.Scanner;

/**
 *
 * @author calve
 */
public class ConsultationManager {
    private final ADTHeap<Consultation> consultationHeap;
    private ADTHeap<Appointment> appointmentHeap;
    private ADTHeap<Visit> queue;
    private Scanner scanner = new Scanner(System.in);
    
    public ConsultationManager(ADTHeap<Visit> queue, ADTHeap<Appointment> appointmentHeap) {
        this.queue = queue;
        this.appointmentHeap = appointmentHeap;
        this.consultationHeap = new ADTHeap<>(true);
    }

    public Object dispatchNextPatient() {
        System.out.print(queue.size());
        Appointment nextAppt = appointmentHeap.peekRoot();
        Visit nextWalkIn = queue.peekRoot();

        // If both are empty
        if (nextAppt == null && nextWalkIn == null) return null;
        if (nextAppt == null) return queue.extractRoot();
        if (nextWalkIn == null) return appointmentHeap.extractRoot();

        // Check if appointment is today
        boolean isToday = nextAppt.getTime().toLocalDate().equals(LocalDate.now());

        // Dispatch based on severity if appointment is today
        if (isToday) {
            if (nextWalkIn.getSeverityLevel().getSeverity() > nextAppt.getSeverity()) {
                return queue.extractRoot();
            } else {
                return appointmentHeap.extractRoot();
            }
        } else {
            // Appointment not today → prioritize walk-in
            return queue.extractRoot();
        }
    }

        
    public boolean consultationRecord(){
        //check patient flag see whether it is true or not if not thn set the severity in patient module 
        
        //get severity table from patient
        System.out.print("Enter severity level: ");
        int severity = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter diagnosis/notes: ");
        String notes = scanner.nextLine();
        
        Consultation newConsult = new Consultation(severity, notes); //patient id later on
        consultationHeap.insert(newConsult);
        //docManager.updateDoctor();
        return true;
    }
    
    public void displayAllRecords(){
        consultationHeap.display();
    }
}
