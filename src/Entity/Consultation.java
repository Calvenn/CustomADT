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
public class Consultation extends Appointment{
    //for report purpose
    public static int numOfFollowUp = 0;
    public static int numOfPharmacy = 0;
    public static int numOfTreatment = 0;
    
    //patient id as foreign key
    private static int idNo = 0; 
    private final String consultationID; //C0001
    private int severity;
    private String disease;
    private String notes;
    private LocalDateTime consultTime;
    private LocalDateTime createdAt;
    
    public Consultation(int severity, Patient patient, String disease, String notes, Doctor doc, LocalDateTime consultTime, LocalDateTime apptDateTime, LocalDateTime createdAt){
        super(patient, doc, apptDateTime);
        this.consultationID = "C" + String.format("%04d", generateId()); 
        this.severity = severity;
        this.disease = disease;
        this.notes = notes;
        this.consultTime = consultTime;
        this.createdAt = createdAt;
    }
    
    public Consultation(String consultationID, int severity, Patient patient, String disease, String notes, Doctor doc, LocalDateTime consultTime, LocalDateTime apptDateTime, LocalDateTime createdAt) {
        super(patient, doc, apptDateTime);
        this.consultationID = consultationID;
        this.severity = severity;
        this.disease = disease;
        this.notes = notes;
        this.consultTime = consultTime;
        this.createdAt = createdAt;
    }
    
    private static int generateId() {
        idNo += 1; 
        return idNo; 
    }
    
    public String getID(){
        return consultationID;
    }
    
    public int getSeverity(){
        return severity;
    }
    
    public String getDisease(){
        return disease;
    }
    
    public String getNotes(){
        return notes;
    }
    
    public LocalDateTime getConsultTime(){
        return consultTime;
    }
    
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    
    public void setNotes(){
        this.notes = notes;
    }
    
    @Override
    public String getAppointmentType() {
        return "Consultation";
    }

    @Override
    public String toString() {
        return "\n================ Consultation Record ================\n"
             + String.format("%-20s: %s%n", "Consultation ID", consultationID)
             + String.format("%-20s: %s%n", "Patient Name", getPatient().getPatientName())
             + String.format("%-20s: %s%n", "Patient IC", getPatient().getPatientIC())
             + String.format("%-20s: %d%n", "Severity Level", severity)
             + String.format("%-20s: %s%n", "Diagnosis", disease)
             + String.format("%-20s: %s%n", "Notes", notes)
             + String.format("%-20s: %s%n", "Doctor In Charge", getDoctor().getName())
             + String.format("%-20s: %s%n", "Appointment Time", getDateTime())
             + "====================================================\n";
    }   
}