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
public abstract class Appointment implements Comparable<Appointment>{
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime dateTime;
    
    public Appointment(Patient patient, Doctor doctor, LocalDateTime time){
        this.patient = patient;
        this.doctor = doctor;
        this.dateTime = time;
    }
    
    public Patient getPatient(){
        return patient;
    }
   
    public Doctor getDoctor(){
        return doctor;
    }
    
    public LocalDateTime getDateTime(){
        return dateTime;
    }
    
    public void setTime(LocalDateTime time){
        this.dateTime = time;
    }
    
    //@Override
    public int compareTo(Appointment other){
        return this.dateTime.compareTo(other.dateTime);
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Appointment Details:\n"
             + " Date & Time : " + dateTime.format(formatter) + "\n"
             + " Doctor      : " + doctor.getName() + "\n";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Appointment other = (Appointment) obj;
        return this.getPatient().getPatientIC().equals(other.getPatient().getPatientIC())
            && this.getDoctor().getID().equals(other.getDoctor().getID());
    }
    
    public abstract String getAppointmentType();
}