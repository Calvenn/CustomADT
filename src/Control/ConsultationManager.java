/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Entity.Appointment;
import Entity.Consultation;
import adt.ADTHeap;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author calve
 */
public class ConsultationManager {
    private final ADTHeap<Consultation> consultationHeap;
    private ADTHeap<Appointment> appointmentHeap;
    //and walkin queue from patient
    private Appointment appt;
    private Scanner scanner = new Scanner(System.in);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public ConsultationManager() {
        appointmentHeap = new ADTHeap<>(false);
        //patient
        consultationHeap = new ADTHeap<>(true); 
    }
    
    /*
    public Object dispatchNextPatient(){
        Appointment nextAppt = appointmentHeap.peekRoot();
        //Patient nextWalkIn = patientHeap.peekRoot();
        
        //if(nextAppt == null) return nextWalkIn.extractRoot();
        //if(nextWalkIn == null) return appointmentHeap.extractRoot();
        
        if (nextWalkIn.getSeverity() > nextAppt.getSeverity()) {
            return walkInQueue.extractRoot();
        } else {
            return appointmentQueue.extractRoot();
        }
    }*/
        
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
        return true;
    }
    
    public void displayAllRecords(){
        consultationHeap.display();
    }
}
