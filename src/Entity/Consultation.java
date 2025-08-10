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
    private String disease;
    private String notes;
    private LocalDateTime createdAt;
    
    public Consultation(int severity, Patient patient, String disease, String notes, Doctor doc, LocalDateTime apptDateTime){
        super(patient, doc, severity, apptDateTime);
        this.consultationID = "C" + String.format("%04d", generateId()); 
        this.disease = disease;
        this.notes = notes;
        this.createdAt = createdAt.now();
    }
    
    public Consultation(String consultationID, int severity, Patient patient, String disease, String notes, Doctor doc, LocalDateTime apptDateTime) {
        super(patient, doc, severity, apptDateTime);
        this.consultationID = consultationID;
        this.disease = disease;
        this.notes = notes;
        this.createdAt = LocalDateTime.now();
    }
    
    private static int generateId() {
        idNo += 1; 
        return idNo; 
    }
    
    public String getID(){
        return consultationID;
    }
    
    public String getDisease(){
        return disease;
    }
    
    public String getNotes(){
        return notes;
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
        return "\n=== Consultation Record ===\n"
             + "Consultation ID  : " + consultationID + "\n"
             + "Patient Name     : " + getPatient().getPatientName() + "\n"
             + "Patient IC       : " + getPatient().getPatientIC() + "\n"
             + "Severity Level   : " + super.getSeverity() + "\n"
             + "Diagnosis        : " + disease + "\n"
             + "Notes            : " + notes + "\n"   
             + "Doctor In Charge : " + super.getDoctor().getDoctorName() + "\n"
             + "Appointment Time : " + super.getDateTime() + "\n"
             + "===========================\n";
    }
}
