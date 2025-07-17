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
    private Integer  severity;
    private String notes;
    
    //private String treatmentAccessID; //sort by time, the earliest at the top
    //private String pharmacyCollectID; //sort by time, the earliest at the top
    
    public Consultation(int severity, String notes){
        this.severity = severity;
        this.notes = notes;
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
