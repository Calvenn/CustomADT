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

import adt.Heap;
import adt.LinkedHashMap;
import adt.List;
import adt.Queue;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 *
 * @author calve
 */
public class ConsultationManager {
    public Doctor currentDoc;
    
    private final Heap<Appointment> appointmentHeap;
    private final Heap<Visit> queue;
    
    private final Queue<TreatmentAppointment> treatmentQueue;
    private final Queue<MedRecord> medCollectQueue;
    
    private final LinkedHashMap<String, List<Consultation>> consultLog;
    
    private static Consultation newConsult = null;
    private final Scanner scanner = new Scanner(System.in);
    
    private Visit currentlyProcessing;
    private Visit nextPatient;
    
    public ConsultationManager(Heap<Visit> queue, Heap<Appointment> appointmentHeap, DoctorManager docManager, LinkedHashMap<String, List<Consultation>> consultLog, Queue<TreatmentAppointment> treatmentQueue, Queue<MedRecord> medCollectQueue) {
        this.queue = queue;
        this.appointmentHeap = appointmentHeap;
        this.consultLog = consultLog;
        this.treatmentQueue = treatmentQueue;
        this.medCollectQueue = medCollectQueue;
    }
    
    /*
    public Visit processNextPatient() {
        if (currentlyProcessing == null && !queue.isEmpty()) {
            // Set the first patient as currently processing (don't remove from queue)
            currentlyProcessing = queue.get(0);
            
            return currentlyProcessing;
        }
        return null;
    }

    public Visit getCurrentlyProcessing() {
        return currentlyProcessing;
    }*/

    public Object dispatchNextPatient() {
        Appointment nextAppt = appointmentHeap.peekRoot();
        Visit nextWalkIn = queue.peekRoot();      

        while (nextAppt != null && !nextAppt.getDoctor().getDoctorID().equals(currentDoc.getDoctorID())) {
            appointmentHeap.extractRoot(); // skip unrelated doctor
            nextAppt = appointmentHeap.peekRoot();
        }

        while (nextWalkIn != null && !nextWalkIn.getDoctor().getDoctorID().equals(currentDoc.getDoctorID())) {
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
        
    public boolean consultationRecord(Patient patient) {
        System.out.print("Enter severity level: ");
        int severity = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter diagnosis/notes: ");
        String notes = scanner.nextLine();

        newConsult = new Consultation(severity, patient, notes, currentDoc); 

        // Get existing consultation list for doctor
        List<Consultation> consultations = consultLog.get(currentDoc.getDoctorID());

        if (consultations == null) {
            consultations = new List<>(); 
        }

        consultations.add(newConsult);
        consultLog.put(currentDoc.getDoctorID(), consultations);
        return true;
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
    
    public boolean displayAllRecordsByDoctor(Doctor doc) {
        Consultation record = null;
        System.out.println("GET Doctor ID: " + doc.getDoctorID());
        List<Consultation> consultations = consultLog.get(doc.getDoctorID());
        if (consultations == null || consultations.isEmpty()) {
            System.out.println("No consultation records found for Doctor " + doc.getDoctorID());
            return false;
        }

        for (int i = 1; i <= consultations.size(); i++) {
            record = consultations.get(i);
            System.out.println(record);
        }

        return true;
    }

    public boolean displayRecordsByIC(String searchedIC) {
        List<Consultation> consultations = consultLog.get(currentDoc.getDoctorID());
        if (consultations == null || consultations.isEmpty()) {
            System.out.println("No consultation records found.");
            return false;
        }

        boolean found = false;
        for (int i = 1; i <= consultations.size(); i++) {
            Consultation c = consultations.get(i);
            if (c.getPatient().getPatientIC().equals(searchedIC)) {
                System.out.println(c);
                found = true;
            }
        }

        return found;
    }
}
