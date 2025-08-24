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
    private final AppointmentManager apptManager;
    
    public ConsultationManager(Heap<Visit> queue, Heap<Appointment> appointmentHeap, DoctorManager docManager, LinkedHashMap<String, List<Consultation>> consultLog, Queue<TreatmentAppointment> treatmentQueue, Queue<MedRecord> medCollectQueue, AppointmentManager apptManager) {
        this.queue = queue;
        this.appointmentHeap = appointmentHeap;
        this.consultLog = consultLog;
        this.treatmentQueue = treatmentQueue;
        this.medCollectQueue = medCollectQueue;
        this.apptManager = apptManager;
    }

    public Object dispatchNextPatient() {
        int severity = -1;
        System.out.println(queue.size());
        Appointment nextAppt = appointmentHeap.peekRoot();
        Visit nextWalkIn = queue.peekRoot();  
        
        if (nextAppt instanceof Consultation) severity = ((Consultation) nextAppt).getSeverity();
        
        while (nextAppt != null && !nextAppt.getDoctor().getID().equals(currentDoc.getID())) {
            appointmentHeap.extractRoot(); // skip unrelated doctor
            nextAppt = appointmentHeap.peekRoot();
        }

        while (nextWalkIn != null && !nextWalkIn.getDoctor().getID().equals(currentDoc.getID())) {
            queue.extractRoot(); // skip unrelated doctor
            nextWalkIn = queue.peekRoot();
        }

        if (nextAppt == null && nextWalkIn == null) return null;
        if (nextAppt == null) return queue.extractRoot();
        if (nextWalkIn == null) return appointmentHeap.extractRoot();

        boolean isToday = nextAppt.getDateTime().toLocalDate().equals(LocalDate.now());

        System.out.println(nextWalkIn);
        
        if (isToday && nextWalkIn.getSeverityLevel().getSeverity() > severity) {
            System.out.println("Visit queue extracted");
            return queue.extractRoot();
        } else {
            System.out.println("Appt queue extracted");
            return appointmentHeap.extractRoot();  // Extract appointment otherwise           
        }
    }   
        
    public Consultation consultationRecord(String id, Patient patient, int severity, String diagnosis, String notes, LocalDateTime startTime, LocalDateTime createdAt) {
        // Get existing consultation list for doctor
        List<Consultation> consultations = consultLog.get(currentDoc.getID());
        
        if(id == null){
            newConsult = new Consultation(severity, patient, diagnosis, notes, currentDoc, startTime, null, createdAt); 
            consultations.add(newConsult);
            consultLog.put(currentDoc.getID(), consultations);
            return newConsult;
        } else {
            newConsult = new Consultation(id, severity, patient, diagnosis, notes, currentDoc, startTime, null, createdAt);
            apptManager.bookAppointment(newConsult, null);
            return newConsult;
        }
    }
    
    public boolean toTreatment(Doctor doc, Treatment treatment, String room, LocalDateTime time, Severity sev){
        if (doc == null || treatment == null || room == null || time == null || sev == null) {
            System.out.println("Missing required information for treatment appointment.");
            return false;
        }     
        
        TreatmentAppointment trtAppt = new TreatmentAppointment(doc, newConsult, treatment, room, time);
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
        boolean toSaveRec = false;
        MedRecord medCollect = new MedRecord(patient, doc, med, qty, time,toSaveRec);
        medCollectQueue.enqueue(medCollect);
        return true;
    }
    
    public boolean displayAllRecordsByDoctor(Doctor doc) {
        Consultation record = null;
        List<Consultation> consultations = consultLog.get(doc.getID());
        if (consultations == null || consultations.isEmpty()) {
            System.out.println("No consultation records found for Doctor " + doc.getID());
            return false;
        }
        
        System.out.println(consultations.size());

        for (int i = 1; i <= consultations.size(); i++) {
            record = consultations.get(i);
            System.out.println(record);
            //dislay med rec & trt rec if have (TMRRRRRRRRRRRRRRRRRRRRRR)
        }
        return true;
    }

    public boolean displayRecordsByIC(String searchedIC) {
        List<Consultation> consultations = consultLog.get(currentDoc.getID());
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
    
    public List<String> suggestedTrt(String symptoms) {
        List<String> trtType = new List<>();
        String input = symptoms.toLowerCase();

        if (input.contains("fever") || input.contains("fatigue")) {
            trtType.add("Blood test");
            trtType.add("Urine test");
        } else if (input.contains("cough") || input.contains("shortness of breath")) {
            trtType.add("X-ray");
            trtType.add("Nebuliser");
        } else if (input.contains("allergy") || input.contains("rash")) {
            trtType.add("Allergy test");
            trtType.add("Cryotherapy");
        } else if (input.contains("injury") || input.contains("wound")) {
            trtType.add("Wound care");
            trtType.add("Physical therapy");
        } else if (input.contains("vision problem") || input.contains("blurred vision")) {
            trtType.add("Eye Examination");
        } else if (input.contains("dehydration")) {
            trtType.add("IV Fluid therapy");
        } else if (input.contains("pregnancy")) {
            trtType.add("Ultrasound");
        } else if (input.contains("vaccination") || input.contains("flu prevention")) {
            trtType.add("Vaccination");
        } else {
            trtType.add("General checkup");
        }

        return trtType;
    }
    
    public List<String> suggestedMeds(String symptoms) {
        String[][] medicines = {
            {"Panadol", "fever", "headache", "pain"},
            {"Amoxicillin", "infection", "bacteria", "throat infection"},
            {"Vitamin C", "immunity", "cold", "flu"},
            {"Loratadine", "allergy", "itchy", "runny nose", "sneeze"},
            {"Omeprazole", "acid", "stomach", "reflux", "heartburn"},
            {"Paracetamol", "pain", "fever"},
            {"Ibuprofen", "inflammation", "swelling", "pain"},
            {"Salbutamol", "asthma", "shortness of breath", "wheezing"},
            {"Aspirin", "blood clot", "chest pain", "heart", "stroke"},
            {"Metformin", "diabetes", "sugar", "glucose"}
        };

        List<String> suggested = new List<>();
        String lowerSymptoms = symptoms.toLowerCase();

        for (String[] med : medicines) {
            String medName = med[0];
            for (int i = 1; i < med.length; i++) {
                if (lowerSymptoms.contains(med[i].toLowerCase())) {
                    if (!suggested.contains(medName)) {
                        suggested.add(medName);
                    }
                }
            }
        }

        return suggested;
    }

}
