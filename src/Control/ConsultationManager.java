/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Entity.Doctor;
import Entity.Appointment;
import Entity.Consultation;
import Entity.MedRecord;
import Entity.Medicine;
import Entity.Patient;
import Entity.Severity;
import Entity.Visit;
import Entity.Treatment;
import Entity.TreatmentAppointment;

import adt.ADTHeap;
import adt.ADTQueue;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 *
 * @author calve
 */
public class ConsultationManager {
    private final ADTHeap<Consultation> consultationHeap;
    private final ADTHeap<Appointment> appointmentHeap;
    private final ADTHeap<Visit> queue;
    
    private final ADTQueue<TreatmentAppointment> treatmentQueue;
    private final ADTQueue<MedRecord> medCollectQueue;
    
    private static Consultation newConsult = null;
    private final Scanner scanner = new Scanner(System.in);
    
    public ConsultationManager(ADTHeap<Visit> queue, ADTHeap<Appointment> appointmentHeap, DoctorManager docManager, ADTQueue<TreatmentAppointment> treatmentQueue, ADTQueue<MedRecord> medCollectQueue) {
        this.queue = queue;
        this.appointmentHeap = appointmentHeap;
        this.consultationHeap = new ADTHeap<>(true);
        this.treatmentQueue = treatmentQueue;
        this.medCollectQueue = medCollectQueue;
    }

    public Object dispatchNextPatient(Doctor doc) {
        Appointment nextAppt = appointmentHeap.peekRoot();
        Visit nextWalkIn = queue.peekRoot();      

        while (nextAppt != null && !nextAppt.getDoctor().getDoctorID().equals(doc.getDoctorID())) {
            appointmentHeap.extractRoot(); // skip unrelated doctor
            nextAppt = appointmentHeap.peekRoot();
        }

        while (nextWalkIn != null && !nextWalkIn.getDoctor().getDoctorID().equals(doc.getDoctorID())) {
            queue.extractRoot(); // skip unrelated doctor
            nextWalkIn = queue.peekRoot();
        }

        if (nextAppt == null && nextWalkIn == null) return null;
        if (nextAppt == null) return queue.extractRoot();
        if (nextWalkIn == null) return appointmentHeap.extractRoot();

        boolean isToday = nextAppt.getTime().toLocalDate().equals(LocalDate.now());

        if (isToday) {
            if (nextWalkIn.getSeverityLevel().getSeverity() > nextAppt.getSeverity()) {
                return queue.extractRoot();
            } else {
                return appointmentHeap.extractRoot();
            }
        } else {
            return queue.extractRoot();
        }
    }
        
    public boolean consultationRecord(Patient patient){
        System.out.print("Enter severity level: ");
        int severity = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter diagnosis/notes: ");
        String notes = scanner.nextLine();
        
        newConsult = new Consultation(severity, patient, notes); //patient id later on
        consultationHeap.insert(newConsult); //chg to linkedhashmap afterward
        //docManager.updateDoctor();
        return true;
    }
    
    public void displayAllRecords(){
        consultationHeap.display();
    }
    
    public boolean toTreatment(Doctor doc, Treatment treatment, String room, LocalDateTime time, Severity sev){
        if (doc == null || treatment == null || room == null || time == null || sev == null) {
            System.out.println("Missing required information for treatment appointment.");
            return false;
        }     
        
        TreatmentAppointment trtAppt = new TreatmentAppointment(doc, newConsult, treatment, room, time, sev);
        treatmentQueue.enqueue(trtAppt);
        return true;
    }
    
    public boolean toPharmacy(Doctor doc, Patient patient, Medicine med, int qty, LocalDateTime time){
        if (doc == null || patient == null || med == null || time == null) {
            System.out.println("Missing data for medicine dispensing.");
            return false;
        }
        
        if (qty <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return false;
        }
        
        MedRecord medCollect = new MedRecord(patient, doc, med, qty, time);
        medCollectQueue.enqueue(medCollect);
        return true;
    }
}
