/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.time.LocalDateTime;

/**
 *
 * @author calve
 */
public class Consultation implements Comparable<Consultation>{
    //for report purpose
    public static int numOfFollowUp = 0;
    public static int numOfPharmacy = 0;
    public static int numOfTreatment = 0;
    
    //patient id as foreign key
    private static int idNo = 0; 
    private final String consultationID;
    private final Patient patient;
    private final Doctor doc;
    private Integer severity;
    private String notes;
    private LocalDateTime time;
    
    
    public Consultation(int severity, Patient patient, String notes, Doctor doc){
        this.consultationID = "C" + String.format("%04d", generateId()); 
        this.severity = severity;
        this.patient = patient;
        this.notes = notes;
        this.doc = doc;
        this.time = time.now();
    }
    
    private static int generateId() {
        idNo += 1; 
        return idNo; 
    }
    
    public String getID(){
        return consultationID;
    }
        
    public Patient getPatient() {
        return patient;
    }
    
    public int getSeverity(){
        return severity;
    }
    
    public String getNotes(){
        return notes;
    }
    
    public Doctor getDoc() {
        return doc;
    }
    
    public void setSeverity(){
        this.severity = severity;
    }
    
    public void setNotes(){
        this.notes = notes;
    }
    
    //@Override
    public int compareTo(Consultation other){
        return this.severity.compareTo(other.severity);
    }
    
    @Override
    public String toString() {
        return "\n=== Consultation Record ===\n"
             + "Consultation ID  : " + consultationID + "\n"
             + "Patient Name     : " + getPatient().getPatientName() + "\n"
             + "Patient IC       : " + getPatient().getPatientIC() + "\n"
             + "Severity Level   : " + severity + "\n"
             + "Diagnosis/Notes  : " + notes + "\n"
             + "Doctor In Charge : " + getDoc().getDoctorName() + "\n"
             + "===========================\n";
    }
}
