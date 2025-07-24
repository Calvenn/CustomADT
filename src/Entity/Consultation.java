/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author calve
 */
public class Consultation implements Comparable<Consultation>{
    //patient id as foreign key
    private static int idNo = 0; 
    private final String consultationID;
    private final Patient patient;
    private Integer severity;
    private String notes;
    
    
    public Consultation(int severity, Patient patient, String notes){
        this.consultationID = "C" + String.format("%04d", generateId()); 
        this.severity = severity;
        this.patient = patient;
        this.notes = notes;
    }
    
    private static int generateId() {
        idNo += 1; 
        return idNo; 
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
    public String toString(){
        return "Severity Level: " + severity + "\nNotes: " + notes;
    }
}
