/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;
import java.time.LocalDateTime;
/**
 *
 * @author CalvenPhnuahKahHong
 */
public abstract class Appointment implements Comparable<Appointment>{
    private final Patient patient;
    private final Doctor doctor;
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
    
    public void setDateTime(LocalDateTime time){
        this.dateTime = time;
    }
    
    @Override
    public int compareTo(Appointment other){
        return this.dateTime.compareTo(other.dateTime);
    }
    
    public abstract String getAppointmentType();
}