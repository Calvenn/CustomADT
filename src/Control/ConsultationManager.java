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
import Entity.Visit;
import Entity.TreatmentAppointment;

import adt.Heap;
import adt.LinkedHashMap;
import adt.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author CalvenPhnuahKahHong
 */
public class ConsultationManager {
    public Doctor currentDoc;
    
    private final Heap<Appointment> appointmentHeap;
    private final Heap<Visit> queue;
    private final LinkedHashMap<String, List<Consultation>> consultLog;
    private final LinkedHashMap<String, List<TreatmentAppointment>> trtApptHistory;
    private final List<MedRecord> medRecList;
    
    private static Consultation newConsult = null;
    private final AppointmentManager apptManager;
    private final DoctorManager doctorManager;
    
    public ConsultationManager(Heap<Visit> queue, Heap<Appointment> appointmentHeap, LinkedHashMap<String, List<Consultation>> consultLog, LinkedHashMap<String, List<TreatmentAppointment>> trtApptHistory, List<MedRecord> medRecList,DoctorManager doctorManager, AppointmentManager apptManager) {
        this.queue = queue;
        this.appointmentHeap = appointmentHeap;
        this.consultLog = consultLog;
        this.apptManager = apptManager;
        this.doctorManager = doctorManager;
        this.trtApptHistory = trtApptHistory;
        this.medRecList = medRecList;
    }

    public Object dispatchNextPatient() {
        appointmentHeap.display();
        queue.display();

        LocalDateTime now = LocalDateTime.now();

        Appointment bestAppt = findNextValidAppointment(now);
        Visit bestWalkIn = findNextValidWalkIn(); // skip walk-ins that have future appointments

        // --- Handle null cases ---
        if (bestAppt == null && bestWalkIn == null) return null;
        if (bestAppt == null) return queue.extractSpecific(bestWalkIn);
        if (bestWalkIn == null) {
            // Only return appointment if it's in the on-time window
            LocalDateTime apptStart = bestAppt.getDateTime().minusMinutes(5);
            LocalDateTime apptEnd   = bestAppt.getDateTime().plusMinutes(15);
            if (!now.isBefore(apptStart) && !now.isAfter(apptEnd)) {
                return appointmentHeap.extractSpecific(bestAppt);
            } 
        }

        // --- Determine if appointment is on time ---
        LocalDateTime apptStart = bestAppt.getDateTime().minusMinutes(5);
        LocalDateTime apptEnd   = bestAppt.getDateTime().plusMinutes(15);
        boolean apptInWindow = !now.isBefore(apptStart) && !now.isAfter(apptEnd);

        // --- Appointment not yet due, serve walk-in first ---
        if (!apptInWindow) {
            return queue.extractSpecific(bestWalkIn);
        }

        // --- Appointment in window, decide by severity ---
        int apptSeverity = (bestAppt instanceof Consultation) ? ((Consultation) bestAppt).getSeverity() : -1;
        boolean isToday = bestAppt.getDateTime().toLocalDate().equals(LocalDate.now());
        if (isToday && bestWalkIn.getSeverityLevel().getSeverity() > apptSeverity) {
            return queue.extractSpecific(bestWalkIn);
        } else {
            return appointmentHeap.extractSpecific(bestAppt);
        }
    }

    
    private Appointment findNextValidAppointment(LocalDateTime now) {
        for (int i = 0; i < appointmentHeap.size(); i++) {
            Appointment appt = appointmentHeap.get(i);
            if (appt.getDoctor().getID().equals(currentDoc.getID()) &&
                !appt.getDateTime().isBefore(now)) {
                return appt; // first valid appointment
            }
        }
        return null;
    }

