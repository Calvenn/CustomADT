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
import Entity.Payment;
import Entity.Severity;
import Entity.Visit;
import Entity.Treatment;
import Entity.TreatmentAppointment;

import adt.Heap;
import adt.LinkedHashMap;
import adt.List;
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
    private final LinkedHashMap<String, List<Consultation>> consultLog;
    private final LinkedHashMap<String, TreatmentAppointment> trtApptHistory;
    private final List<MedRecord> medRecList;
    
    private static Consultation newConsult = null;
    private final AppointmentManager apptManager;
    private final DoctorManager doctorManager;
    private final List<Payment> paymentRec;
    
    public ConsultationManager(Heap<Visit> queue, Heap<Appointment> appointmentHeap, LinkedHashMap<String, List<Consultation>> consultLog, LinkedHashMap<String, TreatmentAppointment> trtApptHistory, List<MedRecord> medRecList,DoctorManager doctorManager, AppointmentManager apptManager, List<Payment> payment) {
        this.queue = queue;
        this.appointmentHeap = appointmentHeap;
        this.consultLog = consultLog;
        this.apptManager = apptManager;
        this.doctorManager = doctorManager;
        this.trtApptHistory = trtApptHistory;
        this.medRecList = medRecList;
        this.paymentRec = payment;
    }

    public Object dispatchNextPatient() {
        int severity = -1;
        Appointment nextAppt = appointmentHeap.peekRoot();
        Visit nextWalkIn = queue.peekRoot();  
        
        if (nextAppt instanceof Consultation) severity = ((Consultation) nextAppt).getSeverity();
        
        while (nextAppt != null && (!nextAppt.getDoctor().getID().equals(currentDoc.getID()) || nextAppt.getDateTime().isBefore(LocalDateTime.now()))) {
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
        
        if (isToday && nextWalkIn.getSeverityLevel().getSeverity() > severity) {
            return queue.extractRoot();
        } else {
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
            return false;
        }     
        
        TreatmentAppointment trtAppt = new TreatmentAppointment(doc, newConsult, treatment, room, time);
        toPayment(newConsult.getPatient(), Payment.consultPrice + treatment.getPrice() , trtAppt, null);
        return true;
    }
    
    public boolean toPharmacy(Doctor doc, Patient patient, Medicine med, int qty, LocalDateTime time){
        if (doc == null || patient == null || med == null || time == null) {
            return false;
        }

        boolean toSaveRec = false;
        MedRecord medCollect = new MedRecord(patient, doc, med, qty, time,toSaveRec, newConsult);
        toPayment(patient, Payment.consultPrice + (med.getPrice() * qty), null, medCollect);
        return true;
    }
    
    public boolean toPayment(Patient patient, double price, TreatmentAppointment trtAppt, MedRecord medCollect){
        Payment payment = new Payment(patient, newConsult, price, false, trtAppt, medCollect);
        paymentRec.add(payment);
        return true;
    }
    
    public List<Consultation> displayAllRecordsByDoctor(Doctor doc) {
        List<Consultation> consultations = consultLog.get(doc.getID());

        if (consultations == null || consultations.isEmpty()) {
            System.out.println("No consultation records found for Doctor " + doc.getID());
            return null; 
        }
        return consultations;
    }
    
    public List<Consultation> getRecordsAll() {
        List<Consultation> allConsultations = new List<>();
        Doctor[] doctors = doctorManager.viewAllDoctor();
        for (int i = 0; i < doctors.length; i++) {
            if(doctors[i].getDepartment().equalsIgnoreCase("CONSULT")){
                List<Consultation> consults = consultLog.get(doctors[i].getID());
                for(int j = 1; j <= consults.size(); j++){
                    if (consults != null) {
                        Consultation c = consults.get(j);
                        allConsultations.add(c); 
                    }
                } 
            }
        }
        return allConsultations;
    }

    public List<Consultation> displayRecordsByIC(String searchedIC) {
        List<Consultation> resultList = new List<>();
        List<Consultation> consultations = getRecordsAll();

        if (consultations == null || consultations.size() == 0) {
            return resultList; // return empty list
        }

        for (int i = 0; i < consultations.size(); i++) {
            Consultation c = consultations.get(i);

            if (c == null || c.getPatient() == null) {
                continue; 
            }

            if (c.getPatient().getPatientIC().equals(searchedIC)) {
                String cid = c.getID();

                // med records
                for (int j = 0; j < medRecList.size(); j++) {
                    MedRecord m = medRecList.get(j);
                    if (m != null && m.getConsult() != null && m.getConsult().getID().equals(cid)) {
                        c.addMedRecord(m);
                    }
                }

                // treatment appointments
                Object[] trtHistory = trtApptHistory.getValues();  
                for (Object obj : trtHistory) {
                    System.out.println("test");
                    if (obj instanceof TreatmentAppointment) {
                        TreatmentAppointment t = (TreatmentAppointment) obj;
                        if(t == null) System.out.println("Null trt appt");
                        System.out.println(t);
                        System.out.println(t.getConsultation().getID());
                        if (t.getConsultation() != null && t.getConsultation().getID().equals(cid)) {
                            c.addTreatmentAppointment(t);
                        }
                    }
                }

                resultList.add(c);
            }
        }
        return resultList;
    }
    
    public Consultation getConsultRec(String id, String docId) {
        Consultation found = null;
        List<Consultation> consultations = consultLog.get(docId);
        if (consultations == null || consultations.isEmpty()) {
            return null;
        }

        for (int i = 1; i <= consultations.size(); i++) {
            Consultation c = consultations.get(i);
            if (c.getID().equals(id)) {
                found = c;
            }
        }

        return found;
    }
    
    private List<Consultation> extractConsultRec(String id, DoctorManager docManager) {
        List<Consultation> allConsultations = new List<>();
        Doctor[] doctors = docManager.viewAllDoctor();

        for (int i = 0; i < doctors.length; i++) {
            List<Consultation> doctorConsults = consultLog.get(doctors[i].getID());
            if (doctorConsults != null) {
                for (int j = 1; j <= doctorConsults.size(); j++) { // your ADT is 1-based
                    allConsultations.add(doctorConsults.get(j));
                }
            }
        }
        return allConsultations;
    }
    
    public Consultation getConsultRec(String id, DoctorManager docManager) {
        List<Consultation> consultations = extractConsultRec(id, docManager);
        for (int i = 1; i <= consultations.size(); i++) {
            Consultation c = consultations.get(i);
            if (c != null && c.getID().equals(id)) {
                return c;
            }
        }
        return null;
    }
}
