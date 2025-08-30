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
    private final Heap<Visit> visitHeap;
    private final LinkedHashMap<String, List<Consultation>> consultLog;
    private final LinkedHashMap<String, List<TreatmentAppointment>> trtApptHistory;
    private final List<MedRecord> medRecList;
    
    private static Consultation newConsult = null;
    private final AppointmentManager apptManager;
    private final DoctorManager doctorManager;
    
    public ConsultationManager(Heap<Visit> visitHeap, Heap<Appointment> appointmentHeap, 
            LinkedHashMap<String, List<Consultation>> consultLog, LinkedHashMap<String, List<TreatmentAppointment>> trtApptHistory, 
            List<MedRecord> medRecList,DoctorManager doctorManager, AppointmentManager apptManager) {
        this.visitHeap = visitHeap;
        this.appointmentHeap = appointmentHeap;
        this.consultLog = consultLog;
        this.apptManager = apptManager;
        this.doctorManager = doctorManager;
        this.trtApptHistory = trtApptHistory;
        this.medRecList = medRecList;
    }

    public Object dispatchNextPatient() {
        LocalDateTime now = LocalDateTime.now();

        Appointment nextAppt = findNextValidAppointment(now);
        Visit nestWalkIn = findNextValidWalkIn(); // skip walk-ins that have future appointments
        
        //handle null case
        if (nextAppt == null && nestWalkIn == null) return null;
        if (nextAppt == null) return visitHeap.extractSpecific(nestWalkIn);
        if (nestWalkIn == null) {
            //only return appointment if is on-time
            LocalDateTime apptStart = nextAppt.getDateTime().minusMinutes(5);
            LocalDateTime apptEnd   = nextAppt.getDateTime().plusMinutes(15);
            if (!now.isBefore(apptStart) && !now.isAfter(apptEnd)) {
                return appointmentHeap.extractSpecific(nextAppt);
            } 
        }

        //determine if appointment is on time (5 min before and 15 min after appt time)
        LocalDateTime apptStart = nextAppt.getDateTime().minusMinutes(5);
        LocalDateTime apptEnd = nextAppt.getDateTime().plusMinutes(15);
        boolean apptIsBetween = now.isAfter(apptStart) && now.isBefore(apptEnd);

        //serve walk-in first, when is not between
        if (!apptIsBetween) {
            return visitHeap.extractSpecific(nestWalkIn);
        }

        //if both come same time, based on severity decide who fist 
        int apptSeverity = (nextAppt instanceof Consultation) ? ((Consultation) nextAppt).getSeverity() : -1;
        boolean isToday = nextAppt.getDateTime().toLocalDate().equals(LocalDate.now());
        if (isToday && nestWalkIn.getSeverityLevel().getSeverity() > apptSeverity) {
            return visitHeap.extractSpecific(nestWalkIn);
        } else {
            return appointmentHeap.extractSpecific(nextAppt);
        }
    }

    private Appointment findNextValidAppointment(LocalDateTime now) {
        for (int i = 0; i < appointmentHeap.size(); i++) {
            Appointment appt = appointmentHeap.get(i);
            if (appt.getDoctor().getID().equals(currentDoc.getID()) &&
                !appt.getDateTime().plusMinutes(15).isBefore(now)) {
                return appt; //first valid appointment
            }
        }
        return null;
    }

    //to skip appt data
    private Visit findNextValidWalkIn() {
        for (int i = 0; i < visitHeap.size(); i++) {
            Visit v = visitHeap.get(i);

            //only consider walk in for this doctor
            if (!v.getDoctor().getID().equals(currentDoc.getID())) continue;

            //skip appointments (ID starts with "A")
            if (v.getVisitId().startsWith("A")) {
                visitHeap.remove(v); //to make sure is remove from visit heap
                continue;
            }

            return v;
        }
        return null; 
    }
        
    public Consultation consultationRecord(String id, Patient patient, int severity, String diagnosis, String notes, LocalDateTime startTime, LocalDateTime createdAt) {
        //get existing consultation list for doctor
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
    
    //to add med collect to pharmacy
    public boolean toPharmacy(Doctor doc, Patient patient, Medicine med, int qty, LocalDateTime time){
        if (doc == null || patient == null || med == null || time == null) {
            return false;
        }

        boolean toSaveRec = false;
        MedRecord medCollect = new MedRecord(patient, doc, med, qty, time,toSaveRec, newConsult);
        PaymentManager.paymentRec.add(new Payment(patient, newConsult, Payment.consultPrice + (med.getPrice() * qty), false, null, medCollect));
        return true;
    }
    
    //return consultation record by doctor
    public List<Consultation> displayAllRecordsByDoctor(Doctor doc) {
        List<Consultation> consultations = consultLog.get(doc.getID());

        if (consultations.isEmpty()) {
            return null; 
        }
        return consultations;
    }
    
    //return all consultation record
    public List<Consultation> getRecordsAll() {
        List<Consultation> allConsultations = new List<>();

        //get only doctors from CONSULT dept
        List<Doctor> consultDoctors = doctorManager.getDoctorsByDept("CONSULT");

        if (!consultDoctors.isEmpty()) {
            for (int i = 1; i <= consultDoctors.size(); i++) {
                Doctor doctor = consultDoctors.get(i);
                List<Consultation> consults = consultLog.get(doctor.getID());

                if (!consults.isEmpty()) {
                    for (int j = 1; j <= consults.size(); j++) { 
                        allConsultations.add(consults.get(j));
                    }
                }
            }
        }
        return allConsultations;
    }

    //return consultation record for specific patient
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

    //get consult rec for specific record
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
