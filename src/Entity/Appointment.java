/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author calve
 */
public class Appointment implements Comparable<Appointment>{
    private static int idNo = 0; 
    private String apptID;
    private Patient patient;
    private Doctor doctor;
    private int severity;
    private LocalDateTime time;
    
    public Appointment(Patient patient, Doctor doctor, int severity, LocalDateTime time){
        apptID = "A" + String.format("%04d", generateId()); 
        this.patient = patient;
        this.doctor = doctor;
        this.severity = severity;
        this.time = time;
    }
    
    private static int generateId() {
        idNo += 1; 
        return idNo; 
    }
    
    public Patient getPatient(){
        return patient;
    }
   
    public Doctor getDoctor(){
        return doctor;
    }
    
    public int getSeverity(){
        return severity;
    }
    
    public LocalDateTime getTime(){
        return time;
    }
    
    public void setSeverity(int severity){
        this.severity = severity;
    }
    
    public void setTime(LocalDateTime time){
        this.time = time;
    }
    
    //@Override
    public int compareTo(Appointment other){
        return this.time.compareTo(other.time);
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Appointment Details:\n"
             + " Date & Time : " + time.format(formatter) + "\n"
            // + " Patient     : " + patientName + " (" + phoneNum + ")\n"
             + " Doctor      : " + doctor.getDoctorName() + "\n"
             + " Severity    : " + severity;
    }
}