    //SKIP APPT DATA
    private Visit findNextValidWalkIn() {
        for (int i = 0; i < queue.size(); i++) {
            Visit v = queue.get(i);

            // Only consider walk-ins for this doctor
            if (!v.getDoctor().getID().equals(currentDoc.getID())) continue;

            // Skip walk-ins that are actually appointments (ID starts with "A")
            if (v.getVisitId().startsWith("A")) continue;

            // Valid walk-in found
            return v;
        }
        return null; // no valid walk-in
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
            apptManager.bookAppointment(newConsult, null, false);
            return newConsult;
        }
    }
    
    public boolean toPharmacy(Doctor doc, Patient patient, Medicine med, int qty, LocalDateTime time){
        if (doc == null || patient == null || med == null || time == null) {
            return false;
        }

        boolean toSaveRec = false;
        MedRecord medCollect = new MedRecord(patient, doc, med, qty, time,toSaveRec, newConsult);
        PaymentManager.paymentRec.add(new Payment(patient, newConsult, Payment.consultPrice + (med.getPrice() * qty), false, null, medCollect));
        return true;
    }
    
    public List<Consultation> displayAllRecordsByDoctor(Doctor doc) {
        List<Consultation> consultations = consultLog.get(doc.getID());

        if (consultations == null || consultations.isEmpty()) {
            return null; 
        }
        return consultations;
    }
    
    public List<Consultation> getRecordsAll() {
        List<Consultation> allConsultations = new List<>();

        // get only doctors from CONSULT dept
        List<Doctor> consultDoctors = doctorManager.getDoctorsByDept("CONSULT");

        if (consultDoctors != null) {
            for (int i = 1; i <= consultDoctors.size(); i++) {
                Doctor doctor = consultDoctors.get(i);
                List<Consultation> consults = consultLog.get(doctor.getID());

                if (consults != null) {
                    for (int j = 1; j <= consults.size(); j++) { 
                        allConsultations.add(consults.get(j));
                    }
                }
            }
        }
        return allConsultations;
    }

    public List<Consultation> displayRecordsByIC(String searchedIC) {
        List<Consultation> resultList = new List<>();
        List<Consultation> consultations = getRecordsAll();

        if (consultations.isEmpty()) return resultList;

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
                    if (!(obj instanceof List)) continue; // skip invalid entries

                    List<TreatmentAppointment> trtList = (List<TreatmentAppointment>) obj;

                    for (int k = 1; k <= trtList.size(); k++) {
                        TreatmentAppointment t = trtList.get(k);
                        if (t == null || t.getConsultation() == null) continue;

                        if (t.getConsultation().getID().equals(cid)) {
                            c.addTreatmentAppointment(t);
                        }
                    }
                }
                resultList.add(c);
            }
        }
        return resultList;
    }
    
    //get consult rec for specific doctor
    public Consultation getConsultRecByDoctor(String id, String docId) {
        Consultation found = null;
        List<Consultation> consultations = consultLog.get(docId);
        if (consultations.isEmpty()) return null;

        for (int i = 1; i <= consultations.size(); i++) {
            Consultation c = consultations.get(i);
            if (c.getID().equals(id)) {
                found = c;
            }
        }
        return found;
    }
    
    //based 
    public Consultation getConsultRec(String id) {
        List<Consultation> consultations = getRecordsAll();
        for (int i = 1; i <= consultations.size(); i++) {
            Consultation c = consultations.get(i);
            if (c != null && c.getID().equals(id)) {
                return c;
            }
        }
        return null;
    }
    
    //Sort by consult start time
     public void sortByDate(List<Consultation> list, boolean ascending) {
        if (list.isEmpty()) return;

        // Bubble sort using ADT 
        for (int i = 1; i <= list.size(); i++) {
            for (int j = 1; j <= list.size() - i; j++) {
                Consultation c1 = list.get(j);
                Consultation c2 = list.get(j + 1);

                boolean needSwap = false;
                if (ascending && c1.getConsultTime().isAfter(c2.getConsultTime())) {
                    needSwap = true;
                } else if (!ascending && c1.getConsultTime().isBefore(c2.getConsultTime())) {
                    needSwap = true;
                }

                if (needSwap) {
                    list.replace(j, c2);
                    list.replace(j + 1, c1);
                }
            }
        }
    }
}
